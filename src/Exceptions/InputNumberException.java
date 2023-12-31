package Exceptions;

@SuppressWarnings("serial")
public class InputNumberException extends AbstractException{
	private String inputData = null; 
	
	public InputNumberException(String msg, String inputData) {
		super(msg);
		this.inputData = inputData;
	}
	
	public InputNumberException(String msg) {
		super(msg);
	}
	
	
	public String getInputData() {
		return inputData;
	}

	@Override
	public void showErrorAlert(String msg) {
		if(getInputData() != null) {	
			alert.setContentText(this.getMessage() + ": " + getInputData() + "\n" + msg);
		} else {
			alert.setContentText(this.getMessage());
		}
        alert.showAndWait();	
	}
		
}
