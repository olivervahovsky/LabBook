package vahovsky.LabBook.fxmodels;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;

public class LaboratoryFxModel {

	private Long laboratoryID;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty location = new SimpleStringProperty();
	private List<Item> items;

	public LaboratoryFxModel(Laboratory laboratory) {
		setLaboratoryID(laboratory.getLaboratoryID());
		setName(laboratory.getName());
		setLocation(laboratory.getLocation());
		if (laboratory.getItems() != null) {
			setItems(laboratory.getItems());
		}
	}
	
	public LaboratoryFxModel() {
	}
	
	public Laboratory getLaboratory() {
		Laboratory l = new Laboratory();
		l.setName(getName());
		l.setLocation(getLocation());
		if (getItems() != null) {
			setItems(getItems());
		}
		l.setLaboratoryID(getLaboratoryID());
		return l;
	}

	public void setLaboratory(Laboratory lab) {
		setLaboratoryID(lab.getLaboratoryID());
		setName(lab.getName());
		setLocation(lab.getLocation());
		if (lab.getItems() != null) {
			setItems(lab.getItems());
		}
	}

	public Long getLaboratoryID() {
		return laboratoryID;
	}

	public void setLaboratoryID(Long laboratoryID) {
		this.laboratoryID = laboratoryID;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getLocation() {
		return location.get();
	}

	public void setLocation(String location) {
		this.location.set(location);
	}

	public StringProperty locationProperty() {
		return location;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
