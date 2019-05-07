import java.io.*;
import java.util.Scanner;

//Jonathan Burdette
//implements a dictionary that stores words and their definitions and allows the dictionary to be queried

public class BSTDictionary {
	
	private class Node {
		String word;
		String definition;
		Node left;
		Node right;
		
		Node(String word, String definition) {
			this.word = word;
			this.definition = definition;
			left = null;
			right = null;
		}
	}
	
	Node root;
	
	//adds an entry to the dictionary
	public void add(String word, String definition) {
		if(root == null) {
			root = new Node(word, definition);
		} else {
			Node c = root;
			while(true) {
				if(word.compareToIgnoreCase(c.word) < 0) {
					//go left
					if(c.left == null) {
						c.left = new Node(word, definition);
						break;
					} else {
						c = c.left;
					}
				} else if(word.compareToIgnoreCase(c.word) == 0) {
					if(!definition.equals(c.definition)) {
						c.definition = c.definition + "OR: " + definition;
					}
					break;
				} else {
					//go right
					if(c.right == null) {
						c.right = new Node(word, definition);
						break;
					} else {
						c = c.right;
					}
				}
			}
		}
	}
	
	//finds requested word in dictionary
	public Node find(String word) {
		Node c = root;
		while(c.word.compareToIgnoreCase(word) != 0) {
			//go left
			if(word.compareToIgnoreCase(c.word) < 0) {
				c = c.left;
			} else {
				//go right
				c = c.right;
			}
			if(c == null) {
				return null;
			}
		}
		return c;
	}
	
	//uses in-order traversal to print dictionary in alphabetical order
	public void list(Node localRoot) {
		if(localRoot != null) {
			list(localRoot.left);
			System.out.println(localRoot.word + ": " + localRoot.definition);
			list(localRoot.right);
		}
	}
	
	//uses in-order traversal to print words within the specified range
	public void listRange(Node localRoot, String begin, String end) {
		if(localRoot != null) {
			listRange(localRoot.left, begin, end);
			if(localRoot.word.compareToIgnoreCase(begin) >= 0 && localRoot.word.compareToIgnoreCase(end) <= 0) {
				System.out.println(localRoot.word + ": " + localRoot.definition);
			}
			listRange(localRoot.right, begin, end);
		}
	}
	
	//reads and executes commands from a file
	public void load(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			
			//loop through each line in the file 
			while(line != null) {
				if(line.length() > 0) { //don't process blank lines
					String[] contents = line.split(" ");
					StringBuilder sb = new StringBuilder();

					//check commands and execute
					if(contents[0].toLowerCase().equals("add")) {
						if(contents.length >= 3) { 
							for(int j=2; j<contents.length; j++) { 
								sb.append(contents[j] + " ");
							}
							String definition = sb.toString();
							add((contents[1]), definition);
							System.out.println("Entry added.");
						} else {
							System.out.println("Invalid syntax. Enter \"add <word> <definition>\"");
						}
					} else if(contents[0].toLowerCase().equals("find")) {
						if(root != null) {
							if(contents.length >= 2) { 
								Node temp = find(contents[1]);
								if(temp != null) {
									System.out.println(temp.word + ": " + temp.definition);
								} else {
									System.out.println("Word not found");
								}
							} else {
								System.out.println("Invalid syntax. Enter \"find <word>\"");
							}
						} else {
							System.out.println("Dictionary is empty");
						}
					} else if(contents[0].toLowerCase().equals("list") && contents.length == 1) {
						if(root != null) {
							list(root);
						} else {
							System.out.println("Dictionary is empty");
						}
					} else if(contents[0].toLowerCase().equals("list") && contents.length > 1) {
						if(root != null) {
							if(contents.length >= 3) {
								listRange(root, contents[1], contents[2]);
							} else {
								System.out.println("Invalid syntax. Enter \"list <begin word> <end word>\" or just \"list\"");
							}
						} else {
							System.out.println("Dictionary is empty");
						}
					} else if(contents[0].toLowerCase().equals("load")) {
						if(contents.length >= 2) { 
							load(contents[1]);
						} else {
							System.out.println("Invalid syntax. Enter \"load <file name>\"");
						}
					} else if(contents[0].toLowerCase().equals("quit")) {
						System.exit(0);
					} else if(contents[0].toLowerCase().equals("tree")) {
						if(root != null) {
							System.out.println("----Binary Search Tree----");
							tree(root, "");		
						} else {
							System.out.println("Dictionary is empty");
						}
					} else {
						System.out.println("Unknown command");
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.err.println("Error reading file.");
		}
	}
	
	//uses preorder traversal to show tree structure
	public void tree(Node localRoot, String indent) {
		if(localRoot != null) {
			System.out.println(indent + localRoot.word);
			tree(localRoot.left, indent + "   ");
			tree(localRoot.right, indent + "   ");
		}
	}
	
	public static void main(String[] args) {
		
		BSTDictionary d = new BSTDictionary();
				
		//if there are command file names, run through them before entering interactive mode
		if(args.length > 0) {
			for(int i=0; i<args.length; i++) {
				d.load(args[i]);
			}
		} 
		
		//enter interactive mode
		System.out.println("\nTo see list of available commands, type \"commands\"\n");
		
		Scanner keyboard = new Scanner(System.in);
		boolean quit = false;

		//loops prompt until quit is entered
		while(quit == false) {
			System.out.print("Enter a command: ");
			String line = keyboard.nextLine();
			String[] contents = line.split(" ");
			StringBuilder sb = new StringBuilder();

			//check commands and execute
			if(contents[0].toLowerCase().equals("commands")) {
				System.out.println("\nAvailable commands (case-insensitive)");
				System.out.println("-------------------------------------");
				System.out.println("commands");
				System.out.println("Add <word> <definition>");
				System.out.println("Find <word>");
				System.out.println("List");
				System.out.println("List <begin word> <end word>");
				System.out.println("Load <file name>");
				System.out.println("Tree");
				System.out.println("Quit\n");
			} else if(contents[0].toLowerCase().equals("add")) {
				if(contents.length >= 3) { 
					for(int j=2; j<contents.length; j++) { 
						sb.append(contents[j] + " ");
					}
					String definition = sb.toString();
					d.add((contents[1]), definition);
					System.out.println("Entry added.");
				} else {
					System.out.println("Invalid syntax. Enter \"add <word> <definition>\"");
				}
			} else if(contents[0].toLowerCase().equals("find")) {
				if(d.root != null) {
					if(contents.length >= 2) { 
						Node temp = d.find(contents[1]);
						if(temp != null) {
							System.out.println(temp.word + ": " + temp.definition);
						} else {
							System.out.println("Word not found");
						}
					} else {
						System.out.println("Invalid syntax. Enter \"find <word>\"");
					}
				} else {
					System.out.println("Dictionary is empty");
				}
			} else if(contents[0].toLowerCase().equals("list") && contents.length == 1) {
				if(d.root != null) {
					d.list(d.root);
				} else {
					System.out.println("Dictionary is empty");
				}
			} else if(contents[0].toLowerCase().equals("list") && contents.length > 1) {
				if(d.root != null) {
					if(contents.length >= 3) {
						d.listRange(d.root, contents[1], contents[2]);
					} else {
						System.out.println("Invalid syntax. Enter \"list <begin word> <end word>\" or just \"list\"");
					}
				} else {
					System.out.println("Dictionary is empty");
				}
			} else if(contents[0].toLowerCase().equals("load")) {
				if(contents.length >= 2) { 
					d.load(contents[1]);
				} else {
					System.out.println("Invalid syntax. Enter \"load <file name>\"");
				}
			} else if(contents[0].toLowerCase().equals("quit")) {
				System.exit(0);
			} else if(contents[0].toLowerCase().equals("tree")) {
				if(d.root != null) {
					System.out.println("----Binary Search Tree----");
					d.tree(d.root, "");		
				} else {
					System.out.println("Dictionary is empty");
				}
			} else {
				System.out.println("Unknown command");
			}
		}
		keyboard.close();
	}
}
