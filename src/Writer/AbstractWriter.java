package Writer;



import Enums.CellType;
import Enums.Heuruistics;
import Exceptions.WriteToFileException;
import javafx.util.Pair;

public abstract class AbstractWriter {
	protected Pair<Integer, Integer> fieldSize;
	protected Heuruistics heuruistic;
	protected CellType[][] field;
	
	public void setFieldSize(Pair<Integer, Integer> fieldSize) throws WriteToFileException{
		if(fieldSize.getKey() == null || fieldSize.getValue() == null) {
			throw new WriteToFileException("Can't be saved. Field size not set");
		}
		this.fieldSize = fieldSize;
	}
	
	public void setHeuristic(Heuruistics heuristic) throws WriteToFileException{
		if(heuristic == null) {
			throw new WriteToFileException("Can't be saved. Heuristic not set");
		}
		this.heuruistic = heuristic;
	}
	
	public void setField(CellType[][] field) throws WriteToFileException{
		if(field == null ) {
			throw new WriteToFileException("Can't be saved. Field not set");
		}
		this.field = field;
	}
	
	public abstract void setData();
}
