package Writer;

import Enums.CellType;
import Enums.Heuruistics;
import javafx.util.Pair;

public abstract class AbstractWriter {
	protected Pair<Integer, Integer> fieldSize;
	protected Heuruistics heuruistic;
	protected CellType[][] field;
	
	public void setFieldSize(Pair<Integer, Integer> fieldSize) {
		this.fieldSize = fieldSize;
	}
	
	public void setHeuristic(Heuruistics heuristic) {
		this.heuruistic = heuristic;
	}
	
	public void setField(CellType[][] field) {
		this.field = field;
	}
	
	public abstract void setData();
}
