package com.example.paint;// Java program to create a menu bar and add
// menu to it and also add menuitems to menu

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;

public class PaintApplication extends Application {

    // launch the application
    public void start(Stage s)
    {
        // set title for the stage
        s.setTitle("DS Paint");

        final InputStream[] stream = {null};
        final String[] latestFilePath = {null};
        final double[] windowSize = {1000, 500};

        ColorPicker lineColorPicker = new ColorPicker();
        lineColorPicker.setValue(Color.BLACK);
        Slider lineSizeSlider = new Slider(0, 20, 0);
        lineSizeSlider.setShowTickLabels(true);
        lineSizeSlider.setShowTickMarks(true);
        lineSizeSlider.setMajorTickUnit(5);
        lineSizeSlider.setBlockIncrement(5);
        ToggleButton drawButton = new ToggleButton("Draw");

        Button clearCanvas = new Button("Clear");

        ColorPicker fillColorPicker = new ColorPicker();
        fillColorPicker.setValue(Color.BLACK);
        Button fillButton = new Button("Fill");

        BorderPane borderPane = new BorderPane();
        StackPane stackPane = new StackPane();
        ScrollPane scrollPane = new ScrollPane();
        Canvas mainDisplay = new Canvas(windowSize[0],windowSize[1]);
        GraphicsContext graphicsContext = mainDisplay.getGraphicsContext2D();

        mainDisplay.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){

                    public void handle(MouseEvent event) {
                        if(drawButton.isSelected()) {
                            graphicsContext.setStroke(lineColorPicker.getValue());
                            graphicsContext.setLineWidth(lineSizeSlider.getValue());
                            graphicsContext.beginPath();
                            graphicsContext.moveTo(event.getX(), event.getY());
                            graphicsContext.stroke();
                        }
                    }
                });

        mainDisplay.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>(){

                    public void handle(MouseEvent event) {
                        if(drawButton.isSelected()) {
                            graphicsContext.lineTo(event.getX(), event.getY());
                            graphicsContext.stroke();
                        }
                    }
                });

        mainDisplay.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>(){

                    public void handle(MouseEvent event) {
                        if(drawButton.isSelected()) {

                        }

                    }
                });

        borderPane.setCenter(scrollPane);
        scrollPane.setContent(stackPane);
        stackPane.getChildren().add(mainDisplay);

        // create a menu
        Menu File = new Menu("File");
        Menu Help = new Menu("Help");

        // create a toolbar
        ToolBar toolBar = new ToolBar();



        // create menuitems
        MenuItem OpenOp = new MenuItem("Open");
        MenuItem SaveOp = new MenuItem("Save");
        MenuItem SaveAsOp = new MenuItem("Save As");
        MenuItem CloseOp = new MenuItem("Close");
        MenuItem AboutOp = new MenuItem("About");
        SeparatorMenuItem sep = new SeparatorMenuItem();

        // add menu items to menu
        File.getItems().add(OpenOp);
        File.getItems().add(SaveOp);
        File.getItems().add(SaveAsOp);
        File.getItems().add(3, sep);
        File.getItems().add(CloseOp);
        Help.getItems().add(AboutOp);
        OpenOp.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        SaveOp.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        OpenOp.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent e) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Resource File");
                    fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Other", "*.*"),
                            new FileChooser.ExtensionFilter("PNG", "*.png"),
                            new FileChooser.ExtensionFilter("JPG", "*.jpg"));
                    File file = fileChooser.showOpenDialog(s);
                    latestFilePath[0] = file.getAbsolutePath();
                    try {
                        stream[0] = new FileInputStream(file);
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    Image image = new Image(stream[0]);
                    mainDisplay.setWidth(image.getWidth());
                    mainDisplay.setHeight(image.getHeight());
                    graphicsContext.drawImage(image,0,0);


                }
            });
        SaveAsOp.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Image");
                    fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("Other", "*.*"));
                    WritableImage writableImage = new WritableImage((int) mainDisplay.getWidth(), (int) mainDisplay.getHeight());
                    mainDisplay.snapshot(null, writableImage);
                    File file = fileChooser.showSaveDialog(s);
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                        latestFilePath[0] = file.getAbsolutePath();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            });

        SaveOp.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent e) {
                    WritableImage writableImage = new WritableImage((int) mainDisplay.getWidth(), (int) mainDisplay.getHeight());
                    mainDisplay.snapshot(null, writableImage);
                    File file = new File(latestFilePath[0]);
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
            });
        CloseOp.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent e) {
                    System.exit(0);
                }
            });

        AboutOp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                launchAbout(s);
            }
        });

        clearCanvas.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                graphicsContext.clearRect(0, 0, mainDisplay.getWidth(), mainDisplay.getHeight());
            }
        });
        fillButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                fillDrawing(graphicsContext, fillColorPicker.getValue());
            }
        });

        // Toolbar buttons
        Separator separator1 = new Separator(Orientation.VERTICAL);
        Separator separator2 = new Separator(Orientation.VERTICAL);
        Label drawLabel = new Label("Draw Tools");
        Label fillLabel = new Label("Fill Tools");
        toolBar.getItems().addAll(clearCanvas,
                separator1,
                drawLabel,
                drawButton,
                lineColorPicker,
                lineSizeSlider,
                separator2,
                fillLabel,
                fillButton,
                fillColorPicker);



        // create a menubar
        MenuBar menuBar = new MenuBar();

        // add menu to menubar
        menuBar.getMenus().add(File);
        menuBar.getMenus().add(Help);


        // create a VBox
        VBox top = new VBox(menuBar);
        top.getChildren().add(toolBar);

        borderPane.setTop(top);

        // create a scene
        Scene scene = new Scene(borderPane, windowSize[0], windowSize[1]);

        // set the scene
        s.setScene(scene);

        s.show();

    }

        public static void main(String[] args)
    {
        // launch the application
        launch(args);
    }

    private void fillDrawing(GraphicsContext gc, Color color){
        gc.setFill(color);
        gc.fill();

    }

    private void launchAbout(Stage s){
        final Stage dialog = new Stage();
        dialog.setTitle("About DS Paint");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(s);
        GridPane gridPane = new GridPane();
        Label labelTitle = new Label("What DS Paint tools do?");
        Label labelClear = new Label("Clear:");
        Label whatItDoClear = new Label("Will completely reset the canvas (This includes any opened images");
        Label labelDraw = new Label("Draw:");
        Label whatItDoDraw = new Label("Toggles the ability to draw on the canvas with the users mouse");
        Label labelFill = new Label("Fill:");
        Label whatItDoFill = new Label("Will fill the last shape drawn with the Draw tool");
        VBox dialogVbox = new VBox(20);
        gridPane.add(labelTitle, 0, 0, 2, 1);
        gridPane.add(labelClear, 0, 1, 2, 1);
        gridPane.add(whatItDoClear, 2, 1, 2, 1);
        gridPane.add(labelDraw, 0, 2, 2, 1);
        gridPane.add(whatItDoDraw, 2, 2, 2, 1);
        gridPane.add(labelFill, 0, 3, 2, 1);
        gridPane.add(whatItDoFill, 2, 3, 2, 1);
        gridPane.setVgap(10);
        dialogVbox.getChildren().add(gridPane);
        Scene dialogScene = new Scene(dialogVbox, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }


}

