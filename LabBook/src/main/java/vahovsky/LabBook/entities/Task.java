package vahovsky.LabBook.entities;

import java.time.LocalDate;
import java.util.List;

public class Task {

	private Long taskID;
	private String name;
	private boolean active;
	private LocalDate dateTimeFrom;
	private LocalDate dateTimeUntil;
	private boolean eachItemAvailable;
	private List<Item> items;
	private Laboratory laboratory;
	private User createdBy;
	private List<User> completedBy;
	private Project project;

	public Task() {

	}

	public Task(String name, LocalDate dateTimeFrom, LocalDate dateTimeUntil, Project project) {
		this.name = name;
		this.dateTimeFrom = dateTimeFrom;
		this.dateTimeUntil = dateTimeUntil;
		this.project = project;
	}

	public Long getTaskID() {
		return taskID;
	}

	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDate getDateTimeFrom() {
		return dateTimeFrom;
	}

	public void setDateTimeFrom(LocalDate dateTimeFrom) {
		this.dateTimeFrom = dateTimeFrom;
	}

	public LocalDate getDateTimeUntil() {
		return dateTimeUntil;
	}

	public void setDateTimeUntil(LocalDate dateTimeUntil) {
		this.dateTimeUntil = dateTimeUntil;
	}

	public boolean isEachItemAvailable() {
		return eachItemAvailable;
	}

	public void setEachItemAvailable(boolean eachItemAvailable) {
		this.eachItemAvailable = eachItemAvailable;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public List<User> getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(List<User> completedBy) {
		this.completedBy = completedBy;
	}

	public Laboratory getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(Laboratory laboratory) {
		this.laboratory = laboratory;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "Task [taskID=" + taskID + ", projectID=" + project.getProjectID() + ", name=" + name + "]";
	}

}
