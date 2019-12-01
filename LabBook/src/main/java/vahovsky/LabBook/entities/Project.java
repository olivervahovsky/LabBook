package vahovsky.LabBook.entities;

import java.time.LocalDate;
import java.util.List;

public class Project implements Entity {

	private Long projectID;
	private String name;
	private boolean active;
	private LocalDate dateFrom;
	private LocalDate dateUntil;
	private User createdBy;
	private List<Task> tasks;
	private List<Item> items;
	private boolean eachItemAvailable;
	private List<User> completedBy;

	public Project() {

	}

	public Project(String name, LocalDate dateFrom, LocalDate dateUntil, boolean active) {
		super();
		this.name = name;
		this.dateFrom = dateFrom;
		this.dateUntil = dateUntil;
		this.active = true;
	}

	@Override
	public Long getEntityID() {
		return projectID;
	}

	public void setProjectID(Long projectID) {
		this.projectID = projectID;
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

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate from) {
		this.dateFrom = from;
	}

	public LocalDate getDateUntil() {
		return dateUntil;
	}

	public void setDateUntil(LocalDate until) {
		this.dateUntil = until;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public boolean isEachItemAvailable() {
		return eachItemAvailable;
	}

	public void setEachItemAvailable(boolean eachItemAvailable) {
		this.eachItemAvailable = eachItemAvailable;
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

	@Override
	public String toString() {
		return "Project [projectID=" + projectID + ", name=" + name + "]";
	}

}
