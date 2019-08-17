package vahovsky.LabBook.business;

import java.util.List;

import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;

public class UserIdentificationManager {

	private static int typeOfUser;
	private static Long id;

	public static int getTypeOfUser() {
		return typeOfUser;
	}

	public static Long getId() {
		return id;
	}

	public static User getUser() {
		List<User> users = DAOfactory.INSTANCE.getUserDAO().getAll();
		for (User u : users) {
			if (u.getUserID().equals(id)) {
				return u;
			}
		}
		return null;
	}

	public static Admin getAdmin() {
		List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
		for (Admin a : admins) {
			if (a.getAdminID().equals(id)) {
				return a;
			}
		}
		return null;
	}

	// nastavi id a typ usera + podla toho aka hodnota sa vrati sa vyberie dalsie
	// okno (pre admina, pre usera alebo wrongData)
	public static int setUser(String userName, String password) {
		PasswordManager pm = new PasswordManager();

		List<User> users = DAOfactory.INSTANCE.getUserDAO().getAll();
		for (User user : users) {
			if (user.getName().equals(userName) && pm.isCorrectPassword(password, user.getUserID(), 1)) {
				UserIdentificationManager.typeOfUser = 1;
				UserIdentificationManager.id = user.getUserID();
				return 1;
			}
		}
		List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
		for (Admin admin : admins) {
			if (admin.getName().equals(userName) && pm.isCorrectPassword(password, admin.getAdminID(), 2)) {
				UserIdentificationManager.typeOfUser = 2;
				UserIdentificationManager.id = admin.getAdminID();
				return 2;
			}
		}
		return -1;
	}

//	public static int setUser(String userName, String password) {
//		List<User> users = DAOfactory.INSTANCE.getUserDAO().getAll();
//		for (User user : users) {
//			if (user.getName().equals(userName) && user.getPassword().equals(password)) {
//				UserIdentificationManager.typeOfUser = 1;
//				UserIdentificationManager.id = user.getUserID();
//				return 1;
//			}
//		}
//		List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
//		for (Admin admin : admins) {
//			if (admin.getName().equals(userName) && admin.getPassword().equals(password)) {
//				UserIdentificationManager.typeOfUser = 2;
//				UserIdentificationManager.id = admin.getAdminID();
//				return 2;
//			}
//		}
//		return -1;
//	}

	public static void logOut() {
		UserIdentificationManager.typeOfUser = 0;
		UserIdentificationManager.id = null;
	}

}
