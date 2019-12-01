package vahovsky.LabBook.entities;

public class Admin implements Entity {

	private Long adminID;
	private String name;
	private String password;
	private String email;

	public Admin() {
	}

	public Admin(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Long getEntityID() {
		return adminID;
	}

	public void setAdminID(Long adminID) {
		this.adminID = adminID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Admin [adminID=" + adminID + ", name=" + name + ", password=" + password + "]";
	}

}
