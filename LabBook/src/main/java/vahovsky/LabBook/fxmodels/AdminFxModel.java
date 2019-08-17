package vahovsky.LabBook.fxmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import vahovsky.LabBook.entities.Admin;

public class AdminFxModel {

	private Long adminID;
	// na property vieme dat listenery, treba rozlisovat medzi vracanim property a
	// vracanim value wrapped v property = property.get()
	private StringProperty name = new SimpleStringProperty();
	private StringProperty password = new SimpleStringProperty();
	private StringProperty email = new SimpleStringProperty();
	//private Admin admin;

	public AdminFxModel(Admin admin) {
		//this.admin = admin;
		setName(admin.getName());
		setPassword(admin.getPassword());
		setAdminID(admin.getAdminID());
		setEmail(admin.getEmail());
	}

	public Admin getAdmin() {
		Admin a = new Admin();
		a.setAdminID(adminID);
		a.setName(getName());
		a.setPassword(getPassword());
		a.setEmail(getEmail());
		return a;
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

	public StringProperty nameProperty() {
		return name;
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public StringProperty passwordProperty() {
		return password;
	}

}
