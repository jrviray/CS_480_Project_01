/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.edu.cs480.project06;

import java.util.logging.Level;
import java.util.logging.Logger;
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
public class testController extends Application{
    
    testAnimator myTest;
    
    private Scene mainScene;

    private double mainPaneWidth= 1200;

    private double mainPaneHeight = 600;

    private Button addButton,
            deleteButton,
            fixButton,
            saveButton,
            loadButton;

    /**
     * This is the main Pane where the animation of RB cpp.edu.cs480.project06.Tree happens
     */
    private Pane mainPane;

    private TextField inputValue;

    private BorderPane rootPane;


    private TextArea outputArea;
    
    public testController() {
        
    }
    public testController(String[] args) {
        launch(args);
    }
    public void tester() throws Exception{
        myTest = new testAnimator(this.mainPane);
        GraphicNode myNode = new GraphicNode(40f,40f,String.format("test1"));
        GraphicNode node2 = new GraphicNode(20f,40f, "test");
        myTest.drawNode(myNode);
        myTest.drawNode(node2);
          
    }
    private void initialize() {
        rootPane=new BorderPane();

        mainScene = new Scene(rootPane);


        inputValue=new TextField();
        inputValue.setPrefWidth(50f);

        addButton=new Button("Add");
        addButton.setOnMouseClicked((event -> {
            try {
                tester();
            } catch (Exception ex) {
                System.out.println("Welp");
            }
        }));
        // add an action on add button
        deleteButton=new Button("Delete");
        deleteButton.setDisable(true);
        fixButton= new Button("Fix");
        fixButton.setDisable(true);
        saveButton=new Button("Save tree");
        loadButton=new Button("Load tree");

        Label inputLabel= new Label("Integer input:");
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
        //resetOutputArea();
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
    }
    @Override
    public void start(Stage primaryStage) throws Exception{

        initialize();
        primaryStage.setTitle("Red/Black Tree Visualization");
        primaryStage.setScene(mainScene);
        primaryStage.show();
 
    }
    
    public static void main(String[] args) {
        testController a = new testController(args);
    }
}
