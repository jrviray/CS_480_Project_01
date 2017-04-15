package cpp.edu.cs480.project06;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by wxy03 on 4/9/2017.
 */
public class Animator {

    private Pane mainPane;

    private GraphicNode[] hashTable;

    private final double UNIT_DISTANCE = GraphicNode.RADIUS * 3;

    private final double NULL_UNIT_DISTANCE = GraphicNode.RADIUS * 1.5;

    private boolean nullNodeVisible;

    private TextArea outputArea;


    /**
     * default constructor
     * @param mainPane
     */
    public Animator(Pane mainPane, boolean isNullVisible,TextArea outputArea)
    {
        this.mainPane=mainPane;
        hashTable=new GraphicNode[2];
        nullNodeVisible=isNullVisible;
        this.outputArea=outputArea;
    }


    /**
     * This method is used to generate a new Node with its unique ID#.
     * @param nodeValue
     *          the data value of the new node
     * @return
     *          the assigned ID# of the new node
     */
    public int generateNode(int nodeValue)
    {
        GraphicNode newNode = new GraphicNode(UNIT_DISTANCE,UNIT_DISTANCE,
                String.format("%04d",nodeValue));   //all the node will start at the top left corner of the canvas
        newNode.setColor(GraphicNode.RED);      //all the node start in red
        drawOnCanvas(newNode);  //add the node on canvas
        int ID;
        //find an empty spot on hash table
        for(ID=0;ID<hashTable.length;ID++)
        {
            if(getNode(ID)==null)
                break;
        }

        hashTable[ID]=newNode;
        //if the hash table is full, expand the table
        if(ID==hashTable.length-1)
        {
            hashTable= Arrays.copyOf(hashTable,hashTable.length*2);
        }

        //dynamically create null children nodes for this new node
        addNullNode(newNode);
        newNode.setHashID(ID);
        return ID;

    }

    /**
     * This method is used to draw an element on the canvas
     * @param node
     */
    private void drawOnCanvas(Node node)
    {
        mainPane.getChildren().add(node);
        node.toFront();
    }

    /**
     * This method is used to remove an element from the canvas
     * @param node
     */
    private void removeFromCanvas(Node... node)
    {
        mainPane.getChildren().removeAll(node);
    }


    /**
     * This is an aiding method for generate an animation for movement of a {@link Circle} to the target position
     * @param target
     * @param x
     * @param y
     * @param onFinish
     * @return
     */
    private TranslateTransition movementTo(Node target, double x, double y, EventHandler<ActionEvent> onFinish)
    {
        TranslateTransition movementTo = new TranslateTransition(Duration.seconds(1),target);
        movementTo.setToX(x);
        movementTo.setToY(y);
        movementTo.setOnFinished(onFinish);
        return movementTo;
    }

    /**
     * This is an aiding method for generate an animation for movement of a {@link GraphicNode} to the target position
     * @param target
     * @param x
     * @param y
     * @param onFinish
     * @return
     */
    private TranslateTransition movementTo(GraphicNode target,double x,double y,EventHandler<ActionEvent> onFinish)
    {
        return movementTo(target.getCircle(),x,y,onFinish);
    }

    /**
     * This is an aiding method for generate an animation for movement of a {@link GraphicNode} by a certain amount
     * of distance
     * @param target
     * @param x
     * @param y
     * @param onFinish
     * @return
     */
    private TranslateTransition movementBy(GraphicNode target, double x, double y, EventHandler<ActionEvent> onFinish)
    {
        TranslateTransition movementBy = new TranslateTransition(Duration.seconds(1),target.getCircle());
        movementBy.setByX(x);
        movementBy.setByY(y);
        movementBy.setOnFinished(onFinish);
        return movementBy;
    }


    /**
     * This is the aiding method thar creates an animation for position adjustment, it push
     * all the parents node that are less than parentNode's left child move to left/right
     * @param parentNode
     * @param isGoLeft
     * @return
     */
    private ParallelTransition leftAdjustment(GraphicNode parentNode, boolean isGoLeft) {
        //make all the parents that are less than move to left/right

        ParallelTransition mainAnimation = new ParallelTransition();
        GraphicNode currentNode = parentNode;
        LinkedList<GraphicNode> movingQueue = new LinkedList<>();
        LinkedList<GraphicNode> unbindQueue = new LinkedList<>();

        //traversal up and find out all the parent nodes that are less than and add them to movingQueue
        //at the same time, keep track of their child which will change the relative position after the moving
        //so that they could be unbind before and rebind after the adjustment
        while(true)
        {
            Boolean isCurrentNodeLeft = currentNode.isLeftChild();
            if(isCurrentNodeLeft==null)
                break;  //hit to the root node
            else if(!isCurrentNodeLeft) {
                unbindQueue.add(currentNode);
                movingQueue.add(currentNode.getParentNode());
            }
            currentNode=currentNode.getParentNode();
        }

        // poll the element from movingQueue and unbindQueue one by one and create the adjustment animation
        // for each of them
        int factor = isGoLeft? -1:1;
        while (!movingQueue.isEmpty()) {
            GraphicNode movingNode=movingQueue.poll();
            GraphicNode unbindNode=unbindQueue.poll();
            movingNode.unbindParent();  //unbind the parent
            unbindNode.unbindParent();

            mainAnimation.getChildren().add(
                    movementBy(movingNode, factor*UNIT_DISTANCE, 0,
                            event -> {
                                movingNode.bindToParent(movingNode.getParentNode()); //rebind the parent
                                unbindNode.bindToParent(movingNode); //rebind the leftChild
                            }
                    ));
        }
        return mainAnimation;
    }

    /**
     * This is the aiding method thar creates an animation for position adjustment, it push
     * all the parents node that are greater than parentNode's right child  to the right/left
     * @param parentNode
     * @return
     */
    private ParallelTransition rightAdjustment(GraphicNode parentNode,boolean isGoLeft) {

        ParallelTransition mainAnimation = new ParallelTransition();
        GraphicNode currentNode = parentNode;
        LinkedList<GraphicNode> movingQueue = new LinkedList<>();
        LinkedList<GraphicNode> unbindQueue = new LinkedList<>();

        //traversal up and find out all the parent nodes that are greater than and add them to movingQueue
        //at the same time, keep track of their child which will change the relative position after the moving
        //so that they could be unbind before and rebind after the adjustment
        while(true)
        {
            Boolean isCurrentNodeLeft = currentNode.isLeftChild();
            if(isCurrentNodeLeft==null)
                break;  //hit to the root node
            else if(isCurrentNodeLeft) {
                unbindQueue.add(currentNode);
                movingQueue.add(currentNode.getParentNode());
            }
            currentNode=currentNode.getParentNode();
        }

        // poll the element from movingQueue and unbindQueue one by one and create the adjustment animation
        // for each of them
        int factor = isGoLeft? -1:1;
        while (!movingQueue.isEmpty()) {
            GraphicNode movingNode=movingQueue.poll();
            GraphicNode unbindNode=unbindQueue.poll();
            movingNode.unbindParent();  //unbind the parent
            unbindNode.unbindParent();

            mainAnimation.getChildren().add(
                    movementBy(movingNode, factor*UNIT_DISTANCE, 0,
                            event -> {
                                movingNode.bindToParent(movingNode.getParentNode()); //rebind the parent
                                unbindNode.bindToParent(movingNode); //rebind the leftChild
                            }
                    ));
        }
        return mainAnimation;
    }

    public TranslateTransition centerAdjustment(int rootID)
    {

        GraphicNode root = getNode(rootID);
        TranslateTransition mainAnimation = movementTo(root,mainPane.getWidth()/2,root.getY(),null);
        mainAnimation.setRate(3f);
        return mainAnimation;
    }



    /**
     * This method is to generate an animation for inserting root node
     * @param newNodeID
     *          the ID# of the root node that is going to be inserted
     * @return
     *a {@link SequentialTransition} that represents the root insertion animation
     */
    public SequentialTransition insertRootAnimation(int newNodeID)
    {
        SequentialTransition mainAnimation = new SequentialTransition();
        GraphicNode newNode = getNode(newNodeID);
        outputString("insert root node "+newNode.getValue());
        newNode.setColor(GraphicNode.BLACK);    //root node's color is always black
        TranslateTransition movementAnimation = movementTo(newNode,mainPane.getWidth()/2, newNode.getY(),
                null);  //move to the center of the canvas
        mainAnimation.getChildren().add(movementAnimation);
        return mainAnimation;
    }

    /**
     * This method is to generate an animation for inserting node left
     * @param newNodeID
     *              the ID# of the new node that is going to be inserted
     * @param parentNodeID
     *              the ID# of the parent node of the new node
     * @return
     * {@link SequentialTransition} that represents the left insertion animation
     */
    public SequentialTransition insertLeftAnimation(int newNodeID, int parentNodeID)
    {
        GraphicNode parentNode=getNode(parentNodeID),
                newNode=getNode(newNodeID);
        SequentialTransition mainAnimation = new SequentialTransition();
        ParallelTransition adjustmentAnimation = this.leftAdjustment(parentNode,true);

        adjustmentAnimation.setOnFinished(actionEvent->{this.removeFromCanvas(parentNode.getLeftChild());  //remove the null node
            parentNode.setLeftChild(newNode);
            parentNode.setLeftLinkVisible(true);
            outputString("insert node "+newNode.getValue()+" to the left of node "+parentNode.getValue());});  //create the link and make it visible
        double targetX=parentNode.getX()-UNIT_DISTANCE,
                targetY=parentNode.getY()+UNIT_DISTANCE;
        outputString("Finding an empty spot to insert "+newNode.getValue());
        mainAnimation.getChildren().addAll(
                                    this.insertionTraversal(parentNodeID), //add the highlight animation
                                    adjustmentAnimation, //add adjustment animation
                                    //add the movement animation
                                    this.movementTo(newNode,targetX,targetY,
                        actionEvent->{newNode.bindToParent(parentNode);}  //when finished, bind with parent
                ));
        return mainAnimation;
    }

    /**
     * This method is to generate an animation for inserting node right
     * @param newNodeID
     *              the ID# of the new node that is going to be inserted
     * @param parentNodeID
     *              the ID# of the parent node of the new node
     * @return
     * {@link SequentialTransition} that represents the right insertion animation
     */
    public SequentialTransition insertRightAnimation(int newNodeID, int parentNodeID)
    {
        GraphicNode parentNode=getNode(parentNodeID),
                newNode=getNode(newNodeID);
        SequentialTransition mainAnimation = new SequentialTransition();
        ParallelTransition adjustmentAnimation = this.rightAdjustment(parentNode,false);

        adjustmentAnimation.setOnFinished(actionEvent->{this.removeFromCanvas(parentNode.getRightChild());  //remove the null node
            parentNode.setRightChild(newNode);
            parentNode.setRightLinkVisible(true);
            outputString("insert node "+newNode.getValue()+" to the right of node "+parentNode.getValue());});  //create the link and make it visible

        double targetX=parentNode.getX()+UNIT_DISTANCE,
                targetY=parentNode.getY()+UNIT_DISTANCE;
        outputString("Finding an empty spot to insert "+newNode.getValue());
        mainAnimation.getChildren().addAll(
                this.insertionTraversal(parentNodeID), //add the highlight animation
                adjustmentAnimation, //add adjustment animation
                //add the movement animation
                this.movementTo(newNode, targetX, targetY,
                        actionEvent->{newNode.bindToParent(parentNode);} //when finished, bind with parent
                ));
        return mainAnimation;

    }


    /**
     * Get a path from parentNode to childNode
     * @param childNode
     * @param parentNode
     * @return
     */
    private Stack<GraphicNode> getTraversal(GraphicNode childNode, GraphicNode parentNode)
    {
        Stack<GraphicNode> traversalStack = new Stack<>();
        GraphicNode currentNode = childNode;
        while(currentNode!=parentNode)
        {
            traversalStack.push(currentNode);
            currentNode=currentNode.getParentNode();
        }
        return traversalStack;
    }

    /**
     * This is an aiding method to create an animation for a highlight circle traverse
     * through a path of node, the circle will be added on the canvas before the animation
     * begins
     * @param circle
     * @param path
     * @return
     */
    private SequentialTransition highlightTraversal(Circle circle,Stack<GraphicNode> path)
    {
        SequentialTransition mainAnimation = new SequentialTransition();
        final GraphicNode startingNode=path.pop();
        PauseTransition drawCircle = new PauseTransition(Duration.ONE);
        circle.setTranslateX(startingNode.getX());
        circle.setTranslateY(startingNode.getY());
        drawCircle.setOnFinished(actionEvent->{drawOnCanvas(circle);});
        mainAnimation.getChildren().add(drawCircle);
        while(!path.isEmpty()) {

            final GraphicNode nextNode = path.pop();
            mainAnimation.getChildren().addAll(new PauseTransition(Duration.seconds(.5f)),
                    //when reach a node, wait for a while
                    this.movementTo(circle, nextNode.getX(), nextNode.getY(),null));
                    //move to the next node
        }
        mainAnimation.getChildren().add(new PauseTransition(Duration.seconds(.5f)));
            return  mainAnimation;
    }

    /**
     * This is an aiding method to generate the highlight circle animation specifically for insertion
     * @param parentNodeID
     * @return
     */
    private SequentialTransition insertionTraversal(int parentNodeID)
    {
        SequentialTransition mainAnimation = new SequentialTransition();

        // get a path from root to the parentNode in a stack
        Stack<GraphicNode> traversalStack = getTraversal(getNode(parentNodeID),null);
        //initialize the highlight circle to the root node
        Circle highlightCircle = createHighlightCircle();
        //get the traversal animation
        SequentialTransition traversalAnimation = highlightTraversal(highlightCircle,traversalStack);

        //remove the highlight circle from the canvas
        PauseTransition removeCircle = new PauseTransition(Duration.seconds(.5f));
        removeCircle.setOnFinished(actionEvent->{removeFromCanvas(highlightCircle);});
        mainAnimation.getChildren().addAll(traversalAnimation,removeCircle);

        return mainAnimation;

    }

    /**
     * This is an aiding method to create an Highlight circle but not put it on the canvas yet

     * @return
     */
    private Circle createHighlightCircle()
    {
        Circle highlightCircle = new Circle(GraphicNode.RADIUS);
        highlightCircle.setFill(new Color(0,0,0,0));
        highlightCircle.setStroke(GraphicNode.HIGHLIGHT);
        highlightCircle.setStrokeType(StrokeType.OUTSIDE);
        highlightCircle.setStrokeWidth(5);
        return highlightCircle;
    }

    /**
     * This is an aiding method to add the null node for a specific node, and this
     * is not an animation
     * @param thisNode
     */
    private void addNullNode(GraphicNode thisNode)
    {
        if(thisNode.getLeftChild()==null)
        {
            GraphicNode nullNode = new GraphicNode(thisNode.getX()-NULL_UNIT_DISTANCE,
                    thisNode.getY()+UNIT_DISTANCE,"NULL");
            nullNode.setNull();
            nullNode.setColor(GraphicNode.BLACK);
            nullNode.bindToParent(thisNode);
            thisNode.setLeftChild(nullNode);

            thisNode.setLeftLinkVisible(nullNodeVisible);
            nullNode.setVisible(nullNodeVisible);
            drawOnCanvas(nullNode);
        }
        if(thisNode.getRightChild()==null)
        {
            GraphicNode nullNode = new GraphicNode(thisNode.getX()+NULL_UNIT_DISTANCE,
                    thisNode.getY()+UNIT_DISTANCE,"NULL");
            nullNode.setNull();
            nullNode.setColor(GraphicNode.BLACK);
            nullNode.bindToParent(thisNode);
            thisNode.setRightChild(nullNode);

            thisNode.setRightLinkVisible(nullNodeVisible);
            nullNode.setVisible(nullNodeVisible);
            drawOnCanvas(nullNode);

        }
    }

    /**
     * This method is to generate an animation for rotating node left
     * @param rotateNodeID
     *                  the ID# of the top node that is going to rotate left
     * @return
     * a {@link SequentialTransition} that represents a left rotation animation
     */
    public SequentialTransition rotateLeftAnimation(int rotateNodeID)
    {
        GraphicNode topNode = getNode(rotateNodeID);    // node that is going to move down and currently on the top
        Boolean isLeftChild = topNode.isLeftChild();    //to indicate whether the top node is a left child or right
        GraphicNode parentNode = topNode.getParentNode();   //the parent node of top node
        GraphicNode bottomNode = topNode.getRightChild();   //node that is going to move up and currently on the bottom
        GraphicNode exchangeNode = bottomNode.getLeftChild();   //this is the root of a subtree that is going to change parent during the rotation

        SequentialTransition mainAnimation = new SequentialTransition();
        topNode.highlightRightLink();
        PauseTransition highlight = new PauseTransition(Duration.ONE);   //highlight the rotation link
        outputString("Invariant is broken.\nPress fix to continue.");

        //after the highlight preparing for rotation
        highlight.setOnFinished(event -> {
            outputString("Rotate node "+topNode.getValue()+" to the left");
        topNode.unhighlightRightLink();
        topNode.unbindParent();
        bottomNode.unbindParent();  //the two moving node first unbind with their parent so that they could move freely
        topNode.setRightChild(exchangeNode);    //the top node get a new right child, which is the exchange node
        if(exchangeNode.isNull())
            topNode.setRightLinkVisible(nullNodeVisible);   //exchange node could be null node


        bottomNode.setLeftChild(topNode);   //bottom node get new left child, which is the top node
        bottomNode.setLeftLinkVisible(true);    //bottom node's left child will not be null

        exchangeNode.unbindParent();    //exchange shouldn't move along its new parent

            if(parentNode!=null)    //if top node is not root node, the parentNode change its child from top node to bottom node
            {
                if(isLeftChild)
                    parentNode.setLeftChild(bottomNode);
                else
                    parentNode.setRightChild(bottomNode);
            }
            resetOverlay(topNode);  //since top node and bottom node change the level, their graphic overlay should reset
        });
        ParallelTransition rotate = new ParallelTransition();

        rotate.getChildren().addAll(movementBy(bottomNode,0,-UNIT_DISTANCE,
                event -> {bottomNode.bindToParent(parentNode);})
                                    ,movementBy(topNode,0,UNIT_DISTANCE,
                        event -> {topNode.bindToParent(bottomNode);
                            exchangeNode.bindToParent(topNode);}));
        mainAnimation.getChildren().addAll(highlight,rotate);
        return mainAnimation;
    }

    /**
     * This is an aiding method to solve the overlay issue when a node changes its level
     * @param node
     */
    private void resetOverlay(GraphicNode node)
    {
        node.toFront();
        if(node.getLeftChild()!=null)
            resetOverlay(node.getLeftChild());
        if(node.getRightChild()!=null)
            resetOverlay(node.getRightChild());

    }

    /**
     * This method is to generate an animation for rotating node right
     * @param rotateNodeID
     *                  the ID# of the top node that is going to rotate right
     * @return
     * a {@link SequentialTransition} that represents a right rotation animation
     */
    public SequentialTransition rotateRightAnimation(int rotateNodeID)
    {

        GraphicNode topNode = getNode(rotateNodeID);    // node that is going to move down and currently on the top
        Boolean isLeftChild = topNode.isLeftChild();    //to indicate whether the top node is a left child or right
        GraphicNode parentNode = topNode.getParentNode();   //the parent node of top node
        GraphicNode bottomNode = topNode.getLeftChild();    //node that is going to move up and currently on the bottom
        GraphicNode exchangeNode = bottomNode.getRightChild();  //this is the root of a subtree that is going to change parent during the rotation

        SequentialTransition mainAnimation = new SequentialTransition();
        topNode.highlightLeftLink();    //highlight the rotation link
        PauseTransition highlight = new PauseTransition(Duration.ONE);
        outputString("Invariant is broken.\nPress fix to continue.");
        //after the highlight preparing for rotation
        highlight.setOnFinished(event -> {
            outputString("Rotate node "+topNode.getValue()+" to the right");
            topNode.unhighlightLeftLink();
            topNode.unbindParent();
            bottomNode.unbindParent();  //the two moving node first unbind with their parent so that they could move freely
            topNode.setLeftChild(exchangeNode); //the top node get a new left child, which is the exchange node

            if(exchangeNode.isNull())
                topNode.setLeftLinkVisible(nullNodeVisible);    //exchange node could be null node

            bottomNode.setRightChild(topNode);  //bottom node get new right child, which is the top node
            bottomNode.setRightLinkVisible(true);   //bottom node's right child could not be null node
            exchangeNode.unbindParent();

            if(parentNode!=null)    //if top node is not root node, the parentNode change its child from top node to bottom node
            {
                if(isLeftChild)
                    parentNode.setLeftChild(bottomNode);
                else
                    parentNode.setRightChild(bottomNode);
            }
            resetOverlay(topNode);   //since top node and bottom node change the level, their graphic overlay should reset
        });
        ParallelTransition rotate = new ParallelTransition();

        rotate.getChildren().addAll(movementBy(bottomNode,0,-UNIT_DISTANCE,event -> {bottomNode.bindToParent(parentNode);})
                ,movementBy(topNode,0,UNIT_DISTANCE,event -> {topNode.bindToParent(bottomNode);
                    exchangeNode.bindToParent(topNode);}));
        mainAnimation.getChildren().addAll(highlight,rotate);
        return mainAnimation;
    }

    /**
     * This method is used to hide or show the null node of the leaves nodes.
     * @param isVisible
     *          {@code true} if set the null nodes visible,
     *          {@code false} if set to invisible
     */
    public void setNullNodeVisible(boolean isVisible)
    {
        nullNodeVisible=isVisible;

        //iterate the hash table to find each non-null node's children
        //check whether it is null and change the visibility accordingly
        for(int i = 0;i<hashTable.length;i++)
        {
                GraphicNode thisNode = getNode(i);
                if(thisNode!=null)
                {
                    GraphicNode leftChild = thisNode.getLeftChild();
                    GraphicNode rightChild = thisNode.getRightChild();
                    if(leftChild.isNull())
                    {
                        leftChild.setVisible(isVisible);
                        thisNode.setLeftLinkVisible(isVisible);
                    }
                    if(rightChild.isNull())
                    {
                        rightChild.setVisible(isVisible);
                        thisNode.setRightLinkVisible(isVisible);
                    }
                }
        }
    }


    /**
     * This method is to generate an animation for deletion
     * @param deleteNodeID
     *                      the ID# of the node that is going to be deleted
     * @return
     * a {@link SequentialTransition} that represents the deletion animation of a node
     */
    public SequentialTransition deleteAnimation(int deleteNodeID)
    {
        GraphicNode deleteNode= getNode(deleteNodeID);
        hashTable[deleteNodeID]=null;   //delete from the hash table
        GraphicNode parentNode = deleteNode.getParentNode();
        Boolean isLeft = deleteNode.isLeftChild();  //check if this node is left or right of its parent

        if(deleteNode.getRightChild().isNull() && deleteNode.getLeftChild().isNull()) {   //delete node has no child
            ParallelTransition adjustment = new ParallelTransition();   //do an adjustment after the deletion
            if (parentNode != null) {      //if this is not a root node deletion
                if (isLeft) {
                    parentNode.setLeftLinkVisible(false);
                } else {
                    parentNode.setRightLinkVisible(false);
                }
            }

            ParallelTransition deleteAnimation = new ParallelTransition();
            FadeTransition[] dele = new FadeTransition[3];  //make three fade animation for the node and its two null children
            dele[0] = new FadeTransition(Duration.seconds(.5f), deleteNode);
            dele[1] = new FadeTransition(Duration.seconds(1f), deleteNode.getLeftChild());
            dele[2] = new FadeTransition(Duration.seconds(1f), deleteNode.getRightChild());
            for (int i = 0; i < 3; i++) {
                dele[i].setToValue(0);
                deleteAnimation.getChildren().add(dele[i]);
            }
            outputString("Deleting node "+deleteNode.getValue());
            deleteAnimation.setOnFinished(event -> {
                removeFromCanvas(deleteNode.getLeftChild());
                removeFromCanvas(deleteNode.getRightChild());
                removeFromCanvas(deleteNode);
                if (parentNode != null) {
                    if (isLeft)
                        parentNode.setLeftChild(null);
                    else
                        parentNode.setRightChild(null);
                    addNullNode(parentNode);
                }
            });
            SequentialTransition mainAnimation = new SequentialTransition(deleteAnimation);
            return mainAnimation;
        }

        else    //delete node has only one child
        {

            ParallelTransition deleteAnimation = new ParallelTransition();
            FadeTransition[] dele = new FadeTransition[2];  //make two fade animation for the node and its two null children
            dele[0] = new FadeTransition(Duration.seconds(.5f), deleteNode);

            final GraphicNode swapNode;

            if(deleteNode.getLeftChild().isNull()) {
                dele[1] = new FadeTransition(Duration.seconds(1f), deleteNode.getLeftChild());
                swapNode = deleteNode.getRightChild();
            }
            else {
                dele[1] = new FadeTransition(Duration.seconds(1f), deleteNode.getRightChild());
                swapNode = deleteNode.getLeftChild();
            }

            swapNode.unbindParent();
            Animation movement;
            for (int i = 0; i < 2; i++) {
                dele[i].setToValue(0);
                deleteAnimation.getChildren().add(dele[i]);
            }

            if(isLeft==null)    //this is a root node
            {
            }
            else if(!isLeft)
            {
                parentNode.setRightChild(swapNode);
            }
            else
            {
                parentNode.setLeftChild(swapNode);
            }
            movement = movementTo(swapNode,deleteNode.getX(),deleteNode.getY(),event->{swapNode.bindToParent(parentNode);});
            SequentialTransition mainAnimation = new SequentialTransition(deleteAnimation,movement);
            return mainAnimation;
        }

    }

    /**
     * This method is to generate an animation for swap the data
     * @param topNodeID
     *                  the ID# of the top node
     * @param bottomNodeID
     *                  the ID# of the bottom node
     * @return
     * A {@link SequentialTransition} that represents the animation of data swap
     */
    public SequentialTransition dataSwap(int topNodeID,int bottomNodeID)
    {
            //get all the info
            GraphicNode topNode = getNode(topNodeID);
            GraphicNode bottomNode = getNode(bottomNodeID);
            String valueA = topNode.getValue();
            String valueB = bottomNode.getValue();

            //create two extra text for animation
            Text bottomText = new Text(valueB);
            bottomText.setTranslateX(bottomNode.getTextX());
            bottomText.setTranslateY(bottomNode.getTextY());
            Text topText = new Text(valueA);
            topText.setTranslateX(topNode.getTextX());
            topText.setTranslateY(topNode.getTextY());

        //get a path from top node to bottom node
        Stack<GraphicNode> bottomNodePath = getTraversal(bottomNode,topNode.getParentNode());

        Circle circleA=createHighlightCircle();
        circleA.setTranslateX(topNode.getX());
        circleA.setTranslateY(topNode.getY());
        drawOnCanvas(circleA);
        Circle circleB=createHighlightCircle();    //circleB will traverse on top node
        circleB.setStroke(GraphicNode.HIGHLIGHT_2);

        SequentialTransition bottomTraversal = highlightTraversal(circleB,bottomNodePath);
        bottomTraversal.setOnFinished(event -> {drawOnCanvas(topText);drawOnCanvas(bottomText);
        outputString("Swapping "+topText.getText()+" and "+ bottomText.getText());});

            ParallelTransition textMovement = new ParallelTransition();
            textMovement.getChildren().add(movementTo(topText,bottomNode.getTextX(),bottomNode.getTextY(),null));
            textMovement.getChildren().add(movementTo(bottomText,topNode.getTextX(),topNode.getTextY(),null));
            textMovement.setOnFinished(event -> {
                removeFromCanvas(topText);
                removeFromCanvas(bottomText);
                removeFromCanvas(circleA);
                removeFromCanvas(circleB);
                topNode.setValue(valueB);
                bottomNode.setValue(valueA);});


        SequentialTransition mainAnimation = new SequentialTransition(bottomTraversal,textMovement);
            return mainAnimation;
    }
   /**
    * This method is used to fix color of the graphicNode tree, should be called whenever node color is changed
    * @param nodeID
    * @param isRed
    * {@code true} if the node changes to red, {@code false} if the node changes to black
    **/
    public void recolor(int nodeID, boolean isRed){
    	GraphicNode colorNode = getNode(nodeID);
    	if(isRed){
    		outputString("Set node "+colorNode.getValue()+" to red");
    		colorNode.setColor(GraphicNode.RED);
    	}
    	else{
            outputString("Set node "+colorNode.getValue()+" to black");
    		colorNode.setColor(GraphicNode.BLACK);
    	}
 
    	
    }

    private GraphicNode getNode(int nodeID)
    {
        return hashTable[nodeID];
    }


    private void outputString(String output)
    {

        outputArea.setText(outputArea.getText()+output+"\n");
        outputArea.positionCaret(outputArea.getText().length());

    }

    public GraphicNode[] getHashTable()
    {
        return hashTable;
    }

    public void loadTree(GraphicNode[] newTable,int rootID)
    {
        mainPane.getChildren().clear();
        this.hashTable = newTable;
        for(int i=0;i<hashTable.length;i++)
        {
            if(getNode(i)!=null)
            {
                getNode(i).setLeftLinkVisible(true);

                getNode(i).setRightLinkVisible(true);
                drawOnCanvas(getNode(i));
                addNullNode(getNode(i));

            }
        }
        resetOverlay(getNode(rootID));
    }

    public void clearCanvas()
    {
        mainPane.getChildren().clear();
        hashTable=new GraphicNode[2];
    }



}
