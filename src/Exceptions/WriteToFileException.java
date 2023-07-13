package Exceptions;

@SuppressWarnings("serial")
public class WriteToFileException extends AbstractException{ 
	
	public WriteToFileException() {
		super();
	}
	
	public WriteToFileException(String msg) {
		super(msg);
	}
	
	
	@Override
	public void showErrorAlert(String msg) {
		alert.setContentText(msg + "\n" + this.getMessage());
        alert.showAndWait();
	}
}
