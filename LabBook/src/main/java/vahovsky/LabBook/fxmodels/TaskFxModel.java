package vahovsky.LabBook.fxmodels;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;

public class TaskFxModel implements EntityFxModel {

	private Long taskId;
	private StringProperty name = new SimpleStringProperty();
	private Laboratory laboratory = new Laboratory();
	private ObjectProperty<LocalDate> from = new SimpleObjectProperty<>();
	private ObjectProperty<LocalDate> until = new SimpleObjectProperty<>();
	private List<Item> items;
	private Project project;
	private User createdBy;

	public TaskFxModel() {
	}

	/**
	 * Constructor that creates fxModel object based on parameters of entity object
	 * 
	 * @param task Entity object that serves as a reference for the fxModel
	 */
	public TaskFxModel(Task task) {
		setProject(task.getProject());
		setName(task.getName());
		setFrom(task.getDateTimeFrom());
		setUntil(task.getDateTimeUntil());
		setTaskId(task.getEntityID());
		setItems(task.getItems());
		setLaboratory(task.getLaboratory());
		setCreatedBy(task.getCreatedBy());
	}

	/**
	 * Method that sets instance variables of fxModel object based on input entity
	 * object
	 * 
	 * @param task entity object, which parameters are used to set parameters of
	 *             fxModel object
	 */
	public void setTask(Task task) {
		setProject(task.getProject());
		setName(task.getName());
		setFrom(task.getDateTimeFrom());
		setUntil(task.getDateTimeUntil());
		setItems(task.getItems());
		if (task.getLaboratory() != null) {
			setLaboratory(task.getLaboratory());
		}
		setCreatedBy(task.getCreatedBy());
	}

	/**
	 * Method that creates and returns entity object based on parameters (instance
	 * variables) of fxModel object
	 * 
	 * @return entity object based on parameters (instance variables) of fxModel
	 *         object
	 */
	@Override
	public Task getEntity() {
		Task t = new Task();
		t.setProject(getProject());
		t.setName(getName());
		t.setDateTimeFrom(getFrom());
		t.setDateTimeUntil(getUntil());
		t.setTaskID(getTaskId());
		t.setLaboratory(getLaboratory());
		t.setCreatedBy(getCreatedBy());
		return t;
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

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getName() {
		return name.get();
	}

	public LocalDate getFrom() {
		return from.get();
	}

	public void setFrom(LocalDate from) {
		this.from.set(from);
	}

	public ObjectProperty<LocalDate> getFromProperty() {
		return from;
	}

	public LocalDate getUntil() {
		return until.get();
	}

	public void setUntil(LocalDate until) {
		this.until.set(until);
	}

	public ObjectProperty<LocalDate> getUntilProperty() {
		return until;
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

}
