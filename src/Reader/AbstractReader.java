package Reader;

import Enums.CellType;
import Enums.Heuruistics;
import javafx.util.Pair;

public abstract class AbstractReader {
	protected Pair<Integer, Integer> fieldSize;
	protected Heuruistics heuruistic;
	protected CellType[][] field;
	
	public Pair<Integer, Integer> getFieldSize() {
		return fieldSize;
	}
	
	public Heuruistics getHeuruistic() {
		return heuruistic;
	}
	
	public CellType[][] getField() {
		return field;
	}
	
	public abstract void getData();
}
