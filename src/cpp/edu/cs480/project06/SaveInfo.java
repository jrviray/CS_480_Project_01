package cpp.edu.cs480.project06;

import java.io.Serializable;

/**
 * Created by wxy03 on 4/13/2017.
 */
public class SaveInfo implements Serializable {

    private SerialNode[] serialTable;

    private RedBlackTree<Integer,Integer> tree;

    private int rootID;

    public SaveInfo(GraphicNode[] table, RedBlackTree<Integer,Integer> tree, int rootID)
    {

         this.serialTable = serialize(table);
        this.tree=tree;
        this.rootID=rootID;
    }

    public GraphicNode[] getTable()
    {
        GraphicNode[] hashTable = new GraphicNode[serialTable.length];
        for (int i=0;i<hashTable.length;i++)
        {
            if(serialTable[i]!=null)
            {

                SerialNode thisNode = serialTable[i];
                hashTable[i] = new GraphicNode(thisNode.getX(),thisNode.getY(),thisNode.getValue());
                hashTable[i].setHashID(i);
                if(thisNode.isRed())
                hashTable[i].setColor(GraphicNode.RED);
                else
                    hashTable[i].setColor(GraphicNode.BLACK);

            }
        }
        for(int i=0;i<hashTable.length;i++)
        {
            if(hashTable[i]!=null)
            {
                SerialNode thisSerialNode = serialTable[i];
                GraphicNode thisGraphicNode = hashTable[i];
                if(thisSerialNode.getLeftChildID()!=-1)
                    thisGraphicNode.setLeftChild(hashTable[thisSerialNode.getLeftChildID()]);
                if(thisSerialNode.getRightChildID()!=-1)
                    thisGraphicNode.setRightChild(hashTable[thisSerialNode.getRightChildID()]);
                if(thisSerialNode.getParentID()!=-1)
                    thisGraphicNode.bindToParent(hashTable[thisSerialNode.getParentID()]);
            }
        }

        return hashTable;
    }

    public RedBlackTree<Integer,Integer>getTree()
    {
        return tree;
    }

    private SerialNode[] serialize(GraphicNode[] hashTable)
    {
        SerialNode[] serialTable= new SerialNode[hashTable.length];

        for(int i=0;i<serialTable.length;i++)
        {
            if(hashTable[i]!=null)
            {
                GraphicNode thisNode = hashTable[i];
                serialTable[i] = new SerialNode(thisNode.getX(),thisNode.getY(),thisNode.isRed(),thisNode.getValue(),
                        thisNode.getHashID());
                if(thisNode.getParentNode()!=null)
                    serialTable[i].setParentID(thisNode.getParentNode().getHashID());

                if(!thisNode.getLeftChild().isNull())
                    serialTable[i].setLeftChildIDID(thisNode.getLeftChild().getHashID());
                if(!thisNode.getRightChild().isNull())
                    serialTable[i].setRightChildID(thisNode.getRightChild().getHashID());
            }
        }
        return serialTable;
    }

    public int getRootID(){return rootID;}
}
