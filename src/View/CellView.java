package View;

import Enums.CellType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellView extends Rectangle{
	private int rowIdx;
	private int colIdx;
	private CellType cellType = CellType.DEFAULT; 
	
	public CellView(double width, double height) {
		super(width, height);
	}
	
	public void setCellType(CellType type) {
		if (type == CellType.BLOCKED) {
			this.setFill(Color.BLACK);
		} if (type == CellType.DEFAULT) {
			this.setFill(Color.GREEN);
		} if (type == CellType.START) {
			this.setFill(Color.BLUE);
		} if (type == CellType.END) {
			this.setFill(Color.RED);
		} if (type == CellType.CHECKED) {
			this.setFill(Color.YELLOW);
		} if (type == CellType.RESULT) {
			this.setFill(Color.AQUA);
		}
		this.cellType = type;
	}
	
	public CellType getCellType() {
		return cellType;
	}
	
	public void setRowIdx(int rowIdx) {
		this.rowIdx = rowIdx;
	}
	
	public void setColIdx(int colIdx) {
		this.colIdx = colIdx;
	}
	
	public int getRowIdx() {
		return rowIdx;
	}
	
	public int getColIdx() {
		return colIdx;
	}
	
}
