package cpp.edu.cs480.project06;

import java.io.Serializable;

/**
 * Created by wxy03 on 4/13/2017.
 */
public class SerialNode implements Serializable {

    private double x;
    private double y;
    private boolean isRed;
    private String value;
    private int parentID;
    private int leftChildID;
    private int rightChildID;
    private int hashID;


    public SerialNode(double x,double y,boolean isRed,String value,int hashID)
    {
        this.x=x;
        this.y=y;
        this.isRed=isRed;
        this.value=value;
        this.parentID=-1;
        this.leftChildID=-1;
        this.rightChildID=-1;
        this.hashID=hashID;

    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public boolean isRed()
    {
        return isRed;
    }

    public String getValue()
    {
        return value;
    }

    public int getParentID()
    {
        return parentID;
    }

    public  int getLeftChildID()
    {
        return leftChildID;
    }

    public int getRightChildID()
    {
        return rightChildID;
    }

    public int getHashID()
    {
        return hashID;
    }

    public void setParentID(int ID)
    {
        parentID=ID;
    }

    public void setLeftChildIDID(int ID)
    {
        leftChildID=ID;
    }

    public void setRightChildID(int ID)
    {
        rightChildID=ID;
    }

}
