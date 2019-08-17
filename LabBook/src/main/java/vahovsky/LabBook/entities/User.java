package vahovsky.LabBook.entities;

import java.util.List;

public class User {

	private Long userID;
	private String name;
	private String password;
	private List<Project> projects;
	private List<Task> tasks;
	private String email;
	
	public User() {
		
	}

	public User(String name, String password, String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
	}
	
	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
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

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override 
	public String toString() {
		return name + "-" + email; 
	}

//	@Override
//	public String toString() {
//		return "User [userID=" + userID + ", name=" + name + ", password=" + password + "]";
//	}

//	@Override
//	public String toString() {
//		return "User [userID=" + userID + ", name=" + name + ", password=" + password + ", projects=" + projects
//				+ ", tasks=" + tasks + ", email=" + email + "]";
//	}

}
