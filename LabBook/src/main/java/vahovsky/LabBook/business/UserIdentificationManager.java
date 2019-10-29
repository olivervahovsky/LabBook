package vahovsky.LabBook.business;

import java.util.List;

import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;

public class UserIdentificationManager {

	private static int typeOfUser;
	private static Long id;

	/**
	 * Method that returns integer characterizing type of the logged-in user (user
	 * or admin).
	 * 
	 * @return integer characterizing type of the logged-in user
	 */
	public static int getTypeOfUser() {
		return typeOfUser;
	}

	/**
	 * Method that returns id of the signed user/admin.
	 * 
	 * @return id of the signed user/admin
	 */
	public static Long getId() {
		return id;
	}

	/**
	 * Method that returns user that is currently signed in, if any, If user is not
	 * signed in, returns null.
	 * 
	 * @return user, in case one is currently signed in.
	 */
	public static User getUser() {
		List<User> users = DAOfactory.INSTANCE.getUserDAO().getAll();
		for (User u : users) {
			if (u.getUserID().equals(id)) {
				return u;
			}
		}
		return null;
	}

	/**
	 * Method that returns admin that is currently signed in, if any, If admin is
	 * not signed in, returns null.
	 * 
	 * @return admin, in case one is currently signed in.
	 */
	public static Admin getAdmin() {
		List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
		for (Admin a : admins) {
			if (a.getAdminID().equals(id)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Method that sets the user for use in the app during login. Type of the user
	 * will be represented by an integer value, 1 for user, 2 for admin. ID of the
	 * user in the app is set according to an ID of the corresponding user in
	 * database. Returns integer value that determines what window opens next.
	 * 
	 * @param userName username typed during the login.
	 * @param password password typed during the login.
	 * @return integer indicating if the login credentials belong to a user, and
	 *         admin or neither (wrong username or password)
	 */
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

	/**
	 * Method that resets typeOfUser and id variables which are used to reference
	 * the user in the app.
	 */
	public static void logOut() {
		UserIdentificationManager.typeOfUser = 0;
		UserIdentificationManager.id = null;
	}

}
