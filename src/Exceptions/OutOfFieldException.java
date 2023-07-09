package Exceptions;


@SuppressWarnings("serial")
public class OutOfFieldException extends AbstractException{
	 String inputData = null;
	Integer borderNumber = null;
	
	public OutOfFieldException(String msg, String inputData, int borderNumber) {
		super(msg);
		this.inputData = inputData;
		this.borderNumber = borderNumber;
	}
	
	public OutOfFieldException(String msg) {
		super(msg);
	}
	
	
	public String getInputData() {
		return inputData;
	}

	@Override
	public void showErrorAlert(String msg) {
		alert.setContentText(this.getMessage() + ": " + getInputData() + "\n" + msg);
        alert.showAndWait();	
	}
}
