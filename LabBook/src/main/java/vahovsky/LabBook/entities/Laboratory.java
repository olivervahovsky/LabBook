package vahovsky.LabBook.entities;

import java.util.List;

public class Laboratory implements Entity {

	private Long laboratoryID;
	private String name;
	private String location;
	private List<Item> items;

	public Laboratory() {

	}

	public Laboratory(String name, String location) {
		this.name = name;
		this.location = location;
	}

	@Override
	public Long getEntityID() {
		return laboratoryID;
	}

	public void setLaboratoryID(Long laboratoryID) {
		this.laboratoryID = laboratoryID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return name + "-" + location;
	}

}
