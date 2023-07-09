package Model;


import java.util.ArrayList;

import Enums.CellType;


public class Cell {
    private CellType type; 
    private int rowIdx; 
    private int colIdx; 
    private double gScore = Double.MAX_VALUE;
    private double hScore = Double.MAX_VALUE;
    private ArrayList<Cell> neighbors;

    public Cell(CellType type, int rowIdx, int colIdx) {
        this.type = type;
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
        this.neighbors = new ArrayList<>();
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }
    
    public int getRowIdx() {
    	return rowIdx;
    }
    
    public void setRowIdx(int rowIdx) {
    	this.rowIdx = rowIdx;
    }
    
    public int getColIdx() {
    	return colIdx;
    }
    
    public void setColIdx(int colIdx) {
    	this.colIdx = colIdx;
    }
    
    public double getGScore() {
    	return gScore;
    }
    
    public void setGScore(double gScore) {
    	this.gScore = gScore;
    }
    
    public double getHScore(double Hscore) {
    	return hScore;
    }
    
    public void setHScore(double hScore) {
    	this.hScore = hScore;
    }
    
    public double getFScore() {
    	return gScore + hScore; 
    }
    
    public ArrayList<Cell> getNeighbors() {
        return neighbors;
    }
    
    public void setNeighbors(ArrayList<Cell> neighbors) {
    	this.neighbors = neighbors;
    }
    
    public void addNeighbor(Cell neighbor) {
        neighbors.add(neighbor);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (this == o) {
    		return true;
    	}
    	if(o == null || getClass() != o.getClass()) {
    		return false;
    	}
    	
    	Cell cell = (Cell) o;
    	
    	return rowIdx == cell.rowIdx && colIdx == cell.colIdx && type == cell.type 
    			&& gScore == cell.gScore && hScore == cell.hScore;
    }
    
    @Override
    public int hashCode() {
    	int result = 17;
    	result = 31 * result + type.hashCode();
    	result = 31 * result + Integer.hashCode(rowIdx);
    	result = 31 * result + Integer.hashCode(colIdx);
    	return result;
    }
    
}
