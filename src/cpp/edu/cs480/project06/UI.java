package cpp.edu.cs480.project06;

//must rename to controller class

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class represents the User Interface that the user sees when using the
 * Red and Black tree. Contains all of the print statements the user sees and
 * also prints the tree in a tree format for better visualization.
 */
public class UI {
	// can only take numbers that range from -128 to 127, due to taking in type Integer.
	RedBlackTree<Integer, String> tree = new RedBlackTree<Integer, String>();
	Scanner input = new Scanner(System.in);

	/**
	 * This method starts the Red Black tree program.
	 */
	public void start() {
		System.out.println("Welcome to Red/Black cpp.edu.cs480.project06.Tree simulator!");

		while (true) {
			try {
				mainMenu();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input, Please try again.\n\n");
				input.nextLine();
			} catch (NullPointerException e) {
				System.out.println("Invalid key, Please try again.\n\n");
			}
		}

	}

	/**
	 * This method represents the main menu of the Red Black tree user
	 * interface. It has options to add, remove, and lookup the tree based on
	 * the key inputted.
	 */
	public void mainMenu() {
		int userInput = 0;

		System.out.println("Select an option to begin, Enter nothing to quit");
		System.out.println("1. Add to Red/Black cpp.edu.cs480.project06.Tree.\n2. Remove from Red/Black cpp.edu.cs480.project06.Tree.\n3. Look up a value");
		userInput = input.nextInt();

		switch (userInput) {

		case 0:
			input.close();
			System.exit(0);
			break;

		case 1:
			addNode();
			break;

		case 2:
			removeNode();
			break;

		case 3:
			lookupKey();
			break;

		default:
			System.out.println("Invalid input, please try again.");
			break;
		}
	}

	/**
	 * This method adds a kev value pair as a node into the Red Black tree.
	 * Afterwards it displays the tree to confirm that the node has been added.
	 */
	public void addNode() {
		int key = 0;
		String data = "";

		System.out.println();
		System.out.println("Enter an integer key and string value:");
		key = input.nextInt();
		data = input.nextLine();
		input.next();

		tree.add(key, data);
		displayTree();
		System.out.println();
	}

	/**
	 * This method removes a node from the tree based on its key. Afterwards the
	 * tree is updated and displayed again to confirm the removal of the node.
	 */
	public void removeNode() {
		int removeKey = 0;

		System.out.println();
		System.out.println("Enter the key of the node you want to remove.");
		removeKey = input.nextInt();

		
		tree.remove(removeKey);
		displayTree();
		System.out.println("If nothing is displayed, cpp.edu.cs480.project06.Tree is empty!");
		System.out.println();
	}

	/**
	 * This method searches the tree based on the inputted Key.
	 */
	public void lookupKey() {
		int searchKey = 0;

		System.out.println();
		System.out.print("Enter the key of the node to search for:");
		searchKey = input.nextInt();
		
		
		System.out.println("The value of node with key " + searchKey + " is: " + tree.lookup(searchKey));
		System.out.println();
		displayTree();
		System.out.println();
		
	}

	/**
	 * This method runs the toPrettyString method that prints out the tree in a
	 * tree format for the user to see.
	 */
	public void displayTree() {
		System.out.println(tree.toPrettyString());
	}
}
