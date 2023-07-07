package View;

import Enums.CellType;
import application.Controller;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.util.Pair;


public class Field {
	private CellView[][] field;
	private int numRows;
	private int numColumns;
	private final float gridPaneSize = 780;
	private CellView startCell;
	private CellView endCell;
	
	public void setField(Pane pane, CellType[][] dataField) {
		this.field = new CellView[numRows][numColumns];
		double rectangleSize = (Math.min(gridPaneSize/numColumns, gridPaneSize/numRows));
		
		double currXcoord = 0;
		double currYcoord = gridPaneSize - rectangleSize;
		
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				CellView cell = new CellView(rectangleSize, rectangleSize);
				field[i][j] = cell;
				
				if(dataField != null) {
					cell.setCellType(dataField[i][j]);
					if(dataField[i][j] == CellType.START) {
						setStartCell(cell);
					} if(dataField[i][j] == CellType.END){
						setEndCell(cell);
					}
				} else {
					cell.setCellType(CellType.DEFAULT);					
				}
				
				cell.setLayoutX(currXcoord);
				cell.setLayoutY(currYcoord);
				
				cell.setStrokeType(StrokeType.INSIDE);
				cell.setStrokeWidth(1.0 /70 * rectangleSize);
				cell.setStroke(Color.BLACK);
				
				cell.setRowIdx(i);
				cell.setColIdx(j);
				
				cell.setOnMouseClicked(event -> Controller.mouseClickedHandler(event, cell));
				
				pane.getChildren().add(cell);
				
				currXcoord += rectangleSize;
			}
			currXcoord = 0;
			currYcoord -= rectangleSize;
		}
	}
	
	public CellType[][] getField(){
		CellType[][] fieldForFile = new CellType[numRows][numColumns];
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				fieldForFile[i][j] = field[i][j].getCellType();
			}
		}
		
		return fieldForFile;
	}
	
	public void setFieldSize(Pair<Integer, Integer> size) {
		this.numRows = size.getKey();
		this.numColumns = size.getValue();
	}
	
	public Pair<Integer, Integer> getFieldSize(){
		return new Pair<>(numRows, numColumns);
	}
	
	public void setNumRows(int rows) {
		this.numRows = rows;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public void setNumColumns(int columns) {
		this.numColumns = columns;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public void setCell(int xIdx, int yIdx, CellView rec) {
		field[xIdx][yIdx] = rec;
	}
	
	public CellView getCell(int xIdx, int yIdx) {
		return field[xIdx][yIdx];
	}
	
	public void setStartCell(CellView cell) {
		this.startCell = cell;
		if(startCell != null) {
			startCell.setCellType(CellType.START);			
		}
	}

	public CellView getStartCell() {
		return startCell;
	}

	public void setEndCell(CellView cell) {
		this.endCell = cell;
		if(endCell != null) {
			endCell.setCellType(CellType.END);			
		}
	}
	
	public CellView getEndCell() {
		return endCell;
	}

	public void clear() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				this.field[i][j] = null;
			}
		}
	}
	
}
