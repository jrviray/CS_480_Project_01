package cpp.edu.cs480.project06;/**
 * Created by wxy03 on 4/9/2017.
 *
 * This class is only for testing the animator!!!
 */

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class TestAnimationUI extends Application {

    private Scene mainScene;

    private Pane mainPane;

    private double mainPaneWidth= 1200;

    private double mainPaneHeight = 600;

    private Animator animator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initialize();
        primaryStage.setTitle("TestUI");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    private void initialize()
    {
        BorderPane rootPane= new BorderPane();
        mainPane = new Pane();
        rootPane.setCenter(mainPane);
        mainPane.setPrefWidth(mainPaneWidth);
        mainPane.setPrefHeight(mainPaneHeight);
        mainScene = new Scene(rootPane);
        animator = new Animator(mainPane);
        Button testButton = new Button("test");
        rootPane.setBottom(testButton);
        testButton.setOnMouseClicked(event -> test());
    }

    private void test()
    {
           testQueue = new LinkedList<>();
           testQueue.add(1);
           testQueue.add(2);
           testQueue.add(3);
           testQueue.add(4);
           testQueue.add(5);
           testQueue.add(6);
           testQueue.add(7);
        testQueue.add(8);
        testQueue.add(9);
           playAnimation(testQueue.poll());
    }



   LinkedList<Integer> testQueue;
    private void playAnimation(int type)
    {
        Animation thisAnimation = new PauseTransition(Duration.ZERO);
        switch (type)
        {
            case 1:
                animator.generateNode(type-1);
                thisAnimation=animator.insertRootAnimation(0);
                break;
            case 2:
                animator.generateNode(type-1);
                thisAnimation=animator.insertLeftAnimation(type-1,0);
                break;
            case 3:
                animator.generateNode(type-1);
                thisAnimation=animator.insertLeftAnimation(type-1,1);
                break;
            case 4:
                animator.generateNode(type-1);
                thisAnimation=animator.insertRightAnimation(type-1,2);
                break;
//           case 5:
//                animator.generateNode(type-1);
//                thisAnimation=animator.insertRightAnimation(type-1,3);
//                break;
//            case 6:
//                animator.generateNode(type-1);
//                thisAnimation=animator.insertRightAnimation(type-1,4);
//                break;
//            case 7:
//                animator.generateNode(type-1);
//                thisAnimation=animator.insertRightAnimation(type-1,2);
//                break;
            case 8:
                thisAnimation=animator.rotateRightAnimation(0);
                break;
            case 9:
                thisAnimation=animator.rotateRightAnimation(1);
                break;

        }

        if(testQueue.isEmpty()) {
            thisAnimation.play();
            return;
        }
        else {
            Integer nextType = testQueue.poll();
            thisAnimation.setOnFinished(event -> playAnimation(nextType));
            thisAnimation.play();
        }
    }
}
