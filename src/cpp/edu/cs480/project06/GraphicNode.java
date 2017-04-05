package cpp.edu.cs480.project06;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Created by wxy03 on 4/3/2017.
 */
public class GraphicNode {

    /**
     * This color is for filling the red nodes
     */
    public final static Color RED_FILL = new Color(1, 0.3373, 0.3098, 1);

    /**
     * This color is for the outline of the red nodes
     */
    public  final static Color RED_STROKE = new Color(0.3922, 0, 0, 1);

    /**
     * This color is for filling key of the red nodes
     */
    public  final static Color RED_KEY_FILL = Color.MAROON;

    /**
     * This color is for filling the black nodes
     */
    public  final static Color BLACK_FILL= new Color(0.4314, 0.4314, 0.4314, 1);

    /**
     * This color is for the outline of the black nodes
     */
    public  final static Color BLACK_STROKE = Color.BLACK;


    /**
     * This color is for filling key of the black nodes
     */
    public static Color BLACK_KEY_FILL = Color.BLACK;

    public static double RADIUS=20f;

    /**
     * This represents the circle shape of the node
     */
    protected Circle circle;

    /**
     * This two represents the link between this node and its children
     */
    protected Line rightLink;

    protected Line leftLink;

    protected GraphicNode leftChild;

    protected  GraphicNode rightChild;


    /**
     * This represents the value on the node
     */
    protected Text keyText;

    /**
     * This represents the parent of this node currently on the graphic, and
     * it does not necessarily always coincide with the actually parent node
     * on the RBTree.
     */
    protected GraphicNode parentGraphicNode;



    public GraphicNode(){}


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

        circle.setTranslateX(x);
        circle.setTranslateY(y);
        //format the key into four digit with leading zero
        keyText=new Text(String.format("%04d",key));
        setColor(RED);
        //bind the keyText with the circle so that they always stay in the same relative position
        bindText();

        //create two null children and link together
        leftChild=new GraphicNullNode(this,true);
        rightChild=new GraphicNullNode(this,false);

        leftLink = new Line();
        rightLink = new Line();

        leftLink.startXProperty().bind(circle.translateXProperty());
        leftLink.startYProperty().bind(circle.translateYProperty());
        rightLink.startXProperty().bind(circle.translateXProperty());
        rightLink.startYProperty().bind(circle.translateYProperty());
        //the starting point of two links will always bind to this node

        connectLink(leftChild,true);
        connectLink(rightChild,false);


    }

    /**
     * This is a aiding method to bind the text Text with the circle
     */
    protected void bindText()
    {
       double H =  keyText.getBoundsInLocal().getHeight();
       double W = keyText.getBoundsInLocal().getWidth();
        keyText.translateXProperty().bind(circle.translateXProperty().subtract(W/2));
        keyText.translateYProperty().bind(circle.translateYProperty().add(H/4));
    }

    /**
     * This method is used to bind the link of this {@link GraphicNode} with others
     * Notice: this method is only for binding the {@link #leftLink} and {@link #rightLink},
     * not for bind the relative position of {@link #circle}
     * @param target
     *              The target {@link GraphicNode} that is going to link
     * @param isLeft
     *              indicate which link is going to bind
     *              {@code true} if is left link;
     *              {@code false} if is right link;
     */
    protected void connectLink(GraphicNode target,boolean isLeft)
    {
        if(isLeft) {
            leftLink.endXProperty().bind(target.circle.translateXProperty());
            leftLink.endYProperty().bind(target.circle.translateYProperty());
        }
        else {
            rightLink.endXProperty().bind(target.circle.translateXProperty());
            rightLink.endYProperty().bind(target.circle.translateYProperty());
        }

    }


    public final static int RED = 1;

    public final static int BLACK =0;

    /**
     * This method is used to set the color for the current node
     * @param color
     *              An static integer constant defined in this class,
     *              could be RED, BLACK
     */
    public void setColor(int color)
    {
        if(color==RED)
        {
            circle.setFill(RED_FILL);
            keyText.setFill(RED_KEY_FILL);
            circle.setStroke(RED_STROKE);
        }

        else if(color==BLACK)
        {
            circle.setFill(BLACK_FILL);
            keyText.setFill(BLACK_KEY_FILL);
            circle.setStroke(BLACK_STROKE);
        }
    }

}
