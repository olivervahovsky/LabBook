package vahovsky.LabBook.persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;

public class MysqlLaboratoryDAO implements LaboratoryDAO {

	private JdbcTemplate jdbcTemplate;

	public MysqlLaboratoryDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addLaboratory(Laboratory laboratory) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		insert.withTableName("laboratory");
		insert.usingGeneratedKeyColumns("id_laboratory");
		insert.usingColumns("name", "location");

		Map<String, Object> values = new HashMap<>();
		values.put("name", laboratory.getName());
		values.put("location", laboratory.getLocation());

		laboratory.setLaboratoryID(insert.executeAndReturnKey(values).longValue());
	}

	@Override
	public List<Laboratory> getAll() {
		String sql = "SELECT id_laboratory, name, location " + "FROM laboratory";
		return jdbcTemplate.query(sql, new RowMapper<Laboratory>() {

			@Override
			public Laboratory mapRow(ResultSet rs, int rowNum) throws SQLException {
				Laboratory laboratory = new Laboratory();
				laboratory.setLaboratoryID(rs.getLong("id_laboratory"));
				laboratory.setName(rs.getString("name"));
				laboratory.setLocation(rs.getString("location"));
				return laboratory;
			}
		});
	}

	@Override
	public void saveLaboratory(Laboratory laboratory) {
		if (laboratory == null)
			return;
		if (laboratory.getLaboratoryID() == null) { // CREATE
			addLaboratory(laboratory);
		} else { // UPDATE
			String sql = "UPDATE laboratory SET " + "name = ?, location = ? " + "WHERE id_laboratory = ?";
			jdbcTemplate.update(sql, laboratory.getName(), laboratory.getLocation(), laboratory.getLaboratoryID());
		}
	}

	@Override
	public void deleteLaboratory(Laboratory laboratory) {
		// First delete the reference of the laboratory to be removed from those items,
		// which formerly belonged to this laboratory
		String sql = "UPDATE item SET " + "laboratory_id_laboratory = ? " + "WHERE laboratory_id_laboratory = ?";
		jdbcTemplate.update(sql, null, laboratory.getLaboratoryID());
		// Also delete the reference of the laboratory to be removed from those tasks,
		// which formerly belonged to this laboratory
		sql = "UPDATE task SET " + "laboratory_id_laboratory = ? " + "WHERE laboratory_id_laboratory = ?";
		jdbcTemplate.update(sql, null, laboratory.getLaboratoryID());
		// Finally, delete the laboratory itself
		jdbcTemplate.update("DELETE FROM laboratory WHERE id_laboratory = " + laboratory.getLaboratoryID());
	}

	@Override
	public Laboratory getLaboratoryByID(Long id) {
		String sql = "SELECT id_laboratory AS laboratoryID, name, location " + "FROM laboratory "
				+ "WHERE id_laboratory = " + id;
		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Laboratory.class));
	}
	
	@Override
	public List<Item> getItemsOfLaboratory(Laboratory laboratory) {
		ItemDAO itemDao = DAOfactory.INSTANCE.getItemDAO();
		List<Item> items = new ArrayList<>();
		if (itemDao.getAll() != null) {
			List<Item> allItems = itemDao.getAll();
			for (Item i : allItems) {
				if (i.getLaboratory() != null)
					if (i.getLaboratory().getLaboratoryID().equals(laboratory.getLaboratoryID())) {
						items.add(i);
					}
			}
		}
		return items;
	}

}
