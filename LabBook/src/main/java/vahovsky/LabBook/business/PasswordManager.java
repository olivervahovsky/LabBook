package vahovsky.LabBook.business;

import java.util.List;

import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;

public class PasswordManager {

	/** Method checking the correctness of the user/admin password.
	 * @param	password String provided by user as a password to be checked.
	 * @param	id Identification of the user/admin in the database.
	 * @param	UserType	Type of the user - user or admin.
	 * @return	True if password is correct, false otherwise.	
	 */
	public boolean isCorrectPassword(String password, Long id, int UserType) {
		if (UserType == 1) {
			List<User> users = DAOfactory.INSTANCE.getUserDAO().getAll();
			for (User user : users) {
				if (user.getEntityID().equals(id)) {
					return password.equals(user.getPassword());
				}
			}
		} else {
			List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
			for (Admin admin : admins) {
				if (admin.getEntityID().equals(id)) {
					return password.equals(admin.getPassword());
				}
			}
		}
		return false;
	}
}
