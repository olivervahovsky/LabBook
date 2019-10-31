package vahovsky.LabBook.fxmodels;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.User;

public class ProjectFxModel {

	private Long projectId;
	private StringProperty name = new SimpleStringProperty();
	private ObjectProperty<LocalDate> from = new SimpleObjectProperty<>();
	private ObjectProperty<LocalDate> until = new SimpleObjectProperty<>();
	private User createdBy;
	private List<User> completedBy;

	// public ProjectFxModel() {
	//
	// }

	public ProjectFxModel(Project project) {
		// this.project = project;
		setName(project.getName());
		setFrom(project.getDateFrom());
		setUntil(project.getDateUntil());
		setCompletedBy(project.getCompletedBy());
		setProjectId(project.getProjectID());
	}

	public void setProject(Project project) {
		setName(project.getName());
		setFrom(project.getDateFrom());
		setUntil(project.getDateUntil());
		setCreatedBy(project.getCreatedBy());
		setCompletedBy(project.getCompletedBy());
	}

	public Project getProject() {
		Project p = new Project();
		p.setName(getName());
		p.setDateFrom(getFrom());
		p.setDateUntil(getUntil());
		p.setCreatedBy(getCreatedBy());
		p.setCompletedBy(getCompletedBy());
		p.setProjectID(getProjectId());
		return p;
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

	public ObjectProperty<LocalDate> fromProperty() {
		return from;
	}

	public LocalDate getUntil() {
		return until.get();
	}

	public void setUntil(LocalDate until) {
		this.until.set(until);
	}

	public ObjectProperty<LocalDate> untilProperty() {
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
