package cpp.edu.cs480.project06;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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


public class GUI extends Application {

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
    
    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        initialize();
        primaryStage.setTitle("Red/Black Tree Visualization");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        test();

    }

    /**
     * This method initialize the user interface of this application
     */
    private void initialize()
    {
        rootPane=new BorderPane();

        mainScene = new Scene(rootPane);


        inputValue=new TextField();
        inputValue.setPrefWidth(50f);

        addButton=new Button("Add");
        addButton.setOnMouseClicked((event -> addNodeToTree()));
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
    }

    /**
     * This a temporary method for testing
     */
    private void test()
    {

    }


    /**
     * This method will be called when the add button is clicked
     */
    private void addNodeToTree()
    {
        try{
            int key=Integer.parseInt(inputValue.getText());
            // if the input is not an integer,NumberFormatException will be handled
            addButton.setDisable(true);
            deleteButton.setDisable(true);
            // the add button and delete button is disable before the animation ends
            GraphicNode newNode = new GraphicNode(40f,40f,String.format("%04d",key));
            newNode.setColor(GraphicNode.RED);
            //the Node is ready on the left top corner, then draw the node
            drawNode(newNode);

            //here inform the backend to do the insertion and ask a node to return

            //here begins the animation
            moveNodeAnimation(newNode,mainPane.getWidth()/2,
                    newNode.getY(),
                    3f,
                    actionEvent -> {fixButton.setDisable(false);
                        drawNullNode(newNode);});
            outputString("adding "+key+" to the tree");

        }
        catch (NumberFormatException e)
        {
            outputString("Invalid Input!");
        }

    }

    
    
    /**
     * This method is used to reset the output text on the bottom of the application
     */
    private void resetOutputArea()
    {
        outputArea.setText("Output:\n");
    }

    /**
     * This method is used to output a new line of string in the bottom of the application
     */
    private void outputString(String output)
    {

        outputArea.setText(outputArea.getText()+output+"\n");
        outputArea.positionCaret(outputArea.getText().length());

    }

    /**
     * This method is used to draw a node on the graphic interface.
     * It also prepare the links of this node.
     * @param node
     */
    private void drawNode(GraphicNode node)
    {
        mainPane.getChildren().add(node);
        node.toFront();
    }


    /**
     * This method is used to draw the null children with animation of a node
     * if one has but not being drawn yet
     * @param node
     */
    private void drawNullNode(GraphicNode node)
    {
        boolean hasNull=false;
        //if there is a null node to draw, this value set true and output such action to user

        //check if there is a null node in the left but not draw
        if(node.checkLeftNullNode()) {
            hasNull=true;
            drawNode(node.getLeftChildNode());
            //do the animation
            moveNodeAnimation(node.getLeftChildNode(),
                    node.getX()-GraphicNode.RADIUS*1.5,
                    node.getY()+GraphicNode.RADIUS*2,
                    2f,
                    actionEvent -> {node.getLeftChildNode().bindCurrentParent();});

        }

        //check if there is a null node in the right but not draw
        if(node.checkRightNullNode()) {
            hasNull=true;
            drawNode(node.getRightChildNode());
            moveNodeAnimation(node.getRightChildNode(),
                    node.getX()+GraphicNode.RADIUS*1.5,
                    node.getY()+GraphicNode.RADIUS*2,
                    2f,
                    actionEvent -> {node.getRightChildNode().bindCurrentParent();});
        }
        if(hasNull)
            outputString("Fill up null node");

    }

    /**
     * This method is for movement animation for {@link GraphicNode}. Make sure the sourceNode
     * is not binding with its parent before calling this method.
     * @param sourceNode
     *                  The Node that is going to make the movement
     * @param targetX
     *                  The x value of target position
     * @param targetY
     *                  The y value of target position
     * @param second
     *                  The time it takes to finish the animation in second
     * @param afterEvent
     *                  The event happens after the animation done
     */
    private void moveNodeAnimation(GraphicNode sourceNode, double targetX,
                                   double targetY, double second,
                                   EventHandler<ActionEvent> afterEvent)
    {
        
        TranslateTransition tt = new TranslateTransition(Duration.seconds(second),sourceNode.getCircle());
        tt.setToX(targetX);
        tt.setToY(targetY);
        tt.setOnFinished(afterEvent);
        tt.play();
    }




}
