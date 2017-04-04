package cpp.edu.cs480.project06;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;


/**
 * Created by wxy03 on 4/3/2017.
 */
public class GraphicNode {

    /**
     * This color is for filling the red nodes
     */
    public static Color RED_FILL = new Color(1f,0,0,0.6);

    /**
     * This color is for the outline of the red nodes
     */
    public static Color RED_STROKE = new Color(.8,0,0,1f);

    /**
     * This color is for filling key of the red nodes
     */
    public static Color RED_KEY_FILL = Color.MAROON;

    public static double RADIUS=20f;

    /**
     * This represents the circle shape of the node
     */
    protected Circle circle;

    /**
     * This represents the link between this node and its parent node
     */
    protected Line link;


    /**
     * This represents the value on the node
     */
    protected Label keyText;

    /**
     * This represents the parent of this node currently on the graphic, and
     * it does not necessarily always coincide with the actually parent node
     * on the RBTree.
     */
    protected GraphicNode parentGraphicNode;

    /**
     * This is the default consturctor for GraphicNode, and it will only initialize
     * the {@link #circle} with red and {@link #keyText}
     * @param x
     *          the initial center x for circle
     * @param y
     *          the initial center y for circle
     */
    public GraphicNode(double x,double y,int key)
    {
        //initialize the circle and keyText
        circle=new Circle(RADIUS);
        circle.setFill(RED_FILL);
        circle.setTranslateX(x);
        circle.setTranslateY(y);
        circle.setStroke(RED_STROKE);
        //format the key into four digit with leading zero
        keyText=new Label(String.format("%04d",key));
        keyText.setTextFill(RED_KEY_FILL);
        keyText.setFont(Font.font(14));

        //bind the keyText with the circle so that they always stay in the same relative position
        keyText.translateXProperty().bind(circle.translateXProperty().subtract(15f));
        keyText.translateYProperty().bind(circle.translateYProperty().subtract(9f));

    }
}
