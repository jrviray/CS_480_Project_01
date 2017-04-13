/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.edu.cs480.project06;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Queue;

/**
 *
 * @author Hunter
 */
public class Controller extends Application{
    private Scene mainScene;
    private Pane mainPane;
    private double mainPaneWidth = 1600;
    private double mainPaneHeight = 600;
    private Button addButton, deleteButton, fixButton, saveButton, loadButton;
    private TextField inputValue;
    private BorderPane rootPane;
    private TextArea outputArea;
    private Animator animator;
    private RedBlackTree<Integer, Integer> tree;
    private DoubleProperty playRate;
    private boolean isNullVisible;
    private Animation thisAnimation;
    

    private void initialize() {
        rootPane = new BorderPane();
        mainScene = new Scene(rootPane);
        inputValue = new TextField();
        inputValue.setPrefWidth(50f);
        addButton = new Button("Add");
        //addButton on mouse action event
        addButton.setOnMouseClicked(event -> add());
        deleteButton = new Button("Delete");
        deleteButton.setOnMouseClicked(event -> delete());
        //delete button on mouse action event
        deleteButton.setDisable(true);
        fixButton = new Button("Fix");
        fixButton.setDisable(true);
        fixButton.setOnMouseClicked(event -> {thisAnimation.play();});
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
        rootPane.setTop(topPane);
        Slider rate = new Slider();
        rate.setMin(0);
        rate.setMax(4f);
        rate.setValue(1f);
        playRate = rate.valueProperty();

        CheckBox nullVisible = new CheckBox("display null leaves");
        nullVisible.setSelected(false);
        isNullVisible=false;
        nullVisible.selectedProperty().addListener(action->{isNullVisible=!isNullVisible;animator.setNullNodeVisible(isNullVisible);});

        leftPane.getChildren().addAll(inputLabel,inputValue,addButton,
                deleteButton,fixButton,rate,nullVisible);
        rightPane.getChildren().addAll(saveButton,loadButton);



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
        animator = new Animator(mainPane, isNullVisible,outputArea);
        tree = new RedBlackTree<Integer, Integer>();
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
    private Animation insert(int ID, int parentID, boolean leftRight) {
        //left is true, right is false
        if(leftRight) {
            return animator.insertLeftAnimation(ID, parentID);
        }
        else {
            return animator.insertRightAnimation(ID, parentID);
        }
    }
    private Animation insertRoot(int ID) {
        System.out.println(ID);
        return animator.insertRootAnimation(ID);
    }
    private Animation rotate(int ID, boolean leftRight) {
        if(leftRight) {
            return animator.rotateLeftAnimation(ID);
        }
        else {
            return animator.rotateRightAnimation(ID);
        }
    }
    private void playAnimation(Instruction input) {
        System.out.println(input);
        //PauseTransition is just so thisAnimation is initialized
        thisAnimation = new PauseTransition(Duration.ZERO);
        String thisInstruction = input.getInstruction();
        switch (thisInstruction) {
            case "add":
                if(input.getParentID() == null) {
                    thisAnimation = insertRoot((int)input.getID());                        
                }
                else {
                    thisAnimation = insert((int)input.getID(), (int)input.getParentID(), input.getLR());
                }   break;
            case "rotate":
                thisAnimation = rotate((int)input.getID(), input.getLR());
                break;
            case "recolor":
                //thisAnimation = recolor
                animator.recolor((int)input.getID(), input.getColor());
                break;
            case "swap":
                thisAnimation = animator.dataSwap((int)input.getNodeAID(), (int)input.getNodeBID());
                break;
            case "remove":
                    thisAnimation = animator.deleteAnimation((int)input.getID());
                break;
            default:
                System.out.println("Error in instruction class, instruction type unknown: " + input.getInstruction());
                break;
        }
        if(tree.info.isEmpty()) {
            thisAnimation.setOnFinished(event -> {addButton.setDisable(false);
                deleteButton.setDisable(false);});
            thisAnimation.rateProperty().bind(playRate);
            thisAnimation.play();
        }
        else {
            thisAnimation.setOnFinished(event -> playAnimation(tree.info.poll()));
            thisAnimation.rateProperty().bind(playRate);
            thisAnimation.play();
            if(thisInstruction.equals("rotate"))
            {
                thisAnimation.pause();
                fixButton.setDisable(false);
            }
        }
    }
    private void add() {
        try {
            //for Input validation
            //key = user input 
            //value = data = ID of node (hidden from user)
            int key = Integer.parseInt(inputValue.getText());
            

            //disable buttons before the animation ends
           int value = animator.generateNode(key);
            tree.add(key,value);
            playAnimation(tree.info.poll());
            addButton.setDisable(true);
            deleteButton.setDisable(true);
        } catch (NumberFormatException e) {
            outputString("Invalid Input! Please enter an integer!");
        }
    }
    private void delete() {
        try {
            int key = Integer.parseInt(inputValue.getText());
            tree.remove(key);
            playAnimation(tree.info.poll());
            addButton.setDisable(true);
            deleteButton.setDisable(true);
        } catch (NumberFormatException e) {
            outputString("Invalid Input! Please enter an Integer!");
        }
//        catch (RuntimeException e)
//        {
//            outputString(e.getMessage());
//        }
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