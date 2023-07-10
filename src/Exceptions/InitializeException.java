package Exceptions;

@SuppressWarnings("serial")
public class InitializeException extends AbstractException{
	
	public InitializeException() {
		super();
	}
	
	public InitializeException(String msg) {
		super(msg);
	}
	
	@Override
	public void showErrorAlert(String msg) {
		alert.setContentText(this.getMessage() + "\n" + msg);
        alert.showAndWait();	
	}
	
}
