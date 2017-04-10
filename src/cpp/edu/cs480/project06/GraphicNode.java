package cpp.edu.cs480.project06;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GraphicNode extends Group {

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
    private Circle circle;

    /**
     * This two represents the link between this node and its children
     */
    private Line rightLink;

    private Line leftLink;

    private GraphicNode leftChild;

    private  GraphicNode rightChild;

    private GraphicNode parent;

    /**
     * This represents the value on the node
     */
    private Text keyText;

    /**
     * This represents the parent of this node currently on the graphic, and
     * it does not necessarily always coincide with the actually parent node
     * on the RBTree.
     */


    /**
     * This is the default constructor for GraphicNode, and it will only initialize
     * the {@link #circle} with red and {@link #keyText}
     * @param x
     *          the initial center x for circle
     * @param y
     *          the initial center y for circle
     * @param value
     *          the value of this node
     */
    public GraphicNode(double x,double y,String value)
    {
        //initialize the circle and keyText
        circle=new Circle(RADIUS);

        circle.setTranslateX(x);
        circle.setTranslateY(y);
        //format the key into four digit with leading zero
        keyText=new Text(value);
        keyText.setFont(Font.font(14));
        double H =  keyText.getBoundsInLocal().getHeight();
        double W = keyText.getBoundsInLocal().getWidth();
        keyText.translateXProperty().bind(getXProperty().subtract(W/2));
        keyText.translateYProperty().bind(getYProperty().add(H/4));
        //bind the keyText with the circle so that they always stay in the same relative position

        rightLink=new Line();
        rightLink.startXProperty().bind(getXProperty());
        rightLink.startYProperty().bind(getYProperty());
        rightLink.setVisible(false);
        leftLink = new Line();
        leftLink.startXProperty().bind(getXProperty());
        leftLink.startYProperty().bind(getYProperty());
        leftLink.setVisible(false);
        //the starting point of two links will always bind to this node
        //links are not visible before it connect to its children

        getChildren().addAll(keyText,circle,rightLink,leftLink);
        keyText.toFront();
        rightLink.toBack();
        leftLink.toBack();
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


    /**
     * This method is used to check whether this {@link GraphicNode} has left null node
     * but not shown in the Graphic. Also, if there is such condition, a null node
     * will be added
     * @return
     * {@code true} if a new null node is being added, {@code false} if not
     */
    public boolean checkLeftNullNode()
    {
            if (leftChild == null) {
                GraphicNode leftChild = new GraphicNode(getX(),getY(),"NULL");
                leftChild.setColor(BLACK);
                leftChild.setParent(this);
                setLeftChild(leftChild);
                return true;
            } else
                return false;
    }

    /**
     * This method is used to check whether this {@link GraphicNode} has right null node
     * but not shown in the Graphic. Also, if there is such condition, a null node
     * will be added
     * @return
     * {@code true} if a new null node is being added, {@code false} if not
     */
    public boolean checkRightNullNode() {
        if (getRightChildNode() == null) {
            GraphicNode rightChild = new GraphicNode(getX(),getY(),"NULL");
            rightChild.setColor(BLACK);
            rightChild.setParent(this);
            setRightChild(rightChild);
            return true;
        } else
            return false;
    }


    /**
     * This method is used to bind the current position of this {@link GraphicNode} with its
     * {@link #getParentNode()}. Once the binding done, the relative position between them
     * will remains the same
     */
    public void bindCurrentParent()
    {
        double XDiff=getParentNode().getX()-getX();
        double YDiff=getParentNode().getY()-getY();
        getXProperty().bind(getParentNode().getXProperty().subtract(XDiff));
        getYProperty().bind(getParentNode().getYProperty().subtract(YDiff));

    }


    public double getX()
    {
        return circle.getTranslateX();
    }

    public double getY()
    {
        return circle.getTranslateY();
    }

    public GraphicNode getLeftChildNode()
    {
        return leftChild;
    }

    public GraphicNode getRightChildNode()
    {
        return rightChild;
    }

    public GraphicNode getParentNode()
    {
        return parent;
    }

    public DoubleProperty getXProperty()
    {
        return circle.translateXProperty();
    }

    public DoubleProperty getYProperty()
    {
        return circle.translateYProperty();
    }

    /**
     *This method can set the node a parent, but will not bind the relative position with the
     * new parent node
     * @param parent
     */
    public void setParent(GraphicNode parent)
    {
        this.parent=parent;
    }


    /**
     * This method will bind the link of this {@link GraphicNode} with {@link #leftChild}
     * Also, this method will make the left link visible
     * Notice: this method will not bind  the relative position of between them
     */
    public void setLeftChild(GraphicNode leftChild)
    {
        this.leftChild=leftChild;
        leftLink.endXProperty().bind(getLeftChildNode().getXProperty());
        leftLink.endYProperty().bind(getLeftChildNode().getYProperty());
        leftLink.setVisible(true);
    }

    /**
     * This method will bind the link of this {@link GraphicNode} with {@link #rightChild}
     * Also, this method will make the left link visible
     * Notice: this method will not bind  the relative position of between them
     */
    public void setRightChild(GraphicNode rightChild)
    {
        this.rightChild=rightChild;
        rightLink.endXProperty().bind(getRightChildNode().getXProperty());
        rightLink.endYProperty().bind(getRightChildNode().getYProperty());
        rightLink.setVisible(true);
    }

    /**
     * This will return a {@link Circle} which is the main Shape for any animation
     * @return
     */
    public Circle getCircle()
    {
        return circle;
    }

    public void setValue(String value)
    {
        keyText.setText(value);
    }

}
