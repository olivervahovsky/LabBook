package vahovsky.LabBook.persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.Entity;

public class MysqlAdminDAO implements AdminDAO {

	private JdbcTemplate jdbcTemplate;

	public MysqlAdminDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addAdmin(Admin admin) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		insert.withTableName("admin");
		insert.usingGeneratedKeyColumns("id_admin");
		insert.usingColumns("name", "password", "email");

		Map<String, Object> values = new HashMap<>();
		values.put("name", admin.getName());
		values.put("password", admin.getPassword());
		values.put("email", admin.getEmail());

		admin.setAdminID(insert.executeAndReturnKey(values).longValue());
	}
	
	@Override
	public void deleteEntity(Entity admin) {
		String sql = "DELETE FROM admin WHERE id_admin = " + admin.getEntityID();
		jdbcTemplate.update(sql);
	}

	@Override
	public List<Admin> getAll() {
		String sql = "SELECT id_admin, name, password, email " + "FROM admin";
		return jdbcTemplate.query(sql, new RowMapper<Admin>() {

			@Override
			public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
				Admin admin = new Admin();
				admin.setAdminID(rs.getLong("id_admin"));
				admin.setName(rs.getString("name"));
				admin.setPassword(rs.getString("password"));
				admin.setEmail(rs.getString("email"));
				return admin;
			}
		});
	}

	@Override
	public void saveAdmin(Admin admin) {
		if (admin == null)
			return;
		if (admin.getEntityID() == null) { // CREATE
			addAdmin(admin);
		} else { // UPDATE
			String sql = "UPDATE admin SET " + "name = ?, password = ?, email = ? " + "WHERE id_admin = ?";
			jdbcTemplate.update(sql, admin.getName(), admin.getPassword(), admin.getEmail(), admin.getEntityID());
		}
	}
	
	@Override
	public boolean isNameAvailable(String name) {
		List<Admin> admins = getAll();
		for (Admin admin : admins) {
			if (admin.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}

}
