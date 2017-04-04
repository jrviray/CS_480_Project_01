package cpp.edu.cs480.project01;/**
 * Created by xinyuan_wang on 4/3/17.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application {

    private Scene mainScene;

    final private double APP_WITDTH = 1000;

    final private double APP_HEIGHT = 1000;

    private Button inserButton,
            deleButton,
            fixButton,
            saveButton,
            loadButton;

    /**
     * This is the main Pane where the animation of RB Tree happens
     */
    private ScrollPane mainPane;


    private TextField inputValue;

    private BorderPane rootPane;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        initialize();
        primaryStage.setTitle("RedBlackTreeVisualization");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    /**
     * This method initialize the user interface of this application
     */
    public void initialize()
    {
        rootPane=new BorderPane();

        mainScene = new Scene(rootPane,APP_WITDTH,APP_HEIGHT);


        inputValue=new TextField();
        inputValue.setPrefWidth(50f);

        inserButton=new Button("Add");
        deleButton=new Button("Delete");
        fixButton= new Button("Fix");
        fixButton.setDisable(true);
        saveButton=new Button("Save tree");
        loadButton=new Button("Load tree");

        Label inputLabel= new Label("Integer input:");
        inputLabel.setFont(Font.font(15f));


        HBox upperPane = new HBox(20f);
        upperPane.setPadding(new Insets(20f,20f,20f,20f));
        upperPane.setAlignment(Pos.BASELINE_CENTER);
        HBox lowerPane = new HBox(20f);
        lowerPane.setPadding(new Insets(0,20f,20f,20f));
        lowerPane.setAlignment(Pos.BASELINE_CENTER);

        VBox topPane = new VBox();
        topPane.getChildren().addAll(upperPane,lowerPane);

        upperPane.getChildren().addAll(inputLabel,inputValue,inserButton,
                deleButton,fixButton);
        lowerPane.getChildren().addAll(saveButton,loadButton);

        rootPane.setTop(topPane);
        //done setting up the top part of the UI

        mainPane = new ScrollPane();
        rootPane.setCenter(mainPane);


    }
}
