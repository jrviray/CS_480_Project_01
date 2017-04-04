package cpp.edu.cs480.project06;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javax.xml.soap.Text;

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
     * This color is for the integer of the red nodes
     */
    public static Color RED_INTEGER = Color.MAROON;

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
     * It is a good idea to set font size to 14,
     * and bind the
     * x to circle.getCenterX() - 16
     * y to circle.getCenterY() + 5
     * and padded # of digit up to 4
     * for beauty propose
     */
    protected Text integer;










}
