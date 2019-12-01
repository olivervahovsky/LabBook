package vahovsky.LabBook.persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import vahovsky.LabBook.entities.Entity;
import vahovsky.LabBook.entities.Item;

public class MysqlItemDAO implements ItemDAO {

	private JdbcTemplate jdbcTemplate;

	public MysqlItemDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addItem(Item item) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		insert.withTableName("item");
		insert.usingGeneratedKeyColumns("id_item");
		insert.usingColumns("name", "quantity", "available", "laboratory_id_laboratory");

		Map<String, Object> values = new HashMap<>();
		values.put("name", item.getName());
		values.put("quantity", item.getQuantity());
		values.put("available", item.isAvailable());
		if (item.getLaboratory() != null)
			values.put("laboratory_id_laboratory", item.getLaboratory().getEntityID());
		else
			values.put("laboratory_id_laboratory", null);
		item.setItemID(insert.executeAndReturnKey(values).longValue());
	}

	@Override
	public void saveItem(Item item) {
		if (item == null)
			return;
		if (item.getEntityID() == null) { // CREATE
			addItem(item);
		} else { // UPDATE
			Long laboratoryID = null;
			if (item.getLaboratory() != null) {
				laboratoryID = item.getLaboratory().getEntityID();
			}
			String sql = "UPDATE item SET " + "name = ?, quantity = ?, available = ?, laboratory_id_laboratory = ? "
					+ "WHERE id_item = ?";
			jdbcTemplate.update(sql, item.getName(), item.getQuantity(), item.isAvailable(), laboratoryID,
					item.getEntityID());
		}
	}

	@Override
	public List<Item> getAll() {
		String sql = "SELECT id_item, name, quantity, available, laboratory_id_laboratory " + "FROM lab_book.item";
		return jdbcTemplate.query(sql, new RowMapper<Item>() {

			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				Item item = new Item();
				item.setItemID(rs.getLong("id_item"));
				item.setName(rs.getString("name"));
				item.setQuantity(rs.getInt("quantity"));
				item.setAvailable(rs.getBoolean("available"));
				if (rs.getObject("laboratory_id_laboratory") != null)
					item.setLaboratory(DAOfactory.INSTANCE.getLaboratoryDAO()
							.getLaboratoryByID(rs.getLong("laboratory_id_laboratory")));
				return item;
			}
		});
	}

	@Override
	public void deleteEntity(Entity item) {
		// First delete all the notes belonging to the item, as they cannot be accessed
		// after item deletion
		jdbcTemplate.update("DELETE FROM note WHERE item_id_item = ?", item.getEntityID());
		// Next delete also all the rows of the table task_has_item corresponding to the
		// item to be deleted
		jdbcTemplate.update("DELETE FROM task_has_item WHERE item_id_item = ?", item.getEntityID());
		// Finally, delete the item itself
		jdbcTemplate.update("DELETE FROM item WHERE id_item = " + item.getEntityID());
	}

	@Override
	public Item getByID(Long id) {
		String sql = "SELECT id_item AS itemID, name, quantity, available, laboratory_id_laboratory " + "FROM item "
				+ "WHERE id_item = " + id;
		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Item.class));
	}

}
