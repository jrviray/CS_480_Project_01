/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.edu.cs480.project06;

import javafx.animation.*;
//import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author Hunter
 */
public class testGui {
    private Pane mainPane;
    public testGui(Pane mainPane) {
        this.mainPane = mainPane;
    }
    public void drawNode(GraphicNode node) {
        mainPane.getChildren().add(node);
        node.toFront();
    }
    public void moveNodeAnimation(GraphicNode sourceNode, double targetX,
                                   double targetY, double second,
                                   EventHandler<ActionEvent> afterEvent)
    {
        //SequentialTransition st = new SequentialTransition();
        TranslateTransition tt = new TranslateTransition(Duration.seconds(second),sourceNode.getCircle());
        
        tt.setToX(targetX);
        tt.setToY(targetY);
        tt.setOnFinished(afterEvent); 
      // st.getChildren().addAll(tt);
        tt.play();
    }
    public void pauseAnimation() throws Exception {
//        PauseTransition pt = new PauseTransition();
//        System.out.println("well");
//        pt.setDuration(new Duration(3000));
//        pt.setOnFinished(null);
//        pt.play();
        Thread.sleep(3000);
    }
}
