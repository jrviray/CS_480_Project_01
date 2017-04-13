/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.edu.cs480.project06;

/**
 *
 * @author Hunter
 */
public class Instruction<V> {
	   String instr;
	   V ID;
           //whether the node is going to be a left child OR right child, OR defines type of rotation
           //left is true, false is right
           boolean leftRight;
           //black false, red true
           //for recoloring, default is always red (true)
           boolean color = true;
           V parentID;
           V nodeAID;
           V nodeBID;
           String error;
	   
	   public Instruction(String instr, V ID, boolean LR, boolean c, V PID, V AID, V BID, String e) {
		   this.instr = instr;
                   this.ID = ID;
                   this.leftRight = LR;
                   this.color = c;
                   this.parentID = PID;
                   //for swapping instruction
                   this.nodeAID = AID;
                   this.nodeBID = BID;
                   //for fix output
                   this.error = e;
	   }
	   public void setInstr(String in) {
               instr = in;
           }
	   public String getInstruction() {
		   return instr;
	   }
           public V getID() {
               return ID;
           }
           public void setID(V in) {
               ID = in;
           }
           public boolean getLR() {
               return leftRight;
           }
           public void setLR(boolean c) {
               leftRight = c;
           }
           //red true black false
           public boolean getColor() {
               return color;
           }
           public void setColor(boolean c) {
               color = c;
           }
           public V getParentID() {
               return parentID;
           }
           public void setParentID(V p) {
               parentID = p;
           }
           public V getNodeAID() {
               return nodeAID;
           }
           public void setNodeAID(V a) {
               nodeAID = a;
           }
           public V getNodeBID() {
               return nodeBID;
           }
           public void setNodeBID(V b) {
               nodeBID = b;
           }
           public void setError(String s) {
               error = s;
           }
           public String getError() {
               return error;
           }
           public String toString() {
               return instr + " " + ID + " " + leftRight + " " + color + " " + parentID + " " + nodeAID + " " + nodeBID + " " + error;
           }
   }
