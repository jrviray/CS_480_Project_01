package cpp.edu.cs480.project06;

public interface Tree<K extends Comparable<K>, V> {
	
	/**
	 * Adds a node to the Red Black tree based on the Key within a <Key, Value>
	 * pair. After adding it calls addFixTree() in order to restore RB
	 * invariant.
	 */
	public void add(K key, V value);
	
	/**
	 * This method removes a target node from the tree based on its key value.
	 * will return an error if the key is not found within the tree. It has
	 * several helper methods in order to account for different cases when
	 * removing from a Red Black cpp.edu.cs480.project06.Tree as well as restoring the invariant of the
	 * Red black tree.
	 */
	public V remove(K key);
	
	/**
	 * Same lookup method as standard BST. This method searches the tree for the
	 * inputted key. if the key does not exist the method returns null.
	 */
	public V lookup(K key);
	
	/**
	 * prints the tree in a tree format. has a recursive function that
	 * stores each level of the tree in a string and formats it so that when
	 * the string is displayed,  it is in a tree format. nil leaves are black.
	 */
	public String toPrettyString();

}
