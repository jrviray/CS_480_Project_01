package cpp.edu.cs480.project06;


import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by wxy03 on 4/4/2017.
 */
public class GraphicNullNode extends GraphicNode {

    public GraphicNullNode(GraphicNode parent)
    {
        circle=new Circle(RADIUS);

        keyText=new Text("NULL");
        keyText.setFont(Font.font(14));

        parentGraphicNode=parent;
        setColor(BLACK);
        //bind the keyText with the circle so that they always stay in the same relative position
        bindText();

    }

}
