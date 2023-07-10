package Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Enums.CellType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class FileDataWriter extends AbstractWriter{
	private FileChooser fileChooser;
	private File file;
	
	public FileDataWriter() throws NullPointerException{
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		this.file = fileChooser.showOpenDialog(null);
		if(file == null) {
			throw new NullPointerException();
		}
	}
	
	@Override
	public void setData() {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
			writer.write(fieldSize.getKey() + " " + fieldSize.getValue());
			writer.newLine();
			
			writer.write(heuruistic.toString());
			writer.newLine();
			
			for(int i = 0; i <= fieldSize.getKey() - 1; i++) {
				for(int j = 0; j < fieldSize.getValue(); j++) {
					if( j == fieldSize.getValue() - 1) {
						if(field[i][j].equals(CellType.CHECKED) || field[i][j].equals(CellType.RESULT)) {
							writer.write(CellType.DEFAULT.toString());													
						} else {
							writer.write(field[i][j].toString());
						}
					} else {
						if(field[i][j].equals(CellType.CHECKED) || field[i][j].equals(CellType.RESULT)) {
							writer.write(CellType.DEFAULT.toString() + " ");
						} else {
							writer.write(field[i][j].toString() + " ");							
						}
					}
				}
				writer.newLine();
			}
			
		} catch(IOException e) {
			System.out.println("Undefined error");
		} 
		
	}
	
	public File getFile() {
		return file;
	}
}
