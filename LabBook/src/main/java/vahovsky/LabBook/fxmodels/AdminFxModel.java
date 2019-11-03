package vahovsky.LabBook.fxmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.Admin;

public class AdminFxModel {

	private Long adminID;

	// the difference between property (e.g. StringProperty name) and value wrapped
	// in this property (e.g. String name) is, that it is possible to add listeners
	// to a property. At the same time, value (String name) is wrapped in this
	// property and is accessible through method get() (String name = name.get() )
	private StringProperty name = new SimpleStringProperty();
	private StringProperty password = new SimpleStringProperty();
	private StringProperty email = new SimpleStringProperty();

	/**
	 * Constructor that creates fxModel object based on parameters of entity object
	 * 
	 * @param admin Entity object that serves as a reference for the fxModel
	 */
	public AdminFxModel(Admin admin) {
		setName(admin.getName());
		setPassword(admin.getPassword());
		setAdminID(admin.getAdminID());
		setEmail(admin.getEmail());
	}

	/**
	 * Method that creates and returns entity object based on parameters
	 * (properties) of fxModel object
	 * 
	 * @return entity object based on parameters (instance variables) of fxModel
	 *         object
	 */
	public Admin getAdmin() {
		Admin admin = new Admin();
		admin.setAdminID(adminID);
		admin.setName(getName());
		admin.setPassword(getPassword());
		admin.setEmail(getEmail());
		return admin;
	}

	public Long getAdminID() {
		return adminID;
	}

	public void setAdminID(Long adminID) {
		this.adminID = adminID;
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

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public StringProperty getPasswordProperty() {
		return password;
	}

	public StringProperty getEmailProperty() {
		return email;
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public String getEmail() {
		return email.get();
	}

}
