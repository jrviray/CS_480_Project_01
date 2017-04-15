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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        fixButton = new Button("Fix");
        fixButton.setDisable(true);
        fixButton.setOnMouseClicked(event -> {thisAnimation.play(); fixButton.setDisable(true);});
        saveButton = new Button("Save");
        saveButton.setOnMouseClicked(event -> saveTree());
        loadButton = new Button("Load");
        loadButton.setOnMouseClicked(event -> loadTree());
        
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


        mainPane=new Pane();
        mainPane.setPadding(new Insets(20,20,20,20));
        mainPane.setPrefHeight(mainPaneHeight);
        mainPane.setPrefWidth(mainPaneWidth);

        SplitPane container = new SplitPane(mainPane,outputArea);
        container.setOrientation(Orientation.VERTICAL);
        container.setDividerPositions(.8f,.2f);

        rootPane.setCenter(container);
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
        if(input.getError() != null) {
            outputString(input.getError());
        }
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

            Animation centerAdjustment = animator.centerAdjustment(tree.root.getData());
            centerAdjustment.setOnFinished(event -> {

                addButton.setDisable(false);
                deleteButton.setDisable(false);
                saveButton.setDisable(false);
                loadButton.setDisable(false);});
            thisAnimation.rateProperty().bind(playRate);
            thisAnimation.setOnFinished(actionEvent -> {centerAdjustment.play();});
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
            inputValue.clear();

            //disable buttons before the animation ends
           int value = animator.generateNode(key);
            tree.add(key,value);
            playAnimation(tree.info.poll());
            addButton.setDisable(true);
            deleteButton.setDisable(true);
            saveButton.setDisable(true);
            loadButton.setDisable(true);
        } catch (NumberFormatException e) {
            outputString("Invalid Input! Please enter an integer!");
        }
        catch (NullPointerException e)
        {
            outputString("Error!");
            e.printStackTrace();
            tree.info.clear();
        }
    }
    private void delete() {
        try {
            int key = Integer.parseInt(inputValue.getText());
            inputValue.clear();
            boolean hasKey = tree.remove(key);
            if(!hasKey)
                outputString("No such node found!");
            else {
                playAnimation(tree.info.poll());
                addButton.setDisable(true);
                deleteButton.setDisable(true);
                saveButton.setDisable(true);
                loadButton.setDisable(true);
            }
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

    private void saveTree(){
        TextInputDialog save = new TextInputDialog();
        save.setTitle("Save tree");
        save.setHeaderText("Save");
        save.setContentText("Please enter the name you want to save as:");
        Optional<String> saveName = save.showAndWait();
        if(saveName.isPresent())
        {
            try {
                SaveInfo data = new SaveInfo(animator.getHashTable(), this.tree,this.tree.root.getData());
                FileOutputStream fileSave = new FileOutputStream(saveName.get() + ".dat");
                ObjectOutputStream dataSave = new ObjectOutputStream(fileSave);
                dataSave.writeObject(data);
                dataSave.close();
                fileSave.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void loadTree()
    {
        List<String> loadableFileName = new ArrayList<>();
        File curDir = new File(".");
        File[] allFile=curDir.listFiles();

            for(File x:allFile)
            {
                if(x.getName().endsWith(".dat"))
                {
                   loadableFileName.add(x.getName().replace(".dat",""));
                }
            }

           if(loadableFileName.isEmpty())
           {
               Alert noFileFound = new Alert(Alert.AlertType.INFORMATION);
               noFileFound.setTitle("No match file found");
               noFileFound.setHeaderText(null);
               noFileFound.setContentText("No match file found!");
               noFileFound.showAndWait();
           }
           else
           {
               ChoiceDialog<String> load = new ChoiceDialog<>(loadableFileName.get(0),loadableFileName);
               load.setTitle("Load tree");
               load.setHeaderText("Load");
               load.setContentText("Please select a tree that you want to load:");
               Optional<String> fileName = load.showAndWait();

               if(fileName.isPresent())
               {
                   try {
                       //load the treeDataSave data;
                       FileInputStream fileSave = new FileInputStream(fileName.get() + ".dat");
                       ObjectInputStream dataSave = new ObjectInputStream(fileSave);
                       SaveInfo data = (SaveInfo) dataSave.readObject();
                       dataSave.close();
                       fileSave.close();
                       tree = data.getTree();
                       animator.loadTree(data.getTable(),data.getRootID());
                   }
                   catch (FileNotFoundException e)
                   {
                       e.printStackTrace();
                   }
                   catch (IOException e)
                   {
                       e.printStackTrace();
                   }
                   catch (ClassNotFoundException e)
                   {
                       e.printStackTrace();
                   }
               }
           }



    }
}