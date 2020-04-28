package vahovsky.LabBook.persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import vahovsky.LabBook.entities.Task;

public class MysqlTaskDAO implements TaskDAO {

	private JdbcTemplate jdbcTemplate;

	public MysqlTaskDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Method that creates reference between task and items used in that task. For
	 * this purpose, rows are inserted into the reference table task_has_item, as
	 * there exists a two - way relationship between tasks and items (more items may
	 * belong to a single task as well as single item may be used in multiple
	 * tasks). If there are rows already referencing this task in the table
	 * task_has_item , these are deleted before inserting new rows.
	 * 
	 * @param task Task, whose reference to items we are updating in the table
	 *             task_has_item
	 */
	private void insertItems(Task task) {
		jdbcTemplate.update("DELETE FROM task_has_item WHERE task_id_task = ?", task.getEntityID());
		if (task.getItems() != null)
			if (task.getItems().size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("INSERT INTO task_has_item (task_id_task, item_id_item)");
				sb.append(" VALUES ");
				for (int i = 0; i < task.getItems().size(); i++) {
					sb.append("(" + task.getEntityID() + "," + task.getItems().get(i).getEntityID() + "),");
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
		values.put("project_id_project", task.getProject().getEntityID());
		values.put("name", task.getName());
		values.put("active", task.isActive());
		values.put("date_time_from", task.getDateTimeFrom());
		values.put("date_time_until", task.getDateTimeUntil());
		values.put("each_item_available", task.isEachItemAvailable());
		values.put("user_id_user", task.getCreatedBy().getEntityID());
		if (task.getLaboratory() != null) {
			values.put("laboratory_id_laboratory", task.getLaboratory().getEntityID());
		}

		task.setTaskID(insert.executeAndReturnKey(values).longValue());
		insertItems(task);
	}

	@Override
	public void saveTask(Task task) {
		if (task == null)
			return;
		if (task.getEntityID() == null) { // CREATE
			addTask(task);
		} else { // UPDATE
			String sql = "UPDATE task SET "
					+ "project_id_project = ?, name = ?, active = ?, date_time_from = ?, date_time_until = ?, "
					+ "each_item_available = ?, user_id_user = ?, laboratory_id_laboratory = ? " + "WHERE id_task = ?";
			Long laboratoryID = null;
			if (task.getLaboratory() != null) {
				laboratoryID = task.getLaboratory().getEntityID();
			}
			jdbcTemplate.update(sql, task.getProject().getEntityID(), task.getName(), task.isActive(),
					task.getDateTimeFrom(), task.getDateTimeUntil(), task.isEachItemAvailable(),
					task.getCreatedBy().getEntityID(), laboratoryID, task.getEntityID());
			insertItems(task);
		}
	}

	@Override
	public List<Item> getItems(Task task) {
		String sql = "SELECT item_id_item FROM task_has_item WHERE task_id_task =" + task.getEntityID();
		List<Item> items = jdbcTemplate.query(sql, new RowMapper<Item>() {

			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return DAOfactory.INSTANCE.getItemDAO().getByID(rs.getLong("item_id_item"));
			}
		});
		return items;
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
			MysqlTaskDAO mtd = new MysqlTaskDAO(jdbcTemplate);
			List<Item> items = mtd.getItems(task);
			task.setItems(items);
		}
		return tasks;
	}

	@Override
	public void deleteEntity(Entity task) {
		// First delete all the notes belonging to the task, as they cannot be
		// accessed after task deletion
		jdbcTemplate.update("DELETE FROM note WHERE task_id_task = ?", task.getEntityID());
		// Next delete all the rows of the table task_has_item corresponding to the
		// task to be deleted
		jdbcTemplate.update("DELETE FROM task_has_item WHERE task_id_task= ?", task.getEntityID());
		// Finally delete the task itself
		String sql = "DELETE FROM task WHERE id_task = " + task.getEntityID();
		jdbcTemplate.update(sql);
	}

	@Override
	public Task getByID(Long id) {
		String sql = "SELECT id_task AS taskID, project_id_project, name, active,"
				+ " date_time_from, date_time_until, each_item_available, user_id_user, laboratory_id_laboratory "
				+ "FROM task " + "WHERE id_task = " + id;
		Task task = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Task.class));
		MysqlTaskDAO mtd = new MysqlTaskDAO(jdbcTemplate);
		List<Item> items = mtd.getItems(task);
		task.setItems(items);
		return task;
	}

	@Override
	public boolean isNameAvailable(String name) {
		List<Task> tasks = getAll();
		for (Task task : tasks) {
			if (task.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public List<Note> getNotes(Task task) {
		List<Note> notes = new ArrayList<>();
		List<Note> allNotes = DAOfactory.INSTANCE.getNoteDAO().getAll();
		for (Note note : allNotes) {
			if (note.getTask() != null) {
				if (note.getTask().getEntityID() == task.getEntityID()) {
					notes.add(note);
				}
			}
		}
		return notes;
	}

}