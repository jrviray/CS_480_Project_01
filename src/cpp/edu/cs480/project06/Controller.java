/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.edu.cs480.project06;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Hunter
 */
public class Controller extends Application{
    private Scene mainScene;
    private Pane mainPane;
    private double mainPaneWidth = 1200;
    private double mainPaneHeight = 600;
    private Button addButton, deleteButton, fixButton, saveButton, loadButton;
    private TextField inputValue;
    private BorderPane rootPane;
    private TextArea outputArea;
    private Animator animator;
    private RedBlackTree<Integer, String> tree;
    
    
    
    
    
    
    private void initialize() {
        rootPane = new BorderPane();
        mainScene = new Scene(rootPane);
        inputValue = new TextField();
        inputValue.setPrefWidth(50f);
        addButton = new Button("Add");
        //addButton on mouse action event
        addButton.setOnMouseClicked((event -> add()));
        deleteButton = new Button("Delete");
        //delete button on mouse action event
        deleteButton.setDisable(true);
        fixButton = new Button("Fix");
        fixButton.setDisable(true);
        saveButton = new Button("Save");
        saveButton.setDisable(true);
        loadButton = new Button("Load");
        
        Label inputLabel = new Label ("Integer Input:");
        inputLabel.setFont(Font.font(15f));
        
        HBox leftPane = new HBox(20f);
        leftPane.setPadding(new Insets(20f,20f,20f,20f));
        leftPane.setAlignment(Pos.BASELINE_LEFT);
        HBox rightPane = new HBox(20f);
        rightPane.setPadding(new Insets(20f,20f,20f,20f));
        rightPane.setAlignment(Pos.BASELINE_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(leftPane);
        topPane.setRight(rightPane);

        leftPane.getChildren().addAll(inputLabel,inputValue,addButton,
                deleteButton,fixButton);
        rightPane.getChildren().addAll(saveButton,loadButton);

        rootPane.setTop(topPane);
        //done setting up the top part of the cpp.edu.cs480.project06.UI

        outputArea = new TextArea();
        outputArea.setFont(Font.font(16));
        resetOutputArea();
        outputArea.setEditable(false);
        ScrollPane bottomScrollPane = new ScrollPane(outputArea);
        bottomScrollPane.setFitToWidth(true);
        bottomScrollPane.setFitToHeight(true);
        bottomScrollPane.setPrefHeight(100);
        rootPane.setBottom(bottomScrollPane);
        bottomScrollPane.setVvalue(1);

        mainPane=new Pane();
        mainPane.setPadding(new Insets(20,20,20,20));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(mainPaneHeight);
        scrollPane.setPrefViewportWidth(mainPaneWidth);
        rootPane.setCenter(scrollPane);
        animator = new Animator(mainPane);
        tree = new RedBlackTree<Integer, String>();
    }
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        initialize();
        primaryStage.setTitle("RedBlack Tree Visualization");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
    private void add() {
        try {
            //for Input validation
            int value = Integer.parseInt(inputValue.getText());
            
            addButton.setDisable(true);
            deleteButton.setDisable(true);
            //disable buttons before the animation ends
            
            
            //begin flow process:
            //
            int key = animator.generateNode(value);
            tree.add(key, Integer.toString(value));
            
        } catch (NumberFormatException e) {
            outputString("Invalid Input! Please enter an Integer!");
        }
    }
    
    
    
    
    
    
    
    
    private void outputString(String output)
    {

        outputArea.setText(outputArea.getText()+output+"\n");
        outputArea.positionCaret(outputArea.getText().length());

    }
    private void resetOutputArea()
    {
        outputArea.setText("Output:\n");
    }
}
