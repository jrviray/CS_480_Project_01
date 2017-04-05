package cpp.edu.cs480.project06;


import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by wxy03 on 4/4/2017.
 */
public class GraphicNullNode extends GraphicNode {

    public GraphicNullNode(GraphicNode parent,boolean isLeft)
    {
        circle=new Circle(RADIUS);

        keyText=new Text("NULL");
        keyText.setFont(Font.font(14));

        setColor(BLACK);
        //bind the keyText with the circle so that they always stay in the same relative position
        bindText();

        parentGraphicNode=parent;

        //bind the null node with parent node
        if(isLeft)
            circle.translateXProperty().bind(parent.circle.translateXProperty().subtract(40f));
        else
            circle.translateXProperty().bind(parent.circle.translateXProperty().add(40f));
        circle.translateYProperty().bind(parent.circle.translateYProperty().add(40f));

    }
}
