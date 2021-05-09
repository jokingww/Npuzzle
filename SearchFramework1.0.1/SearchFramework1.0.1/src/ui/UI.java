package ui;

import java.util.Deque;

import core.problem.Problem;
import core.problem.State;
import core.runner.HeuristicType;
import core.solver.Node;
import g04.problem.npuzzle.Puzzle;
import g04.problem.npuzzle.PuzzleState;
import g04.solver.heuristic.IDAStarSearch;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UI extends Application {

	private Label N = new Label(Constrants.N);
	private Label ref = new Label(Constrants.REF);
	private Label mode = new Label(Constrants.MODE);
	private String myMode = "0";
	private Label initial = new Label(Constrants.INITIALSTATE);
	private Label goal = new Label(Constrants.GOALSTATE);
	private ChoiceBox cbN = new ChoiceBox(FXCollections.observableArrayList(
			"8-Puzzle", "15-Puzzle")
			);
	private TextField refText = new TextField();
	private ChoiceBox cbMode = new ChoiceBox(FXCollections.observableArrayList(
			"????????", "???????")
			);
	private TextField initialSequenceText = new TextField();
	private TextField goalSequenceText = new TextField();
	private TextArea process = new TextArea("");
	private Label newTip = new Label("");
	private Label ingTip = new Label("");
	private Label finTip = new Label("");
	private Button generate;
	private Button search;
	private PuzzleModel puzzleModel;
	private int SIZE = 3;
	private int FIELD_SIZE_PIXELS = 4*80/SIZE;
	private StackPane[][] gameRec = new StackPane[SIZE][SIZE];
	private GridPane gamePane;

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(10, 15, 10, 15));

		cbN.setValue("8-Puzzle");
		cbMode.setValue("????????");

		N.getStyleClass().add("text");
		ref.getStyleClass().add("text");
		mode.getStyleClass().add("text");
		initial.getStyleClass().add("text");
		goal.getStyleClass().add("text");
		newTip.getStyleClass().add("text");
		ingTip.getStyleClass().add("text");
		finTip.getStyleClass().add("text");
		newTip.setWrapText(true);
		ingTip.setWrapText(true);
		finTip.setWrapText(true);

		GridPane header = new GridPane();
		header.setPadding(new Insets(0, 0, 20, 0));
		header.setHgap(30);
		header.setVgap(8);

		header.add(N, 0, 0);
		cbN.getStyleClass().add("top_choicebox");
		header.add(cbN, 1, 0);
		refText.setText("100");
		header.add(ref, 2, 0);
		header.add(refText, 3, 0);
		header.add(mode, 0, 1);
		cbMode.getStyleClass().add("top_choicebox");
		header.add(cbMode, 1, 1);
		initialSequenceText.setPromptText("????????Puzzle????");
		header.add(initial, 2, 1);
		header.add(initialSequenceText, 3, 1);
		goalSequenceText.setPromptText("?????????Puzzle????");
		header.add(goal, 2, 2);
		header.add(goalSequenceText, 3, 2);

		initial.setVisible(true);
		goal.setVisible(true);
		initialSequenceText.setVisible(true);
		goalSequenceText.setVisible(true);


		gamePane = new GridPane();
		gamePane.setPadding(new Insets(5));


		//gamePane.setPadding(new Insets(5, 5, 5, 5));
		puzzleModel = new PuzzleModel(SIZE);
		init(gamePane, puzzleModel);

		VBox showProcess = new VBox();
		showProcess.setSpacing(5);
		showProcess.getStyleClass().add("right");
		showProcess.setPadding(new Insets(5, 5, 5, 5));
		showProcess.setPrefSize(235,1000);
		showProcess.getChildren().add(newTip);
		showProcess.getChildren().add(ingTip);
		ScrollPane scro = new ScrollPane();
		scro.setContent(process);
		process.setPrefColumnCount(15);
		process.setPrefHeight(265);
		showProcess.getChildren().add(scro);
		showProcess.getChildren().add(finTip);


		HBox bottom = new HBox();
		//hbox.setLayoutX(20);
		//hbox.setLayoutY(20);
		generate = new Button();
		generate.setText(Constrants.BTN_GEN);
		generate.getStyleClass().add("button");
		search = new Button();
		search.setText(Constrants.BTN_ST);
		search.getStyleClass().add("button");
		bottom.getChildren().add(generate);
		bottom.getChildren().add(search);
		bottom.setSpacing(30);

		cbN.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue arg0, Object oldValue, Object newValue) {
				System.out.println(newValue.toString());
				if(newValue.toString().equals("0")) {
					SIZE = 3;
					FIELD_SIZE_PIXELS = 4*80/SIZE;
				}
				else if(newValue.toString().equals("1")) {
					SIZE = 4;
					FIELD_SIZE_PIXELS = 4*80/SIZE;
				}
				gameRec = new StackPane[SIZE][SIZE];
				if(myMode.equals("0")) {
					newTip.setText(Constrants.HINT_NEW);
					ingTip.setText("");

					Task task = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							updateMessage("");
							process.setText("");
							finTip.setText("");
							return null;
						}
					};
					process.textProperty().bind(task.messageProperty());
					finTip.textProperty().bind(task.messageProperty());
					gamePane.getChildren().clear();
					puzzleModel = new PuzzleModel(SIZE);
					init(gamePane, puzzleModel);
				}
			}
		});

		cbMode.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue arg0, Object oldValue, Object newValue) {
				System.out.println(newValue.toString());
				myMode = newValue.toString();
				if(myMode.equals("1")) {
					initial.setVisible(false);
					goal.setVisible(false);
					initialSequenceText.setVisible(false);
					goalSequenceText.setVisible(false);
				}
				else {
					initial.setVisible(true);
					goal.setVisible(true);
					initialSequenceText.setVisible(true);
					goalSequenceText.setVisible(true);
					newTip.setText("");
					ingTip.setText("");

					Task task = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							updateMessage("");
							process.setText("");
							finTip.setText("");
							return null;
						}
					};
					process.textProperty().bind(task.messageProperty());
					finTip.textProperty().bind(task.messageProperty());
					gamePane.getChildren().clear();
					puzzleModel = new PuzzleModel(SIZE);
					init(gamePane, puzzleModel);
				}
			}
		});
		
		refText.setTextFormatter(new TextFormatter<String>(change -> {
			if(change.getText().matches("[0-9]*")) {
				return change;
			}
		    return null;
		}));
		
		initialSequenceText.setTextFormatter(new TextFormatter<String>(change -> {
			if(change.getText().matches("([0-9]| )*")) {
				return change;
			}
		    return null;
		}));
		
		goalSequenceText.setTextFormatter(new TextFormatter<String>(change -> {
			if(change.getText().matches("([0-9]| )*")) {
				return change;
			}
		    return null;
		}));

		generate.setOnMouseClicked(e->handleMouseClickGenerate(e, gamePane));
		search.setOnMouseClicked(e->handleMouseClickSearch(e,gamePane));



		/*
		flowPane.getChildren().add(root);
		flowPane.getChildren().add(gamePane);
		flowPane.getChildren().add(showProcess);
		flowPane.getChildren().add(hbox);
		 */
		borderPane.setTop(header);
		borderPane.setCenter(gamePane);
		borderPane.setRight(showProcess);
		borderPane.setBottom(bottom);

		stage.setTitle("N-Puzzle");
		Scene scene = new Scene(borderPane, 600, 500);
		scene.getStylesheets().add(getClass().getResource("puzzle.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void init(GridPane pane, final PuzzleModel model) {

		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {

				PuzzleFieldStyle style = model.getColorAt(x, y);

				gameRec[x][y] = new StackPane();
				final Rectangle rec= new Rectangle((FIELD_SIZE_PIXELS * x),(FIELD_SIZE_PIXELS * y),(FIELD_SIZE_PIXELS),(FIELD_SIZE_PIXELS));
				rec.getStyleClass().add(style.getColor());

				//rec.setText(num);

				if(style.getNumber()!=0) {
				final Text text = new Text(String.valueOf(style.getNumber()));
				text.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
				text.setFill(Color.WHITE);
				text.setStroke(Color.web("#7080A0"));
				gameRec[x][y].getChildren().addAll(rec,text);
				}
				
				else {
					gameRec[x][y].getChildren().addAll(rec);
				}
				//sPane.setAlignment(Pos.TOP_LEFT);
				pane.add(gameRec[x][y],y,x);

				/*
                    rec.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Point moveFromPoint = new Point((int) rec.getX() / FIELD_SIZE_PIXELS, (int) rec.getY() / FIELD_SIZE_PIXELS);
                            Point moveToPoint = model.moveToEmptyField(moveFromPoint);
                            if (moveToPoint != null) {
                                // Increase the counter
                                moveRectangle(moveToPoint, rec);
                                model.printBoards();
                                if (model.areBoardsEqual()) {
                                    youWinText.setVisible(true);
                                }
                            }
                        }
                    });
				 */
			}
		}
	}

	private void moveRectangle(StackPane empty, StackPane rec, String direc, Point pempty) {
		PuzzleFieldStyle style;
		if(direc=="N") {
			/*
			gamePane.getChildren().remove(rec);
			gamePane.getChildren().remove(empty);
			gamePane.add(rec, pempty.getY(), pempty.getX());
			gamePane.add(empty, pempty.getY(), pempty.getX()+1);
			 */

			gameRec[pempty.getX()][pempty.getY()] = rec;
			gameRec[pempty.getX()+1][pempty.getY()] = empty;
			style = puzzleModel.board[pempty.getX()][pempty.getY()];
			puzzleModel.board[pempty.getX()][pempty.getY()]=puzzleModel.board[pempty.getX()+1][pempty.getY()];
			puzzleModel.board[pempty.getX()+1][pempty.getY()]=style;
			puzzleModel.setEmptyPoint(new Point(pempty.getX()+1,pempty.getY()));
		}
		else if(direc=="S") {
			/*
			gamePane.getChildren().remove(rec);
			gamePane.getChildren().remove(empty);
			gamePane.add(rec, pempty.getY(), pempty.getX());
			gamePane.add(empty, pempty.getY(), pempty.getX()-1);
			 */

			gameRec[pempty.getX()][pempty.getY()] = rec;
			gameRec[pempty.getX()-1][pempty.getY()] = empty;
			style = puzzleModel.board[pempty.getX()][pempty.getY()];
			puzzleModel.board[pempty.getX()][pempty.getY()]=puzzleModel.board[pempty.getX()-1][pempty.getY()];
			puzzleModel.board[pempty.getX()-1][pempty.getY()]=style;
			puzzleModel.setEmptyPoint(new Point(pempty.getX()-1,pempty.getY()));
		}
		else if(direc=="W") {
			/*
			gamePane.getChildren().remove(rec);
			gamePane.getChildren().remove(empty);
			gamePane.add(rec, pempty.getY(), pempty.getX());
			gamePane.add(empty, pempty.getY()+1, pempty.getX());
			 */

			gameRec[pempty.getX()][pempty.getY()] = rec;
			gameRec[pempty.getX()][pempty.getY()+1] = empty;
			style = puzzleModel.board[pempty.getX()][pempty.getY()];
			puzzleModel.board[pempty.getX()][pempty.getY()]=puzzleModel.board[pempty.getX()][pempty.getY()+1];
			puzzleModel.board[pempty.getX()][pempty.getY()+1]=style;
			puzzleModel.setEmptyPoint(new Point(pempty.getX(),pempty.getY()+1));
		}
		else if(direc=="E") {
			/*
			gamePane.getChildren().remove(rec);
			gamePane.getChildren().remove(empty);
			gamePane.add(rec, pempty.getY(), pempty.getX());
			gamePane.add(empty, pempty.getY()-1, pempty.getX());
			 */

			gameRec[pempty.getX()][pempty.getY()] = rec;
			gameRec[pempty.getX()][pempty.getY()-1] = empty;
			style = puzzleModel.board[pempty.getX()][pempty.getY()];
			puzzleModel.board[pempty.getX()][pempty.getY()]=puzzleModel.board[pempty.getX()][pempty.getY()-1];
			puzzleModel.board[pempty.getX()][pempty.getY()-1]=style;
			puzzleModel.setEmptyPoint(new Point(pempty.getX(),pempty.getY()-1));
		}

		gamePane.getChildren().clear();
		init(gamePane,puzzleModel);
	}

	private void handleMouseClickGenerate(MouseEvent e, GridPane gamePane) {
		if(myMode.equals("1")) {
			gamePane.getChildren().clear();
			puzzleModel = new PuzzleModel(SIZE);
			puzzleModel.shuffleBoard();
			init(gamePane, puzzleModel);
		}
		else {
			String[] numbers = initialSequenceText.getText().replaceAll( "\\s+", " " ).split(" ");
			System.out.println(numbers.length);
			if(numbers.length!=9&&numbers.length!=16||goalSequenceText.getText().split(" ").length!=numbers.length) {
				Alert alert = new Alert(AlertType.INFORMATION);
	            alert.titleProperty().set("???");
	            alert.headerTextProperty().set("???????д???");
	            alert.showAndWait();
				
				return;
			}
			if(SIZE != Math.sqrt(numbers.length)) {
				SIZE = (int) Math.sqrt(numbers.length);
				FIELD_SIZE_PIXELS = 4*80/SIZE;
				puzzleModel = new PuzzleModel(SIZE);
				gameRec = new StackPane[SIZE][SIZE];
				if(SIZE==3) {
					cbN.setValue("8-Puzzle");
				}
				else {
					cbN.setValue("15-Puzzle");
				}
			}
			puzzleModel.setBoard(numbers);
			gamePane.getChildren().clear();
			init(gamePane, puzzleModel);
		}
		newTip.setText(Constrants.HINT_NEW);
		ingTip.setText("");

		Task task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				updateMessage("");
				process.setText("");
				finTip.setText("");
				return null;
			}
		};
		process.textProperty().bind(task.messageProperty());
		finTip.textProperty().bind(task.messageProperty());
	}
	
	private PuzzleState convert(String s) {
		String[] input = s.split(" ");
		int size = (int)Math.sqrt(input.length);
		int[] board = new int[size * size];
		for(int i=0;i<size * size;i++) {
				board[i]=Integer.parseInt(input[i]);
			}
		PuzzleState puzzleState = new PuzzleState(board,size);
		return puzzleState;
	}


	private void handleMouseClickSearch(MouseEvent e, GridPane gamePane) {
		String initial = initialSequenceText.getText();
		String goal = goalSequenceText.getText();
		PuzzleState initialState = convert(initial);
		PuzzleState goalState = convert(goal);
		//Problem problem = new Problem(initialState,goalState,SIZE);
		Puzzle puzzle = new Puzzle(initialState,goalState,SIZE);
		IDAStarSearch astar = new IDAStarSearch(PuzzleState.predictor(HeuristicType.MANHATTAN));
		Deque<Node> path = astar.search(puzzle);
		
		String answer = puzzle.returnSolution(path);
		//String answer = "S E N W S S E N W S W N N E E S W W S E E";
		//String answer = "N W S W S E E N W W S E E N W N W S S E N N E S W N W S E S E";
		//String answer = "N W S E E N N W S W N E S W S E E N W W S E E N N W W S S E E";
		//String answer = "W W S E S E N N W S S W N E S S E N N E N W W S S S E N N E N W W S S W S E E N W W S E E N W N E S E S";
		if(newTip.getText()=="") {
			newTip.setText(Constrants.HINT_NEW);
		}
		ingTip.setText(Constrants.HINT_ING);
		String[] directions = answer.split(" ");

		Service<String> service=new Service<String>() {
			@Override
			protected Task<String> createTask() {
				return new Task<String>() {
					@Override
					protected String call() throws Exception {
						for(int i=0;i<directions.length;i++) {

							//process.appendText("??"+counter+"??????"+direction+"\n");
							if(i==0) {
								updateValue("??????...\n"+"??"+(i+1)+"??????"+directions[i]+"\n");
							}
							else {
								updateValue(process.getText()+"??"+(i+1)+"??????"+directions[i]+"\n");
							}

							final int ii=i;
							Platform.runLater(() -> {
								process.positionCaret(process.getText().length());
								switch(directions[ii]) {
								case "N":
									moveRectangle(gameRec[puzzleModel.getEmptyPoint().getX()][puzzleModel.getEmptyPoint().getY()], gameRec[puzzleModel.getEmptyPoint().getX()-1][puzzleModel.getEmptyPoint().getY()],"S",puzzleModel.getEmptyPoint());
									break;
								case "E":
									moveRectangle(gameRec[puzzleModel.getEmptyPoint().getX()][puzzleModel.getEmptyPoint().getY()],gameRec[puzzleModel.getEmptyPoint().getX()][puzzleModel.getEmptyPoint().getY()+1],"W",puzzleModel.getEmptyPoint());
									break;
								case "S":
									moveRectangle(gameRec[puzzleModel.getEmptyPoint().getX()][puzzleModel.getEmptyPoint().getY()],gameRec[puzzleModel.getEmptyPoint().getX()+1][puzzleModel.getEmptyPoint().getY()],"N",puzzleModel.getEmptyPoint());
									break;
								case "W":
									moveRectangle(gameRec[puzzleModel.getEmptyPoint().getX()][puzzleModel.getEmptyPoint().getY()],gameRec[puzzleModel.getEmptyPoint().getX()][puzzleModel.getEmptyPoint().getY()-1],"E",puzzleModel.getEmptyPoint());
									break;
								default:
									break;
								}
							});


							Thread.sleep(Integer.parseInt(refText.getText())*10);
						}
						if(directions.length==0) {
							updateMessage(Constrants.HINT_FAIL);
						}
						else {
							updateMessage(Constrants.HINT_FIN+directions.length);
						}
						return process.getText()+"???????";
					}
				};
			}
		};
		process.textProperty().bind(service.valueProperty());
		//?????????????
		service.setOnSucceeded((WorkerStateEvent event) -> {
			System.out.println("??????????");
		});
		//????????start()?????????????
		service.start();
		finTip.textProperty().bind(service.messageProperty());
		/*
    if(directions.length==0) {
		finTip.setText(Constrants.HINT_FAIL);
	}
	else {
		finTip.setText(Constrants.HINT_FIN);
	}*/
	}

	public static void main(String[] args) {
		Application.launch(UI.class, args);
	}
}
