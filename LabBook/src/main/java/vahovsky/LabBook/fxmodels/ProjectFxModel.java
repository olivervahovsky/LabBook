package vahovsky.LabBook.fxmodels;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.User;

public class ProjectFxModel implements EntityFxModel {

	private Long projectId;
	private StringProperty name = new SimpleStringProperty();
	private ObjectProperty<LocalDate> from = new SimpleObjectProperty<>();
	private ObjectProperty<LocalDate> until = new SimpleObjectProperty<>();
	private User createdBy;
	private List<User> completedBy;

	/**
	 * Constructor that creates fxModel object based on parameters of entity object
	 * 
	 * @param project Entity object that serves as a reference for the fxModel
	 */
	public ProjectFxModel(Project project) {
		setName(project.getName());
		setFrom(project.getDateFrom());
		setUntil(project.getDateUntil());
		setCompletedBy(project.getCompletedBy());
		setProjectId(project.getEntityID());
	}

	/**
	 * Method that sets instance variables of fxModel object based on input entity
	 * object
	 * 
	 * @param project entity object, which parameters are used to set
	 *                parameters of fxModel object
	 */
	public void setProject(Project project) {
		setName(project.getName());
		setFrom(project.getDateFrom());
		setUntil(project.getDateUntil());
		setCreatedBy(project.getCreatedBy());
		setCompletedBy(project.getCompletedBy());
	}

	/**
	 * Method that creates and returns entity object based on parameters (instance
	 * variables) of fxModel object
	 * 
	 * @return entity object based on parameters (instance variables) of fxModel
	 *         object
	 */
	@Override
	public Project getEntity() {
		Project project = new Project();
		project.setName(getName());
		project.setDateFrom(getFrom());
		project.setDateUntil(getUntil());
		project.setCreatedBy(getCreatedBy());
		project.setCompletedBy(getCompletedBy());
		project.setProjectID(getProjectId());
		return project;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty getNameProperty() {
		return name;
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

}
