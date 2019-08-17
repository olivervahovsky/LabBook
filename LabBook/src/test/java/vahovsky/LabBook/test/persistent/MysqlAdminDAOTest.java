package vahovsky.LabBook.test.persistent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.persistent.AdminDAO;
import vahovsky.LabBook.persistent.DAOfactory;

class MysqlAdminDAOTest {

	@Test
	void testGetAll() {
		List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
		assertNotNull(admins);
		assertTrue(admins.size() > 0);
	}
	
	@Test
	void addDeleteTest() {
		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		boolean notThere = true;
		List<Admin> all = adminDAO.getAll();
		for (Admin a : all) {
			if (a.getAdminID().equals(testAdmin.getAdminID())) {
				notThere = false;
			}
		}
		assertTrue(notThere);

		adminDAO.addAdmin(testAdmin);
		all = adminDAO.getAll();
		boolean succesfullyAdded = false;
		for (Admin a : all) {
			if (a.getAdminID().equals(testAdmin.getAdminID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		adminDAO.deleteAdmin(testAdmin);
		all = adminDAO.getAll();
		boolean successfullyDeleted = true;
		for (Admin a : all) {
			if (a.getAdminID().equals(testAdmin.getAdminID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
	}
	
	@Test
	void testSave() {
		Admin testAdmin = new Admin();
		testAdmin.setName("tester");
		testAdmin.setPassword("1234");
		AdminDAO adminDAO = DAOfactory.INSTANCE.getAdminDAO();
		// create
		adminDAO.saveAdmin(testAdmin);
		assertNotNull(testAdmin.getAdminID());
		testAdmin.setName("tester_new");
		testAdmin.setEmail("admin.omnipotentny@mail.sk");
		// update
		adminDAO.saveAdmin(testAdmin);
		List<Admin> all = adminDAO.getAll();
		for (Admin a : all) {
			if (a.getAdminID().equals(testAdmin.getAdminID())) {
				assertEquals("tester_new", a.getName());
				adminDAO.deleteAdmin(a);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

}
