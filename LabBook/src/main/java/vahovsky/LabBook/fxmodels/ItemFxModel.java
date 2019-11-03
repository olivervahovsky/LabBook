package vahovsky.LabBook.fxmodels;

import vahovsky.LabBook.entities.Item;

public class ItemFxModel {

	private Long itemID;
	private String name;
	private int quantity;

	/**
	 * Constructor that creates fxModel object based on parameters of entity object
	 * 
	 * @param item Entity object that serves as a reference for the fxModel
	 */
	public ItemFxModel(Item item) {
		setName(item.getName());
		setItemID(item.getItemID());
		setQuantity(item.getQuantity());
	}

	public ItemFxModel() {
	}

	/**
	 * Method that sets instance variables of fxModel object based on input entity
	 * object
	 * 
	 * @param item entity object, which parameters are used to set parameters of
	 *             fxModel object
	 */
	public void setItem(Item item) {
		setName(item.getName());
		setItemID(item.getItemID());
		item.setQuantity(item.getQuantity());
	}

	/**
	 * Method that creates and returns entity object based on parameters (instance
	 * variables) of fxModel object
	 * 
	 * @return entity object based on parameters (instance variables) of fxModel
	 *         object
	 */
	public Item getItem() {
		Item item = new Item();
		item.setName(getName());
		item.setItemID(getItemID());
		item.setQuantity(getQuantity());
		return item;
	}

	public Long getItemID() {
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

}
