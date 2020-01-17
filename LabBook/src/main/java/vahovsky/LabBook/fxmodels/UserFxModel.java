package vahovsky.LabBook.fxmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.User;

public class UserFxModel implements EntityFxModel {

	private Long userID;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty password = new SimpleStringProperty();
	private StringProperty email = new SimpleStringProperty();

	public UserFxModel() {

	}

	/**
	 * Constructor that creates fxModel object based on parameters of entity object
	 * 
	 * @param user Entity object that serves as a reference for the fxModel
	 */
	public UserFxModel(User user) {
		setName(user.getName());
		setPassword(user.getPassword());
		setUserID(user.getEntityID());
		setEmail(user.getEmail());
	}

	/**
	 * Method that sets instance variables of fxModel object based on input entity
	 * object
	 * 
	 * @param user entity object, which parameters are used to set parameters of
	 *             fxModel object
	 */
	public void setByUser(User user) {
		setName(user.getName());
		setPassword(user.getPassword());
		setUserID(user.getEntityID());
	}

	/**
	 * Method that creates and returns entity object based on parameters (instance
	 * variables) of fxModel object
	 * 
	 * @return entity object based on parameters (instance variables) of fxModel
	 *         object
	 */
	@Override
	public User getEntity() {
		User user = new User();
		user.setUserID(getUserID());
		user.setName(getName());
		user.setPassword(getPassword());
		user.setEmail(getEmail());
		return user;
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

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
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

	public StringProperty getPasswordProperty() {
		return password;
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public String getPassword() {
		return password.get();
	}

}
