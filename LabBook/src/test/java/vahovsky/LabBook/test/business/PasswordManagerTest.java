package vahovsky.LabBook.test.business;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.business.PasswordManager;
import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.AdminDAO;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

public class PasswordManagerTest {
	
	@Test
	void isCorrectPasswordTest() {
		
		PasswordManager manager = new PasswordManager();
		
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);
		
		assertTrue(manager.isCorrectPassword("1234", testUser.getEntityID(), 1));
		assertFalse(manager.isCorrectPassword("1234", testUser.getEntityID(), 2));
		assertFalse(manager.isCorrectPassword("1234", 99l, 1));
		assertFalse(manager.isCorrectPassword("1244", testUser.getEntityID(), 1));
		assertFalse(manager.isCorrectPassword("", testUser.getEntityID(), 1));
		
		userDAO.deleteEntity(testUser);
		
		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		adminDAO.addAdmin(testAdmin);
		
		assertTrue(manager.isCorrectPassword("1234", testAdmin.getEntityID(), 2));
		assertFalse(manager.isCorrectPassword("1234", testAdmin.getEntityID(), 1));
		assertFalse(manager.isCorrectPassword("1234", 99l, 2));
		assertFalse(manager.isCorrectPassword("1244", testAdmin.getEntityID(), 1));
		assertFalse(manager.isCorrectPassword("", testAdmin.getEntityID(), 1));
		
		adminDAO.deleteEntity(testAdmin);
		
	}

}
