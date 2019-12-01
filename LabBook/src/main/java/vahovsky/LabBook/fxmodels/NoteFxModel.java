package vahovsky.LabBook.fxmodels;

import java.time.LocalDateTime;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;

public class NoteFxModel implements EntityFxModel {

	private Long noteID;
	private StringProperty text = new SimpleStringProperty();
	private LocalDateTime timestamp;
	private User author;
	private Task task;
	private Project project;
	private Item item;

	/**
	 * Constructor that creates fxModel object based on parameters of entity object
	 * 
	 * @param note Entity object that serves as a reference for the fxModel
	 */
	public NoteFxModel(Note note) {
		setNoteID(note.getEntityID());
		setText(note.getText());
		setTimestamp(note.getTimestamp());
		setAuthor(note.getAuthor());
		setProject(note.getProject());
		setItem(note.getItem());
		setTask(note.getTask());
	}

	/**
	 * Method that creates and returns entity object based on parameters
	 * (instance variables) of fxModel object
	 * 
	 * @return note entity object based on parameters (instance variables) of
	 *         fxModel object
	 */
	@Override
	public Note getEntity() {
		Note note = new Note();
		note.setNoteID(getNoteID());
		note.setText(getText());
		note.setTimestamp(getTimestamp());
		note.setAuthor(getAuthor());
		note.setProject(getProject());
		note.setItem(getItem());
		note.setTask(getTask());
		return note;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Long getNoteID() {
		return noteID;
	}

	public void setNoteID(Long noteID) {
		this.noteID = noteID;
	}

	public String getText() {
		return text.get();
	}

	public void setText(String text) {
		this.text.set(text);
	}

}
