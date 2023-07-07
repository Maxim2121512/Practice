package application;


import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import Enums.CellType;
import Enums.Heuruistics;
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
	private Pane fieldPane;
	@FXML
	private Pane pane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private Button setStandartScaleButton;
	
	private Field field = new Field();
	
	
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
			} else if (cell.getCellType() == CellType.BLOCKED) {
				cell.setCellType(CellType.DEFAULT);
			}
		}
	}
	
	private void zoomHandler(ScrollEvent event) {
		Scale scale = new Scale();
		double scaleFactor = 1.1;
		System.out.println(fieldPane.getScaleX() + " " + fieldPane.getScaleY());
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
		field.clear();
		fieldPane.getChildren().clear();
		
		
		String rows = numberOfRowsText.getText();
		String columns = numberOfColumnsText.getText();
		
		field.setFieldSize(new Pair<>(Integer.parseInt(rows),  Integer.parseInt(columns)));
		field.setField(fieldPane, null);
		
	}
	
	
	private void setStartCellHandler(ActionEvent event) {
		String startRowIdx = startCellRowIdxText.getText();
		String startColIdx = startCellColIdxText.getText();
		
		
		int rowIdx = Integer.parseInt(startRowIdx);
		int colIdx = Integer.parseInt(startColIdx);
		
		if(field.getStartCell() != null) {
			field.getStartCell().setCellType(CellType.DEFAULT);
		}
		
		field.setStartCell(field.getCell(rowIdx, colIdx));
		
	}
	
	private void saveEndCellHandler(ActionEvent event) {
		String endRowIdx = endCellRowIdxText.getText();
		String endColIdx = endCellColIdxText.getText();
		
		int rowIdx = Integer.parseInt(endRowIdx);
		int colIdx = Integer.parseInt(endColIdx);
		
		if(field.getEndCell() != null) {
			field.getEndCell().setCellType(CellType.DEFAULT);
		}
		
		if(field.getCell(rowIdx, colIdx) == field.getStartCell()) {
			field.setStartCell(null);
		}
		
		field.setEndCell(field.getCell(rowIdx, colIdx));
	}
	
	
	
	private void euclidianDistanceHandler(ActionEvent event) {
		setHeuristicButton.setText("Euclidian Distance");
	}
	
	private void manhattanDistanceHandler(ActionEvent event) {
		setHeuristicButton.setText("Manhattan Distance");
	}
	
	private void diagonalDistanceHandler(ActionEvent event) {
		setHeuristicButton.setText("Diagonal Distance");
	}
	
	private void completeOneStepAlgHandler(ActionEvent event) {
		
	
	}
	
	private void executeCompletelyHandler(ActionEvent event) {
		
		
	}
	
	private void extractFieldHandler(ActionEvent event) {
		AbstractReader reader = new FileDataReader();
		reader.getData();
		field.setFieldSize(reader.getFieldSize());
		field.setField(fieldPane, reader.getField());	
	}
	
	private void saveFieldToFileHandler(ActionEvent event) {
		AbstractWriter writer = new FileDataWriter();
		writer.setFieldSize(field.getFieldSize());
		writer.setHeuristic(Heuruistics.MANHATTANDISTANCE);
		writer.setField(field.getField());
		writer.setData();
	}
	
	private void startOverHandler(ActionEvent event) {
		field.clear();
		fieldPane.getChildren().clear();
	}
	
}
