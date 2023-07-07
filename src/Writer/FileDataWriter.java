package Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class FileDataWriter extends AbstractWriter{
	private FileChooser fileChooser;
	private File file;
	
	public FileDataWriter() {
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		this.file = fileChooser.showOpenDialog(null);
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
						writer.write(field[i][j].toString());						
					} else {
						writer.write(field[i][j].toString() + " ");
					}
				}
				writer.newLine();
			}
			
		} catch(IOException e) {
			e.getStackTrace();
		}
		
	}
}
