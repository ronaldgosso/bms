package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class enrolldepartment {

	
	  private  StringProperty name;
	  private  StringProperty serial;
	  private  StringProperty supervisor;
	  private  StringProperty date;
	  
	  public enrolldepartment(String name,String serial,String supervisor,String date){
		  this.name = new SimpleStringProperty(name);
		  this.serial = new SimpleStringProperty(serial);
		  this.supervisor= new SimpleStringProperty(supervisor);
		  this.date = new SimpleStringProperty(date);
	  }

	public void setName(String value) {
		name.set(value);;
	}

	public void setSerial(String value) {
		serial.set(value);;
	}

	public void setSupervisor(String value) {
		supervisor.set(value);
	}

	public void setDate(String value) {
		date.set(value);
	}

	
	
	public String getName() {
		return name.get();
	}

	public String getSerial() {
		return serial.get();
	}

	public String getSupervisor() {
		return supervisor.get();
	}

	public String getDate() {
		return date.get();
	}
	
	
	public StringProperty nameProperty(){
		return name;
	}
	public StringProperty serialProperty(){
		return serial;
	}
	public StringProperty supervisorProperty(){
		return supervisor;
	}
	public StringProperty dateProperty(){
		return date;
	}
	  
}
