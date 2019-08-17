package vahovsky.LabBook.fxmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.User;

public class UserFxModel {

	private Long userID;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty password = new SimpleStringProperty();
	private StringProperty email = new SimpleStringProperty();

	public UserFxModel() {

	}

	public UserFxModel(User user) {
		setName(user.getName());
		setPassword(user.getPassword());
		//System.out.println(user.getUserID());
		setUserID(user.getUserID());
		setEmail(user.getEmail());
	}

	public void setUser(User user) {
		setName(user.getName());
		setPassword(user.getPassword());
		setUserID(user.getUserID());
	}

	public User getUser() {
		User user = new User();
		user.setUserID(getUserID());
		user.setName(getName());
		user.setPassword(getPassword());
		user.setEmail(getEmail());
		return user;
	}

	public StringProperty EmailProperty() {
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

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getName() {
		return name.get();
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public String getPassword() {
		return password.get();
	}

}
