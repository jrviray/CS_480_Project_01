 package cpp.edu.cs480.project06; /**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodr�guez
 *
 * Programming Assignment #3
 *
 * This assignment details the implementation of a 
 * Red Black Binary search tree in java. A Red Black tree
 * is a form of self balancing binary search tree that supports
 * a worst case time complexity of O(logn).
 *
 * @author Felix Zhang 
 *   
 */

import java.util.*;

public class RedBlackTree<K extends Comparable<K>, V> implements Tree<K, V> {

	protected final static boolean RED = true;
	protected final static boolean BLACK = false;
	protected Node root;
	protected String prettyString = "";
	protected Queue<Instruction> info = new LinkedList<Instruction>();

	/**
	 * Adds a node to the Red Black tree based on the Key within a <Key, Value>
	 * pair. After adding it calls addFixTree() in order to restore RB
	 * invariant.
	 */
	public void add(K key, V value) {
		Node current = root;
		Node parent = null;

		if (root == null) {
			root = new Node(key, value, RED);
			root.color = BLACK;
			info.add(new Instruction("add " + key + " null " + "null "));
		} else {
			while (current.key != null) {
				parent = current;
				current = (key.compareTo(current.key) > 0) ? current.rightChild : current.leftChild;

			}
			if (current.key == null) {
				if (key.compareTo(parent.key) > 0) {
					current = new Node(key, value, RED);
					current.parent = parent;
					parent.rightChild = current;
				} else if (key.compareTo(parent.key) < 0) {
					current = new Node(key, value, RED);
					current.parent = parent;
					parent.leftChild = current;
				}
                info.add(new Instruction("add " + key + " " + current.isLeftChild() + " " + current.parent.getData()));
                addFixTree(current);
			}
		}
	}

	/**
	 * Main fixTree method for adding a node. Has several helper methods. for
	 * checking and fixing the tree once a node is added.
	 */
	public void addFixTree(Node target) {
		if (target == root || target.parent.color == BLACK) {
			return;
		} else if (findUncle(target).color == RED) {
			recolor(target);

		} else if (findUncle(target).color == BLACK) {
			if (target.parent.isLeftChild() && target.isLeftChild()) {
				info.add(new Instruction("leftRotate " + target.getData() + " "));
				addLeftLeftCase(target);
			} else if (target.parent.isLeftChild() && !target.isLeftChild()) {
				info.add(new Instruction("leftRotate " + target.getData()));
				info.add(new Instruction("rightRotate " + target.getData()));
				addLeftRightCase(target);
			} else if (!target.parent.isLeftChild() && !target.isLeftChild()) {
				info.add(new Instruction("leftRotate " + target.getData()));
				addRightRightCase(target);
			} else if (!target.parent.isLeftChild() && target.isLeftChild()) {
				info.add(new Instruction("rightRotate " + target.getData()));
				info.add(new Instruction("leftRotate " + target.getData()));
				rightLeftCase(target);
			}
		}
	}

	/**
	 * Helper method for fixTree(). recolors the nodes in order to restore RB
	 * invariant.
	 */
	public void recolor(Node target) {
		if (target == root || target.parent.color == BLACK) {
			return;
		}

		if (findUncle(target).color == RED) {
			Node uncle = findUncle(target);
			target.parent.color = BLACK;
			info.add(new Instruction("recolor " + target.parent.getData() + " BLACK"));
			uncle.color = BLACK;
			info.add(new Instruction("recolor " + uncle.getData() + " BLACK"));
			uncle.parent.color = RED;
			info.add(new Instruction("recolor " + uncle.parent.getData() + " RED"));
			root.color = BLACK;
			info.add(new Instruction("recolor " + root.getData() + " BLACK"));
			
			recolor(target.parent.parent);
		} else if (target.color == RED && target.parent.color == RED) {
			addFixTree(target);
		}
	}

	/**
	 * This method is a helper method that restores Red Black invariant within
	 * the tree when adding a new node. Accounts for the case where the added
	 * node is red and its parent node is also red. Both nodes are left childs.
	 */
	public void addLeftLeftCase(Node target) {
		
		if (target.parent.isLeftChild() && target.isLeftChild()) {
			
			rightRotate(target.parent.parent);
			if (target.color == RED && target.parent.color == RED) {
				if (target.isLeftChild()) {
					target.parent.color = BLACK;
					target.parent.rightChild.color = RED;
				} else {
					target.parent.color = BLACK;
					target.parent.leftChild.color = RED;
				}
			}
			root.parent = null;
		}
	}

	/**
	 * This method is a helper method that restores Red Black invariant within
	 * the tree when adding a ndoe. It accounts for the case where the added
	 * node is an internal node of a left child.
	 */
	public void addLeftRightCase(Node target) {
		if (target.parent.isLeftChild() && !target.isLeftChild()) {
			leftRotate(target.parent);
			rightRotate(target.parent);
			target = target.leftChild;

			if (target.color == RED && target.parent.color == RED) {
				if (target.isLeftChild()) {
					target.parent.color = BLACK;
					target.parent.rightChild.color = RED;
				} else {
					target.parent.color = BLACK;
					target.parent.leftChild.color = RED;
				}
			}
		}
		root.parent = null;
	}

	/**
	 * This method is a helper method that restores Red Black invariant within
	 * the tree when adding a new node. This case is a mirror case of the Left
	 * Left case, where the added node is an external child of a right child
	 * parent node.
	 */
	public void addRightRightCase(Node target) {
		if (!target.isLeftChild() && !target.isLeftChild()) {
			leftRotate(target.parent.parent);

			if (target.color == RED && target.parent.color == RED) {
				if (target.isLeftChild()) {
					target.parent.color = BLACK;
					target.parent.rightChild.color = RED;
				} else {
					target.parent.color = BLACK;
					target.parent.leftChild.color = RED;
				}
			}
		}
		root.parent = null;
	}

	/**
	 * This method is a helper method that restores Red Black invariant within
	 * the tree when adding a new node. This case is a mirror case of the left
	 * right case, the added node is an internal node with its parent being a
	 * right child node.
	 */
	public void rightLeftCase(Node target) {
		if (!target.parent.isLeftChild() && target.isLeftChild()) {
			rightRotate(target.parent);
			leftRotate(target.parent);
			target = target.rightChild;

			if (target.color == RED && target.parent.color == RED) {
				if (target.isLeftChild()) {
					target.parent.color = BLACK;
					target.parent.rightChild.color = RED;
				} else {
					target.parent.color = BLACK;
					target.parent.leftChild.color = RED;
				}
			}
		}
		root.parent = null;
	}

	/**
	 * This method is a helper method to both the add and remove functions.
	 * Rotates nodes in the tree to the right one time based on its target node.
	 */
	public void rightRotate(Node target) {
		Node temp = target.leftChild; // 3
		if (target == root) {
			root.leftChild = temp.rightChild;
			temp.rightChild.parent = root;
			temp.rightChild = root;
			root.parent = temp;
			root = temp;
			return;
		}
		if (target.isLeftChild()) {
			target.parent.leftChild = temp; // 9=3
		} else {
			target.parent.rightChild = temp;
		}
		target.leftChild = temp.rightChild;
		temp.rightChild.parent = target;
		temp.parent = target.parent;
		temp.rightChild = target;
		temp.rightChild.parent = target;
		target.parent = temp;
	}

	/**
	 * This method is a helper meth0od to both the add and remove functions.
	 * Rotates nodes in the tree to the left one time based on its target node.
	 */
	public void leftRotate(Node target) {
		Node temp = target.rightChild;

		if (target == root) {
			root.rightChild = temp.leftChild;
			temp.leftChild.parent = root;
			temp.leftChild = root;
			root.parent = temp;
			root = temp;
			return;
		}
		if (target.isLeftChild()) {
			target.parent.leftChild = temp;
		} else {
			target.parent.rightChild = temp;
		}
		target.rightChild = temp.leftChild;
		temp.leftChild.parent = target;
		temp.parent = target.parent;
		temp.leftChild = target;
		temp.leftChild.parent = target;
		target.parent = temp;

	}

	/**
	 * Helper method for fixTree(). searches and returns the uncle.
	 */
	public Node findUncle(Node target) {
		Node uncle = null;
		if (target.parent.parent.leftChild != null && target.parent.parent.leftChild == target.parent) {
			uncle = target.parent.parent.rightChild;
		} else {
			uncle = target.parent.parent.leftChild;
		}
		return uncle;
	}

	/**
	 * This method removes a target node from the tree based on its key value.
	 * will return an error if the key is not found within the tree. It has
	 * several helper methods in order to account for different cases when
	 * removing from a Red Black cpp.edu.cs480.project06.Tree as well as restoring the invariant of the
	 * Red black tree.
	 */
	public V remove(K key) {
		// need to implement standard BST delete then have
		// remove fix tree in order to restore RB invariant
		V remNode = null;
		Node current = root;

		if (lookup(key) == null) {
			throw new RuntimeException("Key not found.");
		}
		if (key.compareTo(root.key) == 0 && (root.leftChild.key == null && root.rightChild.key == null)) {
			remNode = root.data;
			root = null;
		} else {

			while (current.key != key) {
				current = (key.compareTo(current.key) > 0) ? current.rightChild : current.leftChild;
			}

			remNode = current.data;

			if (current.key == key) {
				if (current.leftChild.key == null && current.rightChild.key == null) {
					deleteCaseOne(current);
				} else if (current.leftChild.key != null && current.rightChild.key != null) {
					deleteCaseTwo(current);
				} else if (current.leftChild.key != null || current.rightChild.key != null) {
					deleteCaseThree(current);
				}
			}

		}

		return remNode;
	}

	/**
	 * Helper method for case one deletion, recolors the nodes after rotations
	 * have been made.
	 */
	public void deleteRecolor(Node target) {
		if (target.color == RED && target.isLeftChild()) {
			if (target.parent.rightChild.color == RED) {
				target.color = BLACK;
				info.add(new Instruction("recolor " + target.getData() + " BLACK"));
				target.parent.color = RED;
				info.add(new Instruction("recolor " + target.parent.getData() + " RED"));
				target.parent.rightChild.color = BLACK;
				info.add(new Instruction("recolor " + target.parent.rightChild.getData() + " BLACK"));
			}
		} else if (target.color == RED && !target.isLeftChild()) {
			if (target.parent.leftChild.color == RED) {
				target.color = BLACK;
				info.add(new Instruction("recolor " + target.getData() + " BLACK"));
				target.parent.color = RED;
				info.add(new Instruction("recolor " + target.parent.getData() + " RED"));
				target.parent.leftChild.color = BLACK;
				info.add(new Instruction("recolor " + target.parent.leftChild.getData() + " BLACK"));
			} else {
				target.color  = RED;
				info.add(new Instruction("recolor " + target.getData() + " RED"));
			}
		} else if (target.color == BLACK && target.parent == root) {
	
			target.color = RED;
			info.add(new Instruction("recolor " + target.getData() + " RED"));
		}
	}
	
	/**
	 * Deletion case for when the target node has no children. Also incorporates
	 * if target node's children are black and at least one of sibling's
	 * children is red. Right Right case for delete.
	 */
	public void deleteCaseOne(Node target) {
		if (target.color == RED) {
			if (target.isLeftChild()) {
				info.add(new Instruction("remove " + target.getData()));
				target.parent.leftChild = new Node();
			} else {
				target.parent.rightChild = new Node();
				info.add(new Instruction("remove " + target.getData()));
			}
		} else {
			if (target.isLeftChild()) {
				target.parent.leftChild = new Node();
				target = target.parent;
				info.add(new Instruction("remove " + target.getData()));
				leftRotate(target);
				info.add(new Instruction("leftRotate " + target.getData()));
				deleteRecolor(target);
			} else {
				target.parent.rightChild = new Node();
				target = target.parent;
				info.add(new Instruction("remove " + target.getData()));
				
				info.add(new Instruction("rightRotate " + target.getData()));
				rightRotate(target);
				deleteRecolor(target);
			}
		}

	}



	/**
	 * Deletion case for when the target node has two children.
	 */
	public void deleteCaseTwo(Node target) {
		Node replaceNode = findInorderSuccessor(target);
		/*
		 * if (target.isLeftChild()) { replaceNode.parent.rightChild =
		 * replaceNode.rightChild; } else { replaceNode.parent.leftChild =
		 * replaceNode.rightChild; } replaceNode.rightChild.parent =
		 * replaceNode.parent;
		 */

		if (target == root) {
			if (root.rightChild == replaceNode) {
				info.add(new Instruction("swap " + root.getData() + " " + replaceNode.getData()));
				//help me
				replaceNode.parent = replaceNode;
				replaceNode.leftChild = root.leftChild;
				replaceNode.rightChild = replaceNode;
				root.leftChild.parent = replaceNode;
				info.add(new Instruction("remove " + root.getData()));
				
				replaceNode.rightChild = new Node();
				root = replaceNode;
				rightRotate(root);
				info.add(new Instruction("rightRotate " + root.getData()));
				deleteRecolor(root.rightChild);
				root.color = BLACK;
				info.add(new Instruction("recolor " + root.getData() + " BLACK"));
				root.parent = null;
				return;
			} else {
				replaceNode.parent.leftChild = replaceNode.rightChild;
				root = replaceNode;
				replaceNode.leftChild = target.leftChild;
				replaceNode.rightChild = target.rightChild;
				root.color = BLACK;
				info.add(new Instruction("recolor " + root.getData() + " BLACK"));
				root.parent = null;
				return;
			}
		} else {
			if (target.isLeftChild()) {
				target.parent.leftChild = replaceNode;
				replaceNode.parent = target.parent;
				deleteRecolor(replaceNode);
				
			} else {

				target.parent.rightChild = replaceNode;
				replaceNode.parent = target.parent;
				
				deleteRecolor(replaceNode);
			
			}
		}


		if (target.rightChild.equals(replaceNode)) {
			info.add(new Instruction("swap " + replaceNode.getData() + " " + target.getData()));
			info.add(new Instruction("remove " + target.getData()));
			replaceNode.leftChild = target.leftChild;
			replaceNode.rightChild = target.leftChild;
			target.leftChild.parent = replaceNode;
			replaceNode.rightChild = new Node();
			root.parent = null;
		} else {
			info.add(new Instruction("swap " + replaceNode.getData() + " " + target.getData()));
			info.add(new Instruction("remove " + target.getData()));

			replaceNode.rightChild = target.rightChild;
			replaceNode.leftChild = target.leftChild;
			target.leftChild.parent = replaceNode;
			target.rightChild.parent = replaceNode;
			root.parent = null;
		}

	}

	/**
	 * Deletion case for when the target node has at most one children.
	 */
	public void deleteCaseThree(Node target) {

		if (target.leftChild.key != null && target.rightChild.key == null) {
			if (target.isLeftChild()) {
				info.add(new Instruction("remove " + target.getData()));
				target.parent.leftChild = target.leftChild;
				target.leftChild.color = BLACK;
				info.add(new Instruction("recolor " + target.leftChild + " BLACK"));
			} else {
				info.add(new Instruction("remove " + target.getData()));
				target.parent.rightChild = target.leftChild;
				target.leftChild.color = BLACK;
				info.add(new Instruction("recolor " + target.leftChild + " BLACK"));
			}
		} else if (target.rightChild.key != null && target.leftChild.key == null) {
			if (target.isLeftChild()) {
				info.add(new Instruction("remove " + target.getData()));
				target.parent.leftChild = target.rightChild;
				target.rightChild.color = BLACK;
				info.add(new Instruction("recolor " + target.rightChild + " BLACK"));
			} else {
				info.add(new Instruction("remove " + target.getData()));
				target.parent.rightChild = target.rightChild;
				target.rightChild.parent = target.parent;
				target.rightChild.color = BLACK;
				info.add(new Instruction("recolor " + target.rightChild + " BLACK"));

			}
		}

	}

	

	/**
	 * Works for the most part. This method is a helper method to removing a
	 * node from a Red Black tree. it finds the in order successor of the target
	 * node.
	 */
	public Node findInorderSuccessor(Node target) {
		Node temp = target;
		temp = temp.rightChild;

		while (temp.leftChild.key != null) {
			temp = temp.leftChild;
		}

		return temp;
	}

	/**
	 * Same lookup method as standard BST. This method searches the tree for the
	 * inputted key. if the key does not exist the method returns null.
	 */
	public V lookup(K key) {
		Node current = root;
		V returnValue = null;
		boolean done = false;

		while (!done) {
			if (current.key == key) {
				returnValue = current.data;
				done = true;
			} else if (current.key.compareTo(key) > 0) {
				current = current.leftChild;
			} else {
				current = current.rightChild;
			}
		}
		return returnValue;
	}

	/**
	 * prints the tree in a tree format. has a recursive function that
	 * stores each level of the tree in a string and formats it so that when
	 * the string is displayed,  it is in a tree format. nil leaves are black.
	 */
	public String toPrettyString() {
		prettyString = "";
		int maxLevel = maxLevel(root);

		treeMakeOver(Collections.singletonList(root), 1, maxLevel);
		return prettyString;
	}

	/**
	 * Helper method for toPrettyString. Stores each level in the tree
	 * in a string and then is used in toPrettyString to display the tree.
	 * Based on each level the node is stored in a list starting with the root.
	 * after the node is added to the output string its left and right children are then 
	 * added to another list which is then is in the recursive call of treeMakerOver. This
	 * repeats until each node has no more children, and the function is then stopped. Has helper
	 * methods to determine the height of the tree as well as to print the spaces required
	 * for the tree format.
	 */
	public void treeMakeOver(List<Node> startTarget, int level, int maxLevel) {
		int floor = maxLevel - (level - 1);
		int edgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
		int firstSpaces = (int) Math.pow(2, (floor)) + 1;
		int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

		if (startTarget.isEmpty() || checkNullElements(startTarget)) {
			return;
		}

		prettyString += blankSpace(firstSpaces - 3);

		List<Node> nodeList = new ArrayList<Node>();
		for (Node node : startTarget) {
			if (node != null) {
				prettyString += node;
				nodeList.add(node.leftChild);
				nodeList.add(node.rightChild);
			} else {
				nodeList.add(null);
				nodeList.add(null);
				prettyString += "  ";
			}

			prettyString += blankSpace(betweenSpaces - 1 );

		}
		prettyString += '\n';

		for (int i = 1; i <= edgeLines; i++) {
			for (int j = 0; j < startTarget.size(); j++) {

				prettyString += blankSpace(firstSpaces - i - 2); // changes the spacing of edges

				if (startTarget.get(j) == null) {
					prettyString += blankSpace(edgeLines + edgeLines + i - 1 );
					continue;
				}

				if (startTarget.get(j).leftChild != null) {
					prettyString += "/";
				} else {
					prettyString += " ";
				}

				prettyString += blankSpace(i + i - 1);

				if (startTarget.get(j).rightChild != null) {
					prettyString += "\\";
				} else {
					prettyString += " ";
				}

				prettyString += blankSpace(edgeLines + edgeLines - i + 1);

			}

			prettyString += '\n';
		}
		treeMakeOver(nodeList, level + 1, maxLevel);
	}

	/**
	 * Checks if the elements in the list are null. It is used as a checkpoint in 
	 * the base case of the recursive function to determine if the function should 
	 * keep recurring or not. The function is supposed to terminate once all elements
	 * inside are null, meaning that the last level of the tree has been printed.
	 */
	public boolean checkNullElements(List<Node> list) {
		for (Object object : list) {
			if (object != null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method prints the blank space required to format
	 * the tree. used to format the spaces inside the tree.
	 */
	public String blankSpace(int count) {
		String emptyString = "";

		for (int i = 0; i < count; i++) {
			emptyString += " ";
		}

		return emptyString;
	}

	/**
	 * Helper method to determine the max height of a given tree given the root of
	 * the tree.
	 */
	public int maxLevel(Node target) {
		if (target == null) {
			return 0;
		}

		return Math.max(maxLevel(target.leftChild), maxLevel(target.rightChild)) + 1;
	}

	/**
	 * This class represents a node in the Red Black tree sequence. Each node
	 * contans references to its right child, left child, as well as parent.
	 * Every time a node is created, its left and right child is set to null
	 * nodes as per the structure of a Red Black tree.
	 */
	protected class Node {
		// delete data because we only want the key (which is the number)
		protected K key;
		protected V data;
		protected Node leftChild, rightChild, parent;;
		protected boolean color = BLACK;

		/**
		 * This method returns true if the target node has a left child or not.
		 */
		public boolean hasLeftChild() {
			if (this.leftChild != null) {
				return true;
			}

			return false;
		}

		/**
		 * This method returns true if the target node has a left child or not.
		 */
		public boolean hasRightChild() {
			if (this.rightChild != null) {
				return true;
			}

			return false;
		}

		/**
		 * Null node constructor, where key and value are both null and color of
		 * the null node is set to black.
		 */
		public Node() {
			this.key = null;
			this.data = null;
			this.color = BLACK;
		}

		/**
		 * This is the main constructor for creating anode The left and right
		 * child are set to be null nodes as per the Red Black tree structure.
		 */
		public Node(K key, V data, boolean color) {
			this.key = key;
			this.data = data;
			this.color = color;
			leftChild = new Node();
			rightChild = new Node();
		}

		/**
		 * Overrides the default compareTo method; instead of comparing two
		 * nodes, it compares the nodes' key value. This is mainly used in
		 * traversing the tree.
		 */
		public int compareTo(Node node) {
			return this.key.compareTo(node.key);
		}

		/**
		 * This method checks if the target node is a left Child. If it is, it
		 * returns true. if not, false.
		 */
		public boolean isLeftChild() {
			if (this.parent.leftChild == this) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * This method returns the color of the node for use in the toString
		 * method. Necessary for error checking and making sure the tree is
		 * structured correctly.
		 */
		public String nodeColor() {
			String color = "";
			if (this.color == RED) {
				color = "R";
			} else if (this == null || this.color == BLACK) {
				color = "B";
			}
			return color;
		}
		

		/**
		 * This method returns a string that shows the key pertaining to the
		 * target node as well as the node's color. Mainly used for error
		 * checking and toPrettyString method.
		 */
		public String toString() {
			String retString = "";

			if (this.key == null) {
				retString = "nil";
			} else {
				retString = "" +this.key  + nodeColor()  ;
			}
			return retString;
		}
		
		public V getData() {
			return data;
		}

	}
   protected class Instruction {
	   String instruction;
	   
	   
	   public Instruction(String input) {
		   instruction = input;

	   }
	   
	   public String getInstruction() {
		   return instruction;
	   }
   }
}
