package application;


import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Enums.CellType;
import Enums.Heuruistics;
import Exceptions.CustomIntegerParser;
import Exceptions.InitializeException;
import Exceptions.InputNumberException;
import Exceptions.OutOfFieldException;
import Exceptions.ReadFromFileException;
import Exceptions.WriteToFileException;
import Model.Cell;
import Model.Model;
import View.CellView;
import View.Field;
import Reader.*;
import Writer.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.util.Pair;



public class Controller implements Initializable{
	@FXML
	private Label mainLabel;
	@FXML
	private Button setFieldSizeButton;
	@FXML
	private Button setStartCellButton;
	@FXML
	private Button setEndCellButton;
	@FXML
	private Button completeOneStepAlgButton;
	@FXML
	private Button executeCompletelyButton;
	@FXML
	private Button saveFieldToFileButton;
	@FXML 
	private Button extractFieldFromFile;
	@FXML
	private Button startOverButton;
	@FXML
	private TextField numberOfRowsText;
	@FXML
	private TextField numberOfColumnsText;
	@FXML
	private TextField startCellRowIdxText;
	@FXML
	private TextField startCellColIdxText;
	@FXML
	private TextField endCellRowIdxText;
	@FXML
	private TextField endCellColIdxText;
	@FXML
	private MenuButton setHeuristicButton;
	@FXML
	private MenuItem euclidianDistanceItem;
	@FXML
	private MenuItem manhattanDistanceItem;
	@FXML
	private MenuItem diagonalDistanceItem;
	@FXML
	private Label totalWayLabel;
	@FXML
	private Pane fieldPane;
	@FXML
	private Pane pane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private Button setStandartScaleButton;
	
	private Field field = new Field();
	
	private static Model model = new Model();
	
	boolean isFirstStep = true;
	static boolean isPossibleToUnblock = true;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setEventHandlers();
	}
	
	private void setEventHandlers() {
		
		fieldPane.addEventHandler(ScrollEvent.SCROLL, this::zoomHandler);
		setFieldSizeButton.setOnAction(this::setFieldSizeHandler);
		setStartCellButton.setOnAction(this::setStartCellHandler);
		setEndCellButton.setOnAction(this::saveEndCellHandler);
		completeOneStepAlgButton.setOnAction(this::completeOneStepAlgHandler);
		executeCompletelyButton.setOnAction(this::executeCompletelyHandler);
		extractFieldFromFile.setOnAction(this::extractFieldHandler);
		saveFieldToFileButton.setOnAction(this::saveFieldToFileHandler);
		euclidianDistanceItem.setOnAction(this::euclidianDistanceHandler);
		manhattanDistanceItem.setOnAction(this::manhattanDistanceHandler);
		diagonalDistanceItem.setOnAction(this::diagonalDistanceHandler);
		startOverButton.setOnAction(this::startOverHandler);
		setStandartScaleButton.setOnAction(this::setStandartScaleHandler);
		
	}
	
	public static void mouseClickedHandler(MouseEvent event, CellView cell) {
		if(event.getButton() == MouseButton.PRIMARY && (cell.getCellType() != CellType.START)
				&& (cell.getCellType() != CellType.END)) {
			
			if (cell.getCellType() == CellType.DEFAULT) {
				cell.setCellType(CellType.BLOCKED);
				model.getCell(cell.getRowIdx(), cell.getColIdx()).setType(CellType.BLOCKED);
			} else if (cell.getCellType() == CellType.BLOCKED && isPossibleToUnblock) {
				cell.setCellType(CellType.DEFAULT);
				model.getCell(cell.getRowIdx(), cell.getColIdx()).setType(CellType.DEFAULT);
			}
			
		}
	}
	
	private void zoomHandler(ScrollEvent event) {
		Scale scale = new Scale();
		double scaleFactor = 1.1;
		double mouseX = event.getX();
		double mouseY = event.getY();
		scale.setPivotX(mouseX);
		scale.setPivotY(mouseY);
		
		if(event.getDeltaY() < 0) {
			scaleFactor = 1 / scaleFactor;
		}
		
		scale.setX(scaleFactor);
		scale.setY(scaleFactor);
		fieldPane.getTransforms().add(scale);
	}
	
	
	private void setStandartScaleHandler(ActionEvent event) {
		fieldPane.getTransforms().clear();
	}
	
	
	private void setFieldSizeHandler(ActionEvent event) {
		try {	
			field.clear();
			model.clear();
			fieldPane.getChildren().clear();
			
			String rows = numberOfRowsText.getText();
			String columns = numberOfColumnsText.getText();
			
			int numRows = CustomIntegerParser.parseInteger(rows);
			int numColumns = CustomIntegerParser.parseInteger(columns);
			
			field.setFieldSize(new Pair<>(numRows, numColumns));
			field.setField(fieldPane, null);
			model.setFieldSize(new Pair<>(numRows, numColumns));
			model.initializeField();
			setHeuristicButton.setText("Click to choose");
			totalWayLabel.setText("Total length:");
			startCellColIdxText.setText(null);
			startCellRowIdxText.setText(null);
			endCellColIdxText.setText(null);
			endCellRowIdxText.setText(null);
		} catch(InputNumberException e) {
			e.showErrorAlert("Field size must be positive integers");
		}
		
	}
	
	
	private void setStartCellHandler(ActionEvent event) {
		String startRowIdx = startCellRowIdxText.getText();
		String startColIdx = startCellColIdxText.getText();
		try {	
			int rowIdx = CustomIntegerParser.parseInteger(startRowIdx, field.getNumRows());
			int colIdx = CustomIntegerParser.parseInteger(startColIdx, field.getNumColumns());
			
			if(field.getStartCell() != null) {
				field.getStartCell().setCellType(CellType.DEFAULT);
			}
			
			if(field.getCell(rowIdx, colIdx) == field.getEndCell()) {
				field.setEndCell(null);
			}
			field.setStartCell(field.getCell(rowIdx, colIdx));
			model.setStartCell(new Cell(CellType.START, rowIdx, colIdx));
		} catch (InputNumberException e) {
			e.showErrorAlert("Start cell coordinates must be positive integers");
		} catch (OutOfFieldException e) {
			e.showErrorAlert("Start Сell must be within the field");
		} 
		
	}
	
	private void saveEndCellHandler(ActionEvent event) {
		String endRowIdx = endCellRowIdxText.getText();
		String endColIdx = endCellColIdxText.getText();
		try {	
			int rowIdx = CustomIntegerParser.parseInteger(endRowIdx, field.getNumRows());
			int colIdx = CustomIntegerParser.parseInteger(endColIdx, field.getNumColumns());
			
			if(field.getEndCell() != null) {
				field.getEndCell().setCellType(CellType.DEFAULT);
			}
			
			if(field.getCell(rowIdx, colIdx) == field.getStartCell()) {
				field.setStartCell(null);
			}
			
			field.setEndCell(field.getCell(rowIdx, colIdx));
			model.setEndCell(new Cell(CellType.END, rowIdx, colIdx));
		} catch (InputNumberException e) {
			e.showErrorAlert("End Сell coordinates must be positive integers");
		} catch (OutOfFieldException e) {
			e.showErrorAlert("End Сell must be within the field");
		}
	}
	
	
	
	private void euclidianDistanceHandler(ActionEvent event) {
		setHeuristicButton.setText("Euclidian Distance");
		model.setHeuristic(Heuruistics.EUCLIDIANDISTANCE);
	}
	
	private void manhattanDistanceHandler(ActionEvent event) {
		setHeuristicButton.setText("Manhattan Distance");
		model.setHeuristic(Heuruistics.MANHATTANDISTANCE);
	}
	
	private void diagonalDistanceHandler(ActionEvent event) {
		setHeuristicButton.setText("Diagonal Distance");
		model.setHeuristic(Heuruistics.DIAGONALDISTANCE);
	}
	
	private void completeOneStepAlgHandler(ActionEvent event) {
		try {	
			if(isFirstStep) {
				model.prepareToStart();
				isFirstStep = false;
			}
	
			Pair<Integer, Integer> transitionCellCoords =  model.Astar();
			
			if (transitionCellCoords == null) {
				totalWayLabel.setText("Total length: path not found");
				executeCompletelyButton.setDisable(true);
				completeOneStepAlgButton.setDisable(true);
				
			} else if (transitionCellCoords.getKey() == model.getEndCell().getRowIdx() 
					&& transitionCellCoords.getValue() == model.getEndCell().getColIdx()) {
				Pair<Integer, ArrayList<Cell>> result = model.makeResult();
				totalWayLabel.setText("Total way: " + result.getKey().toString());
				for(Cell cell: result.getValue()) {
					if(cell != model.getEndCell() && cell != model.getStartCell()) {
						field.getCell(cell.getRowIdx(), cell.getColIdx()).setCellType(CellType.RESULT);					
					}
				}
				
				executeCompletelyButton.setDisable(true);
				completeOneStepAlgButton.setDisable(true);
			} else {
				field.getCell(transitionCellCoords.getKey(), transitionCellCoords.getValue()).setCellType(CellType.CHECKED);			
			}
			isPossibleToUnblock = false;
			setFieldSizeButton.setDisable(true);
			numberOfColumnsText.setDisable(true);
			numberOfRowsText.setDisable(true);
			startCellColIdxText.setDisable(true);
			startCellRowIdxText.setDisable(true);
			setStartCellButton.setDisable(true);
			endCellColIdxText.setDisable(true);
			endCellRowIdxText.setDisable(true);
			setEndCellButton.setDisable(true);
			setHeuristicButton.setDisable(true);
			
			
		} catch(InitializeException e) {
			e.showErrorAlert("Initialize error.\nBefore starting the algorithm, it is necessary to initialize the size of the field, the starting cell, the end cell and heuristic");
		}
		
	}
	
	private void executeCompletelyHandler(ActionEvent event) {
		try {		
			model.prepareToStart();
			isFirstStep = false;
			Pair<Integer, Integer> transitionCellCoords = model.Astar();
		
			
			while(transitionCellCoords != null && !(transitionCellCoords.getKey() == model.getEndCell().getRowIdx() 
					&& transitionCellCoords.getValue() == model.getEndCell().getColIdx())) {
				field.getCell(transitionCellCoords.getKey(), transitionCellCoords.getValue()).setCellType(CellType.CHECKED);
				transitionCellCoords = model.Astar();
			}
			
			if (transitionCellCoords == null) {
				totalWayLabel.setText("Total length: path not found");
			} else {
				Pair<Integer, ArrayList<Cell>> result = model.makeResult();
				totalWayLabel.setText("Total length: " + result.getKey().toString());
				for(Cell cell: result.getValue()) {
					if(cell != model.getEndCell() && cell != model.getStartCell()) {
						field.getCell(cell.getRowIdx(), cell.getColIdx()).setCellType(CellType.RESULT);					
					}
				}
			}
			
			isPossibleToUnblock = false;
			setFieldSizeButton.setDisable(true);
			numberOfColumnsText.setDisable(true);
			numberOfRowsText.setDisable(true);
			startCellColIdxText.setDisable(true);
			startCellRowIdxText.setDisable(true);
			setStartCellButton.setDisable(true);
			endCellColIdxText.setDisable(true);
			endCellRowIdxText.setDisable(true);
			setEndCellButton.setDisable(true);
			setHeuristicButton.setDisable(true);
			executeCompletelyButton.setDisable(true);
			completeOneStepAlgButton.setDisable(true);
		} catch(InitializeException e) {
			e.showErrorAlert("Initialize error.\nBefore starting the algorithm, it is necessary to initialize the size of the field, the starting cell, the end cell and heuristic");
		}
		
		
	}
	
	private void extractFieldHandler(ActionEvent event) {
		try {	
			AbstractReader reader = new FileDataReader();
			field.clear();
			model.clear();
			fieldPane.getChildren().clear();
			reader.getData();
			field.setFieldSize(reader.getFieldSize());
			model.setFieldSize(reader.getFieldSize());
			field.setField(fieldPane, reader.getField());	
			model.setField(reader.getField());
			model.setHeuristic(reader.getHeuruistic());
			
			isFirstStep = true;
			isPossibleToUnblock = true;
			numberOfColumnsText.setDisable(false);
			numberOfRowsText.setDisable(false);
			setFieldSizeButton.setDisable(false);
			startCellColIdxText.setDisable(false);
			startCellRowIdxText.setDisable(false);
			setStartCellButton.setDisable(false);
			endCellColIdxText.setDisable(false);
			endCellRowIdxText.setDisable(false);
			setEndCellButton.setDisable(false);
			completeOneStepAlgButton.setDisable(false);
			executeCompletelyButton.setDisable(false);
			setHeuristicButton.setDisable(false);
			setHeuristicButton.setText("Click to choose");
			totalWayLabel.setText("Total length:");
			numberOfColumnsText.setText(null);
			numberOfRowsText.setText(null);
			startCellColIdxText.setText(null);
			startCellRowIdxText.setText(null);
			endCellColIdxText.setText(null);
			endCellRowIdxText.setText(null);
		} catch(InputNumberException e) {
			e.showErrorAlert("Error while reading file");
		} catch(ReadFromFileException e) {
			e.showErrorAlert("Error while reading file");
		} catch(NullPointerException e) {
			System.out.println("File not selected");
		}
	}
	
	private void saveFieldToFileHandler(ActionEvent event) {
		try {	
			AbstractWriter writer = new FileDataWriter();
			writer.setFieldSize(field.getFieldSize());
			writer.setHeuristic(model.getHeuristic());
			writer.setField(field.getField());
			writer.setData();
		} catch (NullPointerException e) {
			System.out.println("File not selected");
		} catch(WriteToFileException e) {
			e.showErrorAlert("Error while save to file");
		}
	}
	
	private void startOverHandler(ActionEvent event) {
		
		if(field != null && model != null) {
			field.clear();
			fieldPane.getChildren().clear();
			model.clear();
			isFirstStep = true;
			isPossibleToUnblock = true;
			numberOfColumnsText.setDisable(false);
			numberOfRowsText.setDisable(false);
			setFieldSizeButton.setDisable(false);
			startCellColIdxText.setDisable(false);
			startCellRowIdxText.setDisable(false);
			setStartCellButton.setDisable(false);
			endCellColIdxText.setDisable(false);
			endCellRowIdxText.setDisable(false);
			setEndCellButton.setDisable(false);
			numberOfColumnsText.setText(null);
			numberOfRowsText.setText(null);
			startCellColIdxText.setText(null);
			startCellRowIdxText.setText(null);
			endCellColIdxText.setText(null);
			endCellRowIdxText.setText(null);
			completeOneStepAlgButton.setDisable(false);
			executeCompletelyButton.setDisable(false);
			setHeuristicButton.setDisable(false);
			setHeuristicButton.setText("Click to choose");
			totalWayLabel.setText("Total length:");
		}
		
	}
	
}
