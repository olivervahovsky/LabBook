package vahovsky.LabBook.test.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.AdminDAO;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

public class UserIdentificationManagerTest {

	@Test
	void setUserTest() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		int output = UserIdentificationManager.setUser("tester", "1234");
		assertEquals(UserIdentificationManager.getTypeOfUser(), 1);
		assertEquals(UserIdentificationManager.getId(), testUser.getEntityID());
		assertEquals(output, 1);

		userDAO.deleteEntity(testUser);

		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		adminDAO.addAdmin(testAdmin);

		output = UserIdentificationManager.setUser("tester", "1234");
		assertEquals(UserIdentificationManager.getTypeOfUser(), 2);
		assertEquals(UserIdentificationManager.getId(), testAdmin.getEntityID());
		assertEquals(output, 2);

		adminDAO.deleteEntity(testAdmin);
	}

	@Test
	void logOutTest() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		UserIdentificationManager.setUser("tester", "1234");
		assertEquals(UserIdentificationManager.getTypeOfUser(), 1);
		assertEquals(UserIdentificationManager.getId(), testUser.getEntityID());

		UserIdentificationManager.logOut();
		assertEquals(UserIdentificationManager.getTypeOfUser(), 0);
		assertEquals(UserIdentificationManager.getId(), null);

		userDAO.deleteEntity(testUser);
	}

	@Test
	void getUserTest() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		UserIdentificationManager.setUser("tester", "1234");
		assertEquals(UserIdentificationManager.getUser().getEntityID(), testUser.getEntityID());

		userDAO.deleteEntity(testUser);
	}

	@Test
	void getAdminTest() {
		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		adminDAO.addAdmin(testAdmin);
		
		UserIdentificationManager.setUser("tester", "1234");
		assertEquals(UserIdentificationManager.getAdmin().getEntityID(), testAdmin.getEntityID());
		
		adminDAO.deleteEntity(testAdmin);
	}

}
