package vahovsky.LabBook.business;

import java.util.List;

import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;

public class PasswordManager {

	public boolean isCorrectPassword(String password, Long id, int UserType) {
		if (UserType == 1) {
			List<User> users = DAOfactory.INSTANCE.getUserDAO().getAll();
			for (User user : users) {
				if (user.getUserID().equals(id)) {
					return password.equals(user.getPassword());
				}
			}
		} else {
			List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
			for (Admin admin : admins) {
				if (admin.getAdminID().equals(id)) {
					return password.equals(admin.getPassword());
				}
			}
		}
		return false;
	}
}
