package vahovsky.LabBook.persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import vahovsky.LabBook.entities.Entity;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.fxmodels.TaskFxModel;

public class MysqlNoteDAO implements NoteDAO {

	private JdbcTemplate jdbcTemplate;

	public MysqlNoteDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addNote(Note note) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		insert.withTableName("note");
		insert.usingGeneratedKeyColumns("id_note");
		insert.usingColumns("text", "timestamp", "user_id_user", "task_id_task", "project_id_project", "item_id_item");

		Map<String, Object> values = new HashMap<>();
		values.put("text", note.getText());
		values.put("timestamp", note.getTimestamp());
		values.put("user_id_user", note.getAuthor().getEntityID());
		if (note.getTask() != null)
			values.put("task_id_task", note.getTask().getEntityID());
		else
			values.put("task_id_task", null);
		if (note.getProject() != null)
			values.put("project_id_project", note.getProject().getEntityID());
		else
			values.put("project_id_project", null);
		if (note.getItem() != null)
			values.put("item_id_item", note.getItem().getEntityID());
		else
			values.put("item_id_item", null);

		note.setNoteID(insert.executeAndReturnKey(values).longValue());
	}

	@Override
	public List<Note> getAll() {
		String sql = "SELECT id_note, text, timestamp, user_id_user, task_id_task, project_id_project, item_id_item "
				+ "FROM note";
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
	public void saveNote(Note note) {
		if (note == null)
			return;
		if (note.getEntityID() == null) { // CREATE
			addNote(note);
		} else { // UPDATE
			String sql = "UPDATE note SET " + "text = ?, timestamp = ?, user_id_user = ?, "
					+ "task_id_task = ?, project_id_project = ?, item_id_item = ? " + "WHERE id_note = ?";

			Long taskID = null;
			if (note.getTask() != null)
				taskID = note.getTask().getEntityID();

			Long projectID = null;
			if (note.getProject() != null)
				projectID = note.getProject().getEntityID();

			Long itemID = null;
			if (note.getItem() != null)
				itemID = note.getItem().getEntityID();

			jdbcTemplate.update(sql, note.getText(), note.getTimestamp(), note.getAuthor().getEntityID(), taskID,
					projectID, itemID, note.getEntityID());
		}
	}

	@Override
	public void deleteEntity(Entity note) {
		String sql = "DELETE FROM note WHERE id_note = " + note.getEntityID();
		jdbcTemplate.update(sql);
	}
	
	@Override
	public List<Note> getNotes(TaskFxModel taskModel) {
		List<Note> notes = new ArrayList<>();
		List<Note> allNotes = getAll();
		for (Note note : allNotes) {
			if (note.getTask() != null)
				if (note.getTask().getEntityID().equals(taskModel.getTaskId())) {
					notes.add(note);
				}
		}
		return notes;
	}

}
