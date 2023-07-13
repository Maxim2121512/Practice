package Model;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;


import Enums.CellType;
import Enums.Heuruistics;
import Exceptions.InitializeException;
import javafx.util.Pair;

public class Model {
	private Cell startCell = null;
	private Cell endCell = null;
	private Cell[][] field = null;
	ArrayList<Cell> closeSet = new ArrayList<Cell>();
	HashMap<Cell, Cell> cameFrom = new HashMap<>();
	PriorityQueue<Cell> openSet = new PriorityQueue<>((a, b) -> Double.compare(a.getFScore(), b.getFScore()));
	private Heuruistics heuristic = null;
	
	
	public Model() {
		
	}
	
	public Model(int height, int width) {
		field = new Cell[height][width];
	}
	
	public void setFieldSize(Pair<Integer, Integer> fieldSize) {
		field = new Cell[fieldSize.getKey()][fieldSize.getValue()];
	}
	
	
	public void setStartCell(Cell newStartCell) {
		if(field[newStartCell.getRowIdx()][newStartCell.getColIdx()].getType() == CellType.END) {
			endCell = null;
		}
		if(startCell != null) {
			startCell.setType(CellType.DEFAULT);
		}
		
		field[newStartCell.getRowIdx()][newStartCell.getColIdx()] = newStartCell;
		
		
		startCell = newStartCell;
	}
	
	public void setEndCell(Cell newEndCell) {
		if(field[newEndCell.getRowIdx()][newEndCell.getColIdx()].getType() == CellType.START) {
			startCell = null;
		}
		if(endCell != null) {
			endCell.setType(CellType.DEFAULT);			
		}
		
		field[newEndCell.getRowIdx()][newEndCell.getColIdx()] = newEndCell;
		
		
		endCell = newEndCell;
	}
	
	public Cell getStartCell() {
		return startCell;
	}
	
	public Cell getEndCell() {
		return endCell;
	}
	
	
	public void setHeuristic(Heuruistics heuristic) {
		this.heuristic = heuristic;
	}
	
	public Heuruistics getHeuristic() {
		return heuristic;
	}
	
	public Cell getCell(int numRow, int numCol) {
		return field[numRow][numCol];
	}
	
	public void initializeField() {
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[0].length; j++) {
				field[i][j] = new Cell(CellType.DEFAULT, i, j);
			}
		}
	}
	
	public Cell[][] getField() {
		return field;
	}
	
	public void setField(CellType[][] typeField) {
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				field[i][j] = new Cell(typeField[i][j], i, j);
				if(typeField[i][j] == CellType.START) {
					this.startCell = field[i][j];
				} if(typeField[i][j] == CellType.END) {
					this.endCell = field[i][j];
				}
			}
		}
	}
	
	private void setNeighbors() {
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[0].length; j++) {
				for(int dx = -1; dx <= 1; dx++) {
					for(int dy = -1; dy <= 1; dy++) {
						if(dx == 0 && dy == 0) {
							continue;
						}
						
						int numCol = j + dx;
						int numRow = i + dy;
						
						if(numCol >= 0 && numCol < field[0].length && numRow >= 0 && numRow < field.length) {
							field[i][j].addNeighbor(field[numRow][numCol]);
						}
					}
				}
			}
		}
	}
	
	public void prepareToStart() throws InitializeException{
		
		if(field == null) {
			throw new InitializeException("Field not initialized");
		}
		
		if(startCell == null) {
			throw new InitializeException("Start cell not initialized");
		}
		
		if(endCell == null) {
			throw new InitializeException("End cell not initialized");
		}
		
		if(heuristic == null) {
			throw new InitializeException("Heuristic not initialized");
		}
		
		startCell.setGScore(0.0);
		startCell.setHScore(calculateHeuristic(startCell));
		openSet.add(startCell);
		
		
		setNeighbors();
	}
	
	private double calculateHeuristic(Cell cell) {
		double dx = Math.abs(cell.getRowIdx() - endCell.getRowIdx());
		double dy = Math.abs(cell.getColIdx() - endCell.getColIdx());
		
		switch (heuristic) {
			case EUCLIDIANDISTANCE:
				return Math.sqrt(dx * dx + dy * dy);
			case MANHATTANDISTANCE:
				return dx + dy;
			case DIAGONALDISTANCE:
				return Math.max(dx, dy);
			default:
				return 0;
		}
		
	}
	
	private void delBlockedCellFromQueue() {
	    Iterator<Cell> iterator = openSet.iterator();
	    while (iterator.hasNext()) {
	        Cell cell = iterator.next();
	        if (cell.getType() == CellType.BLOCKED) {
	            iterator.remove();
	        }
	    }
	}

	
	public Pair<Integer, Integer> Astar(){
		
		delBlockedCellFromQueue();
		
		if (!openSet.isEmpty()) {
			Cell currentCell = openSet.poll();

			closeSet.add(currentCell);
			
			if(currentCell.equals(endCell)) {
				return new Pair<>(currentCell.getRowIdx(), currentCell.getColIdx());
			}
			
			ArrayList<Cell> neighbors = currentCell.getNeighbors();
			
			for(Cell neighbor: neighbors) {
				if(neighbor.getType() != CellType.BLOCKED) {
					double tentativeGScore = currentCell.getGScore() + 1.0;
					
					if(tentativeGScore < neighbor.getGScore()) {
						cameFrom.put(neighbor, currentCell);
						neighbor.setGScore(tentativeGScore);
						neighbor.setHScore(calculateHeuristic(neighbor));
						openSet.add(neighbor);
					}
				}
			}
			if(openSet.peek() != null) {
				return new Pair<>(openSet.peek().getRowIdx(), openSet.peek().getColIdx());
			}
		}
		
		return null;
	}
	
	public Pair<Integer, ArrayList<Cell>> makeResult() {
		ArrayList<Cell> totalPath = new ArrayList<>();
		Cell current = this.endCell;
		totalPath.add(current);
		int resultLenght = 0;
		while(!current.equals(startCell)) {
			current = cameFrom.get(current);
			resultLenght +=1;
			totalPath.add(current);
		}
		
		return new Pair<>(resultLenght, totalPath);
	}
	
	public void clear() {
        field = null;
        heuristic = null;
        startCell = null;
        endCell = null;
        openSet.clear();
        closeSet.clear();
        cameFrom.clear();
    }
}
