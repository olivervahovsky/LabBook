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

	/**
	 * Method that tests if method
	 * <code>setUserOrAdmin(String userName, String password)</code> in class
	 * <code>UserIdentificationManager</code> works properly. Test user is created
	 * and set. Methods <code>getTypeOfUser()</code> and <code>getId()</code> from
	 * class <code>UserIdentificationManager</code> are used to test, if the test
	 * user was properly set. The same then goes for the admin.
	 */
	@Test
	void setUserOrAdminTest() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		int output = UserIdentificationManager.setUserOrAdmin("tester", "1234");
		assertEquals(UserIdentificationManager.getTypeOfUser(), 1);
		assertEquals(UserIdentificationManager.getId(), testUser.getEntityID());
		assertEquals(output, 1);

		userDAO.deleteEntity(testUser);

		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		adminDAO.addAdmin(testAdmin);

		output = UserIdentificationManager.setUserOrAdmin("tester", "1234");
		assertEquals(UserIdentificationManager.getTypeOfUser(), 2);
		assertEquals(UserIdentificationManager.getId(), testAdmin.getEntityID());
		assertEquals(output, 2);

		adminDAO.deleteEntity(testAdmin);
	}

	/**
	 * Method that tests if method <code>logOut()</code> in class
	 * <code>UserIdentificationManager</code> works properly. Test user is created,
	 * set and tested, if it was set properly. Then the method <code>logOut()</code>
	 * is used to log out the user from the app which is tested with the methods
	 * <code>getTypeOfUser()</code> and <code>getId()</code> from class
	 * <code>UserIdentificationManager</code>.
	 */
	@Test
	void logOutTest() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		UserIdentificationManager.setUserOrAdmin("tester", "1234");
		assertEquals(UserIdentificationManager.getTypeOfUser(), 1);
		assertEquals(UserIdentificationManager.getId(), testUser.getEntityID());

		UserIdentificationManager.logOut();
		assertEquals(UserIdentificationManager.getTypeOfUser(), 0);
		assertEquals(UserIdentificationManager.getId(), null);

		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests if method <code>getUser()</code> in class
	 * <code>UserIdentificationManager</code> works properly. Test user is created
	 * and set by the method
	 * <code>setUserOrAdmin(String userName, String password)</code> and
	 * subsequently the user returned from the method <code>getUser()</code> is
	 * compared to him.
	 */
	@Test
	void getUserTest() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		UserIdentificationManager.setUserOrAdmin("tester", "1234");
		assertEquals(UserIdentificationManager.getUser().getEntityID(), testUser.getEntityID());

		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests if method <code>getAdmin()</code> in class
	 * <code>UserIdentificationManager</code> works properly. Test admin is created
	 * and set by the method
	 * <code>setUserOrAdmin(String userName, String password)</code> and
	 * subsequently the admin returned from the method <code>getAdmin()</code> is
	 * compared to him.
	 */
	@Test
	void getAdminTest() {
		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		adminDAO.addAdmin(testAdmin);

		UserIdentificationManager.setUserOrAdmin("tester", "1234");
		assertEquals(UserIdentificationManager.getAdmin().getEntityID(), testAdmin.getEntityID());

		adminDAO.deleteEntity(testAdmin);
	}

}
