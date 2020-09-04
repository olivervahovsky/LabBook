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
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;

public class MysqlUserDAO implements UserDAO {

	private JdbcTemplate jdbcTemplate;

	public MysqlUserDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<User> getAll() {
		String sql = "SELECT id_user, name, password, email " + "FROM lab_book.user";
		return jdbcTemplate.query(sql, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setUserID(rs.getLong("id_user"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				return user;
			}
		});
	}

	@Override
	public void addUser(User user) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		insert.withTableName("user");
		insert.usingGeneratedKeyColumns("id_user");
		insert.usingColumns("name", "password", "email");

		Map<String, Object> values = new HashMap<>();
		values.put("name", user.getName());
		values.put("password", user.getPassword());
		values.put("email", user.getEmail());

		user.setUserID(insert.executeAndReturnKey(values).longValue());
	}
	
	@Override
	public void deleteEntity(Entity user) {
		// First delete all the notes belonging to the user, as they cannot be
		// accessed after user deletion
		jdbcTemplate.update("DELETE FROM note WHERE user_id_user = ?", user.getEntityID());
		// Next delete all the tasks belonging to the user, as they cannot be
		// accessed after user deletion. Here, deleting rows from the tasks table is
		// not enough, as tasks are foreign keys elsewhere, hence we delete tasks by
		// calling the method deleteTask which updates all the references to these
		// tasks in other tables of the database.
		List<Task> tasks = DAOfactory.INSTANCE.getUserDAO().getTasksOfUser(user);
		if (tasks != null) {
			for (Task task : tasks) {
				DAOfactory.INSTANCE.getTaskDAO().deleteEntity(task);
			}
		}
		// Next delete all the projects belonging to the user, as they cannot be
		// accessed after user deletion.
		jdbcTemplate.update("DELETE FROM project WHERE user_id_user = ?", user.getEntityID());
		// Finally delete the user himself
		String sql = "DELETE FROM user WHERE id_user = " + user.getEntityID();
		jdbcTemplate.update(sql);
	}

	@Override
	public void saveUser(User user) {
		if (user == null)
			return;
		if (user.getEntityID() == null) { // CREATE
			addUser(user);
		} else { // UPDATE
			String sql = "UPDATE user SET " + "name = ?, password = ?, email = ? " + "WHERE id_user = ?";
			jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getEmail(), user.getEntityID());
		}
	}

	@Override
	public User getByID(Long id) {
		String sql = "SELECT id_user AS userID, name, password, email " + "FROM lab_book.user " + "WHERE id_user = "
				+ id;
		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class));
	}

	@Override
	public List<Task> getTasksOfUser(Entity user) {
		String sql = "SELECT id_task, project_id_project, name, active,"
				+ " date_time_from, date_time_until, each_item_available, user_id_user, laboratory_id_laboratory "
				+ "FROM task " + "WHERE user_id_user = " + user.getEntityID();
		List<Task> tasks = jdbcTemplate.query(sql, new RowMapper<Task>() {

			@Override
			public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
				Task task = new Task();
				task.setTaskID(rs.getLong("id_task"));
				task.setProject(DAOfactory.INSTANCE.getProjectDAO().getByID(rs.getLong("project_id_project")));
				task.setName(rs.getString("name"));
				task.setActive(rs.getBoolean("active"));
				Timestamp timestamp = rs.getTimestamp("date_time_from");
				if (timestamp != null) {
					task.setDateTimeFrom(timestamp.toLocalDateTime().toLocalDate());
				}
				timestamp = rs.getTimestamp("date_time_until");
				if (timestamp != null) {
					task.setDateTimeUntil(timestamp.toLocalDateTime().toLocalDate());
				}
				task.setEachItemAvailable(rs.getBoolean("each_item_available"));
				task.setCreatedBy(DAOfactory.INSTANCE.getUserDAO().getByID(rs.getLong("user_id_user")));
				if (task.getLaboratory() != null)
					task.setLaboratory(DAOfactory.INSTANCE.getLaboratoryDAO()
							.getLaboratoryByID(rs.getLong("laboratory_id_laboratory")));
				return task;
			}
		});
		for (Task task : tasks) {
			sql = "SELECT item_id_item FROM task_has_item WHERE task_id_task =" + task.getEntityID();
			List<Item> items = jdbcTemplate.query(sql, new RowMapper<Item>() {

				@Override
				public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
					return DAOfactory.INSTANCE.getItemDAO().getByID(rs.getLong("item_id_item"));
				}
			});
			task.setItems(items);
		}
		return tasks;
	}

	@Override
	public List<Note> getNotes(User user) {
		String sql = "SELECT id_note, text, timestamp, user_id_user, task_id_task, project_id_project, item_id_item "
				+ "FROM note " + "WHERE user_id_user = " + user.getEntityID();
		return jdbcTemplate.query(sql, new RowMapper<Note>() {

			@Override
			public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
				Note note = new Note();
				note.setNoteID(rs.getLong("id_note"));
				note.setText(rs.getString("text"));
				note.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
				note.setAuthor(DAOfactory.INSTANCE.getUserDAO().getByID(rs.getLong("user_id_user")));
				
				if (rs.getObject("task_id_task") != null)
					note.setTask(DAOfactory.INSTANCE.getTaskDAO().getByID(rs.getLong("task_id_task")));
				else
					note.setTask(null);

				if (rs.getObject("project_id_project") != null)
					note.setProject(DAOfactory.INSTANCE.getProjectDAO().getByID(rs.getLong("project_id_project")));
				else
					note.setProject(null);

				if (rs.getObject("item_id_item") != null)
					note.setItem(DAOfactory.INSTANCE.getItemDAO().getByID(rs.getLong("item_id_item")));
				else
					note.setItem(null);
				return note;
			}
		});
	}

	@Override
	public List<Project> getProjects(User user) {
		String sql = "SELECT id_project, name, active, date_from, date_until, each_item_available, user_id_user "
				+ "FROM project " + "WHERE user_id_user = " + user.getEntityID();
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
	public User getByEmail(String email) {
		String sql = "SELECT id_user AS userID, name, password, email " + "FROM lab_book.user " + "WHERE email = '"
				+ email + "'";
		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class));
	}

	@Override
	public List<String> getAllEmails() {
		String sql = "SELECT email " + "FROM lab_book.user";
		return jdbcTemplate.query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String email = rs.getString("email");
				return email;
			}
		});
	}

	@Override
	public List<String> getAllNames() {
		String sql = "SELECT name " + "FROM lab_book.user";
		return jdbcTemplate.query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String name = rs.getString("name");
				return name;
			}
		});
	}

}
