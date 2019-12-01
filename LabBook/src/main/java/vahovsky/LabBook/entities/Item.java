package vahovsky.LabBook.entities;

import java.util.List;

public class Item implements Entity {

	private Long itemID;
	private String name;
	private int quantity;
	private boolean available;
	private Laboratory laboratory;
	private List<Project> usedInProjects;
	private List<Task> usedInTasks;

	@Override
	public Long getEntityID() {
		return itemID;
	}

	public void setItemID(Long itemID) {
		this.itemID = itemID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public List<Project> getUsedInProjects() {
		return usedInProjects;
	}

	public void setUsedInProjects(List<Project> usedInProjects) {
		this.usedInProjects = usedInProjects;
	}

	public List<Task> getUsedInTasks() {
		return usedInTasks;
	}

	public void setUsedInTasks(List<Task> usedInTasks) {
		this.usedInTasks = usedInTasks;
	}

	public Laboratory getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(Laboratory laboratory) {
		this.laboratory = laboratory;
	}

	@Override
	public String toString() {
		return name;
	}

}
