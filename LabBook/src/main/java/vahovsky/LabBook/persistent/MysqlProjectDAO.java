package vahovsky.LabBook.persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import vahovsky.LabBook.entities.Entity;
import vahovsky.LabBook.entities.Project;

public class MysqlProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public MysqlProjectDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addProject(Project project) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		insert.withTableName("project");
		insert.usingGeneratedKeyColumns("id_project");
		insert.usingColumns("name", "active", "date_from", "date_until", "each_item_available", "user_id_user");

		Map<String, Object> values = new HashMap<>();
		values.put("name", project.getName());
		values.put("active", project.isActive());
		values.put("date_from", project.getDateFrom());
		values.put("date_until", project.getDateUntil());
		values.put("each_item_available", project.isEachItemAvailable());
		values.put("user_id_user", project.getCreatedBy().getEntityID());

		project.setProjectID(insert.executeAndReturnKey(values).longValue());
	}

	@Override
	public List<Project> getAll() {
		String sql = "SELECT id_project, name, active, date_from, date_until, each_item_available, user_id_user "
				+ "FROM project";
		return jdbcTemplate.query(sql, new RowMapper<Project>() {

			@Override
			public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
				Project project = new Project();
				project.setProjectID(rs.getLong("id_project"));
				project.setName(rs.getString("name"));
				project.setActive(rs.getBoolean("active"));
				Timestamp timestamp = rs.getTimestamp("date_from");
				project.setDateFrom(timestamp.toLocalDateTime().toLocalDate());
				timestamp = rs.getTimestamp("date_until");
				if (timestamp != null) {
					project.setDateUntil(timestamp.toLocalDateTime().toLocalDate());
				}
				project.setEachItemAvailable(rs.getBoolean("each_item_available"));
				project.setCreatedBy(DAOfactory.INSTANCE.getUserDAO().getByID(rs.getLong("user_id_user")));
				return project;
			}
		});
	}

	@Override
	public void saveProject(Project project) {
		if (project == null)
			return;
		if (project.getEntityID() == null) { // CREATE
			addProject(project);
		} else { // UPDATE
			String sql = "UPDATE project SET " + "name = ?, active = ?, date_from = ?, date_until = ?, "
					+ "each_item_available = ?, user_id_user = ? " + "WHERE id_project = ?";
			jdbcTemplate.update(sql, project.getName(), project.isActive(), project.getDateFrom(),
					project.getDateUntil(), project.isEachItemAvailable(), project.getCreatedBy().getEntityID(),
					project.getEntityID());
		}
	}

	@Override
	public void deleteEntity(Entity project) {
		// First delete all the notes belonging to the project, as they cannot be
		// accessed after project deletion
		jdbcTemplate.update("DELETE FROM note WHERE project_id_project = ?", project.getEntityID());
		// Next delete all the tasks belonging to the project, as they cannot be
		// accessed after project deletion
		jdbcTemplate.update("DELETE FROM task WHERE project_id_project = ?", project.getEntityID());
		// Finally delete the project itself
		String sql = "DELETE FROM project WHERE id_project = " + project.getEntityID();
		jdbcTemplate.update(sql);
	}

	@Override
	public Project getByID(Long id) {
		String sql = "SELECT id_project AS projectID, name, active, date_from, date_until, each_item_available, user_id_user "
				+ "FROM project " + "WHERE id_project = " + id;
		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Project.class));
	}
}
