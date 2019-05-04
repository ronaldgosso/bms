package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class enrollSupervisor {
	private StringProperty Fname;
	private StringProperty Lname;
	private StringProperty username;
	private StringProperty date;
	private StringProperty email;
	private StringProperty password;

	public String getEmail() {
		return email.get();
	}

	public String getPassword() {
		return password.get();
	}

	public void setEmail(String value) {
		email.set(value);
		;
	}

	public void setPassword(String value) {
		password.set(value);
	}

	public enrollSupervisor(String username, String password, String email, String Fname , String Lname,String date) {
		
		this.username = new SimpleStringProperty(username);
		this.password = new SimpleStringProperty(password);
		this.email = new SimpleStringProperty(email);
		this.Fname = new SimpleStringProperty(Fname);
		this.Lname = new SimpleStringProperty(Lname);
		this.date = new SimpleStringProperty(date);
		
	}

	public void setName(String value) {
		Fname.set(value);
	}

	public void setSerial(String value) {
		Lname.set(value);
	}

	public void setSupervisor(String value) {
		username.set(value);
	}

	public void setDate(String value) {
		date.set(value);
	}

	public String getName() {
		return Fname.get();
	}

	public String getSerial() {
		return Lname.get();
	}

	public String getSupervisor() {
		return username.get();
	}

	public String getDate() {
		return date.get();
	}

	public StringProperty FnameProperty() {
		return Fname;
	}

	public StringProperty LnameProperty() {
		return Lname;
	}

	public StringProperty usernameProperty() {
		return username;
	}

	public StringProperty dateProperty() {
		return date;
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public StringProperty emailProperty() {
		return email;
	}

}
