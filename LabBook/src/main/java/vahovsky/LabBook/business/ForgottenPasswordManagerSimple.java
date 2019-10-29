package vahovsky.LabBook.business;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.lang3.RandomStringUtils;

import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

// https://stackoverflow.com/questions/46663/how-can-i-send-an-email-by-java-application-using-gmail-yahoo-or-hotmail#47452

public class ForgottenPasswordManagerSimple {

	// GMail user name (just the part before "@gmail.com")
	private static String USER_NAME = "ovlv.projekt";
	// GMail password
	private static String PASSWORD = "LabBook17ViVa";

	/**
	 * Method to set new random password to a user defined by an email address and
	 * to send him this new password
	 * 
	 * @param email email address of the user that requested new password
	 */
	public static void sendPassword(String email) {
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();

		User user = userDAO.getByEmail(email);
		String newPassword = RandomStringUtils.randomAscii(10);
		user.setPassword(newPassword);
		userDAO.saveUser(user);

		String from = USER_NAME;
		String pass = PASSWORD;
		String[] to = { email }; // list of recipient email addresses
		String subject = "Forgotten Password";
		String body = "Your new password is " + userDAO.getByEmail(email).getPassword();

		sendFromGMail(from, pass, to, subject, body);
	}

	/**
	 * Method to send mail message through smtp.gmail.com server
	 * 
	 * @param from    gmail username of the account, from which the message is sent
	 * @param pass    gmail password of the account, from which the message is sent
	 * @param to      field of mail addresses of all the recipients of the message
	 *                to be sent
	 * @param subject subject of the message to be sent
	 * @param body    body of the message to be sent
	 */
	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
}