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
import Exceptions.CustomIntegerParser;
import Exceptions.InputNumberException;
import Exceptions.ReadFromFileException;

public class FileDataReader extends AbstractReader{
	private FileChooser fileChooser;
	private File file;
	
	public FileDataReader() throws NullPointerException{
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		this.file = fileChooser.showOpenDialog(null);
		if(file == null) {
			throw new NullPointerException();
		}
	}
	
	@Override
	public void getData() throws InputNumberException, ReadFromFileException{
		boolean isStartWrited = false;
		boolean isEndWrited = false;
		int currentLine = 0;
		int numRow = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			String line;
			while((line = reader.readLine()) != null) {
				String[] fieldData = line.split(" ");
							
				if (currentLine == 0) {
					this.fieldSize = new Pair<>(CustomIntegerParser.parseInteger(fieldData[0]), CustomIntegerParser.parseInteger(fieldData[1]));
					field = new CellType[CustomIntegerParser.parseInteger(fieldData[0])][CustomIntegerParser.parseInteger(fieldData[1])];
				} else if (currentLine == 1) {
					try {						
						heuruistic = Heuruistics.valueOf(line);
					} catch(IllegalArgumentException e) {
						throw new ReadFromFileException("Invalid heuristic", line);
					}
				} else {
					if (numRow >= fieldSize.getKey()) {
						throw new ReadFromFileException("The number of rows given does not match the number of lines in the file");
					}
					
					for(int i = 0; i < fieldData.length; i++) {
						try {	
							if(fieldData[i].matches(notAllowedValues)) {
								throw new ReadFromFileException("Invalid CellType, use only DEFAULT, START, END, BLOCKED", fieldData[i]);
							} if(fieldData.length > fieldSize.getValue()) {
								throw new ReadFromFileException("The number of columns given does not match the number of lines in the file");
							}
							
							field[numRow][i] = CellType.valueOf(fieldData[i]);
							
							if(field[numRow][i] == CellType.START && !isStartWrited) {
								isStartWrited = true;
							} else if (field[numRow][i] == CellType.START && isStartWrited) {
								throw new ReadFromFileException("In the file more than one start cell");
							}
							
							if(field[numRow][i] == CellType.END && !isEndWrited) {
								isEndWrited = true;
							} else if (field[numRow][i] == CellType.END && isEndWrited) {
								throw new ReadFromFileException("In the file more than one end cell");
							}
							
							
						} catch (IllegalArgumentException e) {
							throw new ReadFromFileException("Invalid CellType", fieldData[i]);
						}
					}
					numRow++;
				}
				currentLine++;
			}
		} catch(IOException e) {
			System.out.println("Undefined error");
		}
		
		if(currentLine == 0) {
			throw new ReadFromFileException("File is empty");
		}
	}
	
}
