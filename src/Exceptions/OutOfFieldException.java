package Exceptions;


@SuppressWarnings("serial")
public class OutOfFieldException extends AbstractException{
	private String inputData = null;
	private Integer borderNumber = null;
	
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
	
	public Integer getBorderNumber() {
		return borderNumber;
	}

	@Override
	public void showErrorAlert(String msg) {
		alert.setContentText(this.getMessage() + ": " + getInputData() + "\n" + msg);
        alert.showAndWait();	
	}
}
