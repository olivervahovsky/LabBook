package vahovsky.LabBook.test.persistent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.persistent.AdminDAO;
import vahovsky.LabBook.persistent.DAOfactory;

class MysqlAdminDAOTest {

	/**
	 * Method that tests method <code>getAll()</code> in class
	 * <code>MysqlAdminDAO</code>. Tests if the list of admins returned from the
	 * database have size > 0.
	 */
	@Test
	void testGetAll() {
		List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
		assertNotNull(admins);
		assertTrue(admins.size() > 0);
	}

	/**
	 * Method that tests method <code>addAdmin(Admin admin)</code> and
	 * <code>deleteEntity(Entity admin)</code> in a class
	 * <code>MysqlAdminDAO</code>. Test admin is created and it is checked if such
	 * an admin is not already in the database. He is then added into the database
	 * and it is tested if the addition was successful. In the end he is removed
	 * from the database and it is tested if the removal was successful.
	 */
	@Test
	void addDeleteTest() {
		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		boolean notThere = true;
		List<Admin> all = adminDAO.getAll();
		for (Admin a : all) {
			if (a.getEntityID().equals(testAdmin.getEntityID())) {
				notThere = false;
			}
		}
		assertTrue(notThere);

		adminDAO.addAdmin(testAdmin);
		all = adminDAO.getAll();
		boolean succesfullyAdded = false;
		for (Admin a : all) {
			if (a.getEntityID().equals(testAdmin.getEntityID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		adminDAO.deleteEntity(testAdmin);
		all = adminDAO.getAll();
		boolean successfullyDeleted = true;
		for (Admin a : all) {
			if (a.getEntityID().equals(testAdmin.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
	}

	/**
	 * Method that tests method <code>saveAdmin</code> in class
	 * <code>MysqlAdminDAO</code>. Test admin is made and saved into the database.
	 * First test runs to assert that he was correctly added into the database. Next
	 * some of his parameters are changed and saved. Then we look for the test admin
	 * in the database (through his ID) and check, if his parameters really changed.
	 * In the end it is removed from the database.
	 */
	@Test
	void testSave() {
		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		// create
		adminDAO.saveAdmin(testAdmin);
		assertNotNull(testAdmin.getEntityID());
		testAdmin.setName("tester_new");
		testAdmin.setEmail("admin.omnipotentny@mail.sk");
		// update
		adminDAO.saveAdmin(testAdmin);
		List<Admin> all = adminDAO.getAll();
		for (Admin a : all) {
			if (a.getEntityID().equals(testAdmin.getEntityID())) {
				assertEquals("tester_new", a.getName());
				adminDAO.deleteEntity(a);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

	/**
	 * Method that tests method <code>isNameAvailable(String name)</code> in class
	 * <code>MysqlAdminDAO</code>. Test admin is made and added into the database.
	 * Before the addition, the name of the test admin should not by present in the
	 * database, hence method <code>isNameAvailable(String name)</code> should
	 * evaluate to <code>true</code>. After addition, it should evaluate to
	 * <code>false</code>.
	 */
	@Test
	void testIsNameAvailable() {
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		assertTrue(adminDAO.isNameAvailable("tester"));
		adminDAO.addAdmin(testAdmin);
		assertFalse(adminDAO.isNameAvailable("tester"));
		adminDAO.deleteEntity(testAdmin);
	}

}
