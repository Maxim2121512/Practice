package Exceptions;

@SuppressWarnings("serial")
public class ReadFromFileException extends AbstractException{
	private String fileData = null; 
	
	public ReadFromFileException() {
		super();
	}
	
	public ReadFromFileException(String msg) {
		super(msg);
	}
	
	
	public ReadFromFileException(String msg, String fileData) {
		super(msg);
		this.fileData = fileData;
	}
	
	public String getFileData() {
		return fileData;
	}
	
	@Override
	public void showErrorAlert(String msg) {
		if(getFileData() != null) {
			alert.setContentText(msg + "\n" + this.getMessage() + ": " + getFileData());			
		} else {
			alert.setContentText(msg + "\n" + this.getMessage());
		}
        alert.showAndWait();
	}
	
}
