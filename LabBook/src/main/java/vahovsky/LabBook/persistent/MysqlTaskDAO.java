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

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Task;

public class MysqlTaskDAO implements TaskDAO {

	private JdbcTemplate jdbcTemplate;

	public MysqlTaskDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private void insertItems(Task task) {
		jdbcTemplate.update("DELETE FROM task_has_item WHERE task_id_task = ?", task.getTaskID());
		if (task.getItems() != null)
			if (task.getItems().size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("INSERT INTO task_has_item (task_id_task, item_id_item)");
				sb.append(" VALUES ");
				for (int i = 0; i < task.getItems().size(); i++) {
					sb.append("(" + task.getTaskID() + "," + task.getItems().get(i).getItemID() + "),");
				}
				String insertSql = sb.substring(0, sb.length() - 1);
				jdbcTemplate.update(insertSql);
			}
	}

	@Override
	public void addTask(Task task) {

		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		insert.withTableName("task");
		insert.usingGeneratedKeyColumns("id_task");
		insert.usingColumns("project_id_project", "name", "active", "date_time_from", "date_time_until",
				"each_item_available", "user_id_user", "laboratory_id_laboratory");

		Map<String, Object> values = new HashMap<>();
		values.put("project_id_project", task.getProject().getProjectID());
		values.put("name", task.getName());
		values.put("active", task.isActive());
		values.put("date_time_from", task.getDateTimeFrom());
		values.put("date_time_until", task.getDateTimeUntil());
		values.put("each_item_available", task.isEachItemAvailable());
		values.put("user_id_user", task.getCreatedBy().getUserID());
		if (task.getLaboratory() != null) {
			values.put("laboratory_id_laboratory", task.getLaboratory().getLaboratoryID());
		}

		task.setTaskID(insert.executeAndReturnKey(values).longValue());
		insertItems(task);
	}

	@Override
	public void saveTask(Task task) {
		if (task == null)
			return;
		if (task.getTaskID() == null) { // CREATE
			addTask(task);
		} else { // UPDATE
			String sql = "UPDATE task SET "
					+ "project_id_project = ?, name = ?, active = ?, date_time_from = ?, date_time_until = ?, "
					+ "each_item_available = ?, user_id_user = ?, laboratory_id_laboratory = ? " + "WHERE id_task = ?";
			Long laboratoryID = null;
			if (task.getLaboratory() != null) {
				laboratoryID = task.getLaboratory().getLaboratoryID();
			}
			jdbcTemplate.update(sql, task.getProject().getProjectID(), task.getName(), task.isActive(),
					task.getDateTimeFrom(), task.getDateTimeUntil(), task.isEachItemAvailable(),
					task.getCreatedBy().getUserID(), laboratoryID, task.getTaskID());
			insertItems(task);
		}
	}

	@Override
	public List<Task> getAll() {
		String sql = "SELECT id_task, project_id_project, name, active, date_time_from, date_time_until,"
				+ " each_item_available, user_id_user, laboratory_id_laboratory " + "FROM task";
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
			sql = "SELECT item_id_item FROM task_has_item WHERE task_id_task =" + task.getTaskID();
			List<Item> items = jdbcTemplate.query(sql, new RowMapper<Item>() {

				@Override
				public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
					Item item = new Item();
					item = DAOfactory.INSTANCE.getItemDAO().getByID(rs.getLong("item_id_item"));
					return item;
				}
			});
			task.setItems(items);
		}
		return tasks;
	}

	@Override
	public void deleteTask(Task task) {
		jdbcTemplate.update("DELETE FROM task_has_item WHERE task_id_task= ?", task.getTaskID());
		String sql = "DELETE FROM task WHERE id_task = " + task.getTaskID();
		jdbcTemplate.update(sql);
	}

	@Override
	public Task getByID(Long id) {
		String sql = "SELECT id_task AS taskID, project_id_project, name, active,"
				+ " date_time_from, date_time_until, each_item_available, user_id_user, laboratory_id_laboratory "
				+ "FROM task " + "WHERE id_task = " + id;
		Task task = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Task.class));
		sql = "SELECT item_id_item FROM task_has_item WHERE task_id_task =" + task.getTaskID();
		List<Item> items = jdbcTemplate.query(sql, new RowMapper<Item>() {

			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				Item item = new Item();
				item = DAOfactory.INSTANCE.getItemDAO().getByID(rs.getLong("item_id_item"));
				return item;
			}
		});
		task.setItems(items);
		return task;
	}

}
//=======
//package vahovsky.LabBook.persistent;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.ResultSetExtractor;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//
//import vahovsky.LabBook.entities.Item;
//import vahovsky.LabBook.entities.Laboratory;
//import vahovsky.LabBook.entities.Project;
//import vahovsky.LabBook.entities.Task;
//import vahovsky.LabBook.entities.User;
//
//public class MysqlTaskDAO implements TaskDAO {
//
//	private JdbcTemplate jdbcTemplate;
//
//	public MysqlTaskDAO(JdbcTemplate jdbcTemplate) {
//		this.jdbcTemplate = jdbcTemplate;
//	}
//
//	@Override
//	public void addTask(Task task) {
//
//		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
//		insert.withTableName("task");
//		insert.usingGeneratedKeyColumns("id_task");
//		insert.usingColumns("project_id_project", "name", "active", "date_time_from", "date_time_until",
//				"each_item_available", "user_id_user", "laboratory_id_laboratory");
//
//		Map<String, Object> values = new HashMap<>();
//		values.put("project_id_project", task.getProject().getProjectID());
//		values.put("name", task.getName());
//		values.put("active", task.isActive());
//		values.put("date_time_from", task.getDateTimeFrom());
//		values.put("date_time_until", task.getDateTimeUntil());
//		values.put("each_item_available", task.isEachItemAvailable());
//		values.put("user_id_user", task.getCreatedBy().getUserID());
//		if (task.getLaboratory() != null) {
//			values.put("laboratory_id_laboratory", task.getLaboratory().getLaboratoryID());
//		}
//
//		task.setTaskID(insert.executeAndReturnKey(values).longValue());
//	}
//
//	@Override
//	public void saveTask(Task task) {
//		if (task == null)
//			return;
//		if (task.getTaskID() == null) { // CREATE
//			addTask(task);
//		} else { // UPDATE
//			String sql = "UPDATE task SET "
//					+ "project_id_project = ?, name = ?, active = ?, date_time_from = ?, date_time_until = ?, "
//					+ "each_item_available = ?, user_id_user = ?, laboratory_id_laboratory = ? " + "WHERE id_task = ?";
//			Long laboratoryID = null;
//			if (task.getLaboratory() != null) {
//				laboratoryID = task.getLaboratory().getLaboratoryID();
//			}
//			jdbcTemplate.update(sql, task.getProject().getProjectID(), task.getName(), task.isActive(),
//					task.getDateTimeFrom(), task.getDateTimeUntil(), task.isEachItemAvailable(),
//					task.getCreatedBy().getUserID(), laboratoryID, task.getTaskID());
//		}
//	}
//
//	@Override
//	public List<Task> getAll() {
//		String sql = "SELECT id_task, project_id_project, name, active, date_time_from, date_time_until,"
//				+ " each_item_available, user_id_user, laboratory_id_laboratory " + "FROM task";
//		List<Task> tasks = jdbcTemplate.query(sql, new RowMapper<Task>() {
//
//			@Override
//			public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Task task = new Task();
//				task.setTaskID(rs.getLong("id_task"));
//				task.setProject(DAOfactory.INSTANCE.getProjectDAO().getByID(rs.getLong("project_id_project")));
//				task.setName(rs.getString("name"));
//				task.setActive(rs.getBoolean("active"));
//				Timestamp timestamp = rs.getTimestamp("date_time_from");
//				if (timestamp != null) {
//					task.setDateTimeFrom(timestamp.toLocalDateTime().toLocalDate());
//				}
//				timestamp = rs.getTimestamp("date_time_until");
//				if (timestamp != null) {
//					task.setDateTimeUntil(timestamp.toLocalDateTime().toLocalDate());
//				}
//				task.setEachItemAvailable(rs.getBoolean("each_item_available"));
//				task.setCreatedBy(DAOfactory.INSTANCE.getUserDAO().getByID(rs.getLong("user_id_user")));
//				if (task.getLaboratory() != null)
//					task.setLaboratory(DAOfactory.INSTANCE.getLaboratoryDAO()
//							.getLaboratoryByID(rs.getLong("laboratory_id_laboratory")));
//				return task;
//			}
//		});
//		for (Task task : tasks) {
//			sql = "SELECT item_id_item FROM task_has_item WHERE task_id_task =" + task.getTaskID();
//			List<Item> items = jdbcTemplate.query(sql, new RowMapper<Item>() {
//
//				@Override
//				public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
//					Item item = new Item();
//					item = DAOfactory.INSTANCE.getItemDAO().getByID(rs.getLong("item_id_item"));
//					return item;
//				}
//			});
//			task.setItems(items);
//		}
//		return tasks;
//	}
//
//	@Override
//	public void deleteTask(Task task) {
//		String sql = "DELETE FROM task WHERE id_task = " + task.getTaskID();
//		jdbcTemplate.update(sql);
//	}
//
//	@Override
//	public Task getByID(Long id) {
//		String sql = "SELECT id_task AS taskID, project_id_project, name, active,"
//				+ " date_time_from, date_time_until, each_item_available, user_id_user, laboratory_id_laboratory "
//				+ "FROM task " + "WHERE id_task = " + id;
//		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Task.class));
//	}
//
//}
//>>>>>>> branch 'zakladne_gui_+_dao' of https://github.com/vistanovalaura/LabBook.git
