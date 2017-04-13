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

    public final static Color HIGHLIGHT = new Color(0.2118, 0.6392, 0.2588, 1);

    public final static Color HIGHLIGHT_2 = new Color(0.1725, 0.4627, 0.6275, 1);


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
     * Indicate whether this is a null node
     */
    private boolean isNull;

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
        isNull=false;
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
        unhighlightRightLink();
        setRightLinkVisible(false);
        leftLink = new Line();
        leftLink.startXProperty().bind(getXProperty());
        leftLink.startYProperty().bind(getYProperty());
        unhighlightLeftLink();
        setLeftLinkVisible(false);
        //the starting point of two links will always bind to this node
        //links are not visible before it connect to its children

        getChildren().addAll(keyText,circle,rightLink,leftLink);
        keyText.toFront();
        rightLink.toBack();
        leftLink.toBack();
    }


    public final static int RED = 1;

    public final static int BLACK = 0;
    
    public int nodeColor;

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
        	nodeColor = RED;
            circle.setFill(RED_FILL);
            keyText.setFill(RED_KEY_FILL);
            circle.setStroke(RED_STROKE);
        }

        else if(color==BLACK)
        {
        	nodeColor = BLACK;
            circle.setFill(BLACK_FILL);
            keyText.setFill(BLACK_KEY_FILL);
            circle.setStroke(BLACK_STROKE);
        }
    }

    /**
     * This method is used to set and bind the current position of this {@link GraphicNode}
     * with a new parent Node.Once the binding done,the relative position between them
     * will remains the same
     */
    public void bindToParent(GraphicNode newParent)
    {       parent = newParent;
        if(parent!=null) {

            double XDiff = getParentNode().getX() - getX();
            double YDiff = getParentNode().getY() - getY();
            getXProperty().bind(getParentNode().getXProperty().subtract(XDiff));
            getYProperty().bind(getParentNode().getYProperty().subtract(YDiff));
        }

    }


    public double getX()
    {
        return circle.getTranslateX();
    }

    public double getY()
    {
        return circle.getTranslateY();
    }

    public GraphicNode getLeftChild()
    {
        return leftChild;
    }

    public GraphicNode getRightChild()
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
     * This method will bind the link of this {@link GraphicNode} with {@link #leftChild}
     * Notice: this method will not bind  the relative position of between them
     */
    public void setLeftChild(GraphicNode leftChild)
    {
        this.leftChild=leftChild;
        if(leftChild!=null) {
            leftLink.endXProperty().bind(getLeftChild().getXProperty());
            leftLink.endYProperty().bind(getLeftChild().getYProperty());
        }
    }

    /**
     * This method will bind the link of this {@link GraphicNode} with {@link #rightChild}
     * Notice: this method will not bind  the relative position of between them
     */
    public void setRightChild(GraphicNode rightChild)
    {
        this.rightChild=rightChild;
        if(rightChild!=null) {
            rightLink.endXProperty().bind(getRightChild().getXProperty());
            rightLink.endYProperty().bind(getRightChild().getYProperty());
        }
    }

    /**
     * This method is used to unbind the relative position with its parent
     */
    public void unbindParent()
    {
        getXProperty().unbind();
        getYProperty().unbind();
    }


    /**
     * This method will return a {@link Circle} which is the main Shape for any animation
     * @return
     */
    public Circle getCircle()
    {
        return circle;
    }

    /**
     * This method will change the value display on the center of the node
     * @param value
     */
    public void setValue(String value)
    {
        keyText.setText(value);
    }

    /**
     * This method get the value of the node
     * @return
     */
    public String getValue()
    {
        return keyText.getText();
    }


    /**
     * This method will check whether this {@link GraphicNode} is a left child or right child
     * @return
     * {@code true} if this is a left child, {@code false} if this is a right child
     */
    public Boolean isLeftChild()
    {
        if(getParentNode()==null)
            return null;
        else if(getParentNode().getLeftChild()==this)
            return true;
        else
            return false;
    }

    public void highlightLeftLink()
    {
        leftLink.setStroke(HIGHLIGHT);
        leftLink.setStrokeWidth(3f);
    }

    public void highlightRightLink()
    {
        rightLink.setStroke(HIGHLIGHT);
        rightLink.setStrokeWidth(3f);
    }

    public void unhighlightLeftLink()
    {
        leftLink.setStroke(Color.BLACK);
        leftLink.setStrokeWidth(1f);
    }


    public void unhighlightRightLink()
    {
        rightLink.setStroke(Color.BLACK);
        rightLink.setStrokeWidth(1f);
    }

    public boolean isNull()
    {
        return isNull;
    }

    public void setNull()
    {
        isNull=true;
    }

    public void setLeftLinkVisible(boolean isVisible)
    {
        leftLink.setVisible(isVisible);
    }

    public void setRightLinkVisible(boolean isVisible)
    {
        rightLink.setVisible(isVisible);
    }

    public double getTextX() {return keyText.getTranslateX();}

    public double getTextY(){return keyText.getTranslateY();}

}
