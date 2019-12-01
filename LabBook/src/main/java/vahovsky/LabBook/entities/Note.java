package vahovsky.LabBook.entities;

import java.time.LocalDateTime;

public class Note implements Entity {

	private Long noteID;
	private String text;
	private LocalDateTime timestamp;
	private User author;
	private Task task;
	private Project project;
	private Item item;

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

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public Long getEntityID() {
		return noteID;
	}

	public void setNoteID(Long noteID) {
		this.noteID = noteID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime time) {
		this.timestamp = time;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "Note [noteID=" + noteID + ", text=" + text + ", timestamp=" + timestamp + ", author=" + author
				+ ", task=" + task + ", project=" + project + ", item=" + item + "]";
	}

}
