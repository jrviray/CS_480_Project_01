/**
 * For now, this class is just for testing JAVA FX
 * Created by xinyuan_wang on 4/3/17.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class GUI extends Application {

    TextField text;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Button TestButton =new Button();
        text=new TextField();
        text.setEditable(false);
        TestButton.setText("Test");
        TestButton.setOnAction(this::TestButtonAction);

        BorderPane root= new BorderPane();
        root.setCenter(TestButton);
        root.setBottom(text);

        Scene scene = new Scene(root,500,500);

        primaryStage.setTitle("RedBlackTreeVisualization");
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    private void TestButtonAction(ActionEvent event)
    {
        System.out.println("Hello World");
        text.setText("Hello World");
    }
}
