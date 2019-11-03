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

	/**
	 * Constructor that creates fxModel object based on parameters of entity object
	 * 
	 * @param laboratory entity object, which parameters are used to create fxModel
	 *                   object
	 */
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

	/**
	 * Method that sets instance variables of fxModel object based on input entity
	 * object
	 * 
	 * @param laboratory entity object, which parameters are used to set parameters
	 *                   of fxModel object
	 */
	public void setLaboratory(Laboratory laboratory) {
		setLaboratoryID(laboratory.getLaboratoryID());
		setName(laboratory.getName());
		setLocation(laboratory.getLocation());
		if (laboratory.getItems() != null) {
			setItems(laboratory.getItems());
		}
	}

	/**
	 * Method that creates and returns entity object based on parameters (instance
	 * variables) of fxModel object
	 * 
	 * @return entity object based on parameters (instance variables) of fxModel
	 *         object
	 */
	public Laboratory getLaboratory() {
		Laboratory laboratory = new Laboratory();
		laboratory.setName(getName());
		laboratory.setLocation(getLocation());
		if (getItems() != null) {
			setItems(getItems());
		}
		laboratory.setLaboratoryID(getLaboratoryID());
		return laboratory;
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

	public StringProperty getNameProperty() {
		return name;
	}

	public String getLocation() {
		return location.get();
	}

	public void setLocation(String location) {
		this.location.set(location);
	}

	public StringProperty getLocationProperty() {
		return location;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
