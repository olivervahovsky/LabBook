package vahovsky.LabBook.persistent;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;

public enum DAOfactory {

	INSTANCE;

	private JdbcTemplate jdbcTemplate;
	private UserDAO userDAO;
	private ProjectDAO projectDAO;
	private AdminDAO adminDAO;
	private TaskDAO taskDAO;
	private ItemDAO itemDAO;
	private LaboratoryDAO laboratoryDAO;
	private NoteDAO noteDAO;

	/**
	 * Method that provides instance of userDAO object stored in DAOfactory class.
	 * 
	 * @return instance of userDAO shared among all classes during a single session
	 *         (running of an application).
	 */
	public UserDAO getUserDAO() {
		if (userDAO == null) {
			userDAO = new MysqlUserDAO(getJbdcTemplate());
		}
		return userDAO;
	}

	/**
	 * Method that provides instance of adminDAO object stored in DAOfactory class.
	 * 
	 * @return instance of adminDAO shared among all classes during a single session
	 *         (running of an application).
	 */
	public AdminDAO getAdminDAO() {
		if (adminDAO == null) {
			adminDAO = new MysqlAdminDAO(getJbdcTemplate());
		}
		return adminDAO;
	}

	/**
	 * Method that provides instance of projectDAO object stored in DAOfactory
	 * class.
	 * 
	 * @return instance of projectDAO shared among all classes during a single
	 *         session (running of an application).
	 */
	public ProjectDAO getProjectDAO() {
		if (projectDAO == null) {
			projectDAO = new MysqlProjectDAO(getJbdcTemplate());
		}
		return projectDAO;
	}

	/**
	 * Method that provides instance of taskDAO object stored in DAOfactory class.
	 * 
	 * @return instance of taskDAO shared among all classes during a single session
	 *         (running of an application).
	 */
	public TaskDAO getTaskDAO() {
		if (taskDAO == null) {
			taskDAO = new MysqlTaskDAO(getJbdcTemplate());
		}
		return taskDAO;
	}

	/**
	 * Method that provides instance of itemDAO object stored in DAOfactory class.
	 * 
	 * @return instance of itemDAO shared among all classes during a single session
	 *         (running of an application).
	 */
	public ItemDAO getItemDAO() {
		if (itemDAO == null) {
			itemDAO = new MysqlItemDAO(getJbdcTemplate());
		}
		return itemDAO;
	}

	/**
	 * Method that provides instance of laboratoryDAO object stored in DAOfactory
	 * class.
	 * 
	 * @return instance of laboratoryDAO shared among all classes during a single
	 *         session (running of an application).
	 */
	public LaboratoryDAO getLaboratoryDAO() {
		if (laboratoryDAO == null) {
			laboratoryDAO = new MysqlLaboratoryDAO(getJbdcTemplate());
		}
		return laboratoryDAO;
	}

	/**
	 * Method that provides instance of noteDAO object stored in DAOfactory class.
	 * 
	 * @return instance of noteDAO shared among all classes during a single session
	 *         (running of an application).
	 */
	public NoteDAO getNoteDAO() {
		if (noteDAO == null) {
			noteDAO = new MysqlNoteDAO(getJbdcTemplate());
		}
		return noteDAO;
	}

	/**
	 * Method that provides instance of jdbcTemplate object stored in DAOfactory class.
	 * 
	 * @return instance of jdbcTemplate shared among all classes during a single session
	 *         (running of an application).
	 */
	private JdbcTemplate getJbdcTemplate() {
		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			// username to login into database
			dataSource.setUser("paz1c");
			// password to login into database
			dataSource.setPassword("paz1cJeSuper");
			// dataSource.setDatabaseName("lab_book"); // nazov schemy
			dataSource.setUrl("jdbc:mysql://localhost/lab_book?serverTimezone=Europe/Bratislava");
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}

}
