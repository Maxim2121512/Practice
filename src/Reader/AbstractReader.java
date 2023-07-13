package Reader;



import Enums.CellType;
import Enums.Heuruistics;
import Exceptions.InputNumberException;
import Exceptions.ReadFromFileException;
import javafx.util.Pair;

public abstract class AbstractReader {
	protected Pair<Integer, Integer> fieldSize;
	protected Heuruistics heuruistic;
	protected CellType[][] field;
	protected String notAllowedValues = ("RESULT|CHECKED");
	public Pair<Integer, Integer> getFieldSize() {
		return fieldSize;
	}
	
	public Heuruistics getHeuruistic() {
		return heuruistic;
	}
	
	public CellType[][] getField() {
		return field;
	}
	
	public abstract void getData() throws InputNumberException, ReadFromFileException;
}
