package Exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("serial")
public abstract class AbstractException extends Exception{
	Alert alert = null;
	
	protected AbstractException() {
		super();
		this.alert = new Alert(AlertType.WARNING);
		alert.setTitle("WARNING");
		alert.setHeaderText("The program has been stopped because: " + this.getClass().toString() + " was caused");
	}
	
	protected AbstractException(String msg) {
		super(msg);
		this.alert = new Alert(AlertType.WARNING);
		alert.setTitle("WARNING");
		alert.setHeaderText("The program has been stopped because: " + this.getClass().toString() + " was caused");
	}
	
	protected AbstractException(String msg, Throwable cause) {
		super(msg,cause);
		this.alert = new Alert(AlertType.WARNING);
		alert.setTitle("WARNING");
		alert.setHeaderText("The program has been stopped because: " + this.getClass().toString() + " was caused");
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + super.getLocalizedMessage();
	}
	
	
	public abstract void showErrorAlert(String msg);
}
