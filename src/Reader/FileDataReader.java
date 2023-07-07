package Reader;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Enums.CellType;
import Enums.Heuruistics;

public class FileDataReader extends AbstractReader{
	private FileChooser fileChooser;
	private File file;
	
	public FileDataReader() {
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		this.file = fileChooser.showOpenDialog(null);
	}
	
	@Override
	public void getData() {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			String line;
			int currentLine = 0;
			int numRow = 0;
			while((line = reader.readLine()) != null) {
				String[] fieldData = line.split(" ");
				if (currentLine == 0) {
					this.fieldSize = new Pair<>(Integer.parseInt(fieldData[0]), Integer.parseInt(fieldData[1]));
					field = new CellType[Integer.parseInt(fieldData[0])][Integer.parseInt(fieldData[1])];
				} else if (currentLine == 1) {
					heuruistic = Heuruistics.valueOf(line);
				} else {
					for(int i = 0; i < fieldData.length; i++) {
						field[numRow][i] = CellType.valueOf(fieldData[i]);
					}
					numRow++;
				}
				currentLine++;
			}
			
		} catch(IOException e) {
			e.getMessage();
		} 
		
	}
	
}
