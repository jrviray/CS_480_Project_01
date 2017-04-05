package cpp.edu.cs480.project06;
/**
 * Created by xinyuan_wang on 4/3/17.
 */


import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
     * This is the main Pane where the animation of RB Tree happens
     */
    private Pane mainPane;

    private Pane linkPane;


    private TextField inputValue;

    private BorderPane rootPane;


    private TextArea outputArea;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        initialize();
        primaryStage.setTitle("RedBlackTreeVisualization");
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
        //done setting up the top part of the UI

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

        linkPane= new Pane();
        StackPane stackPane= new StackPane();
        stackPane.getChildren().addAll(linkPane,mainPane);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(stackPane);
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
            GraphicNode newNode = new GraphicNode(40f,40f,key);
            //the Node is ready on the left top corner, then draw the node
            drawNode(newNode);

            //here inform the backend to do the insertion and ask a node to return

            //here begins the animation
            addNodeAnimation(newNode);
            outputString("adding "+key+" to the tree");

        }
        catch (NumberFormatException e)
        {
            outputString("Invalid Input!");
        }

    }


    /**
     * This method is handling the animation for adding new Node
     * @param currentNode
     */
    private void addNodeAnimation(GraphicNode currentNode)
    {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(3f),currentNode.circle);
        tt.setToX(mainPane.getWidth()/2);
        tt.setOnFinished(event -> {fixButton.setDisable(false);});
        tt.play();

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
     * If the node has null child(ren), it(they) will also be drawn.
     * The node only need to draw once
     * @param node
     */
    private void drawNode(GraphicNode node)
    {
        mainPane.getChildren().addAll(node.circle,node.keyText);
        linkPane.getChildren().addAll(node.leftLink,node.rightLink);
        if(node.leftChild instanceof GraphicNullNode)
            mainPane.getChildren().addAll(node.leftChild.circle,node.leftChild.keyText);
        if(node.rightChild instanceof GraphicNullNode)
            mainPane.getChildren().addAll(node.rightChild.circle,node.rightChild.keyText);
    }

}
