//package cpp.edu.cs480.project06;
//import javafx.animation.SequentialTransition;
//import javafx.scene.layout.Pane;
//import java.util.Queue;
//
///**
// * Created by wxy03 on 4/9/2017.
// */
//public class Animator {
//
//    private Pane mainPane;
//
//    private GraphicNode[] hashTable;
//
//
//    /**
//     * default constructor
//     * @param mainPane
//     */
//    public Animator(Pane mainPane)
//    {
//        this.mainPane=mainPane;
//        hashTable=new GraphicNode[50];
//    }
//
//
//    /**
//     * This method is used to generate a new Node with its unique ID#.
//     * @param nodeValue
//     *          the data value of the new node
//     * @return
//     *          the assigned ID# of the new node
//     */
//    public int generateNode(int nodeValue)
//    {
//    }
//
//
//    private void drawNode(GraphicNode newNode)
//    {
//        mainPane.getChildren().add(newNode);
//        newNode.toFront();
//    }
//
//    /**
//     * This method is to generate an animation for inserting node left
//     * @param newNodeID
//     * @param parentNodeID
//     * @return
//     */
//    public SequentialTransition insertLeftAnimation(int newNodeID, int parentNodeID)
//    {
//
//    }
//
//    /**
//     * This method is to generate an animation for rotating node left
//     * @param rotateNodeID
//     * @return
//     */
//    public SequentialTransition rotateLeftAnimation(int rotateNodeID)
//    {
//    }
//
//    /**
//     * This method is to generate an animation for rotating node right
//     * @param rotateNodeID
//     * @return
//     */
//    public SequentialTransition rotateRightAnimation(int rotateNodeID)
//    {
//
//    }
//
//    /**
//     * This method is to generate an animation for recoloring
//     * @param nodeID
//     * @param color
//     * @return
//     */
//    public SequentialTransition recolorAnimation(Queue<Integer> nodeID, Queue<Boolean> color)
//    {
//
//    }
//
//    /**
//     * This method is to generate an animation for deletion
//     * @param deleteNodeID
//     * @return
//     */
//    public SequentialTransition deleteAnimation(int deleteNodeID)
//    {
//
//    }
//
//    /**
//     * This method is to generate an animation for swap the data
//     * @param swapNodeID_1
//     * @param swapNodeID_2
//     * @return
//     */
//    public SequentialTransition dataSwap(int swapNodeID_1,int swapNodeID_2)
//    {
//
//    }
//
//    private GraphicNode getNode(int nodeID)
//    {
//        return hashTable[nodeID];
//    }
//
//}
