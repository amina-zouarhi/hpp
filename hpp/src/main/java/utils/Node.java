package utils;


import java.util.ArrayList;
import java.util.List;

import model.Person;

/**
 * Node represents a node in the non-binary Tree
 */
public class Node {
	 /**
		 * parent: to keep track of the chain, this is the parent of the current Node
		 */
	    private Node parent;
		 /**
			 * children: list of all the current Node children
			 */
	    private List<Node> children = null;
		 /**
			 * person: the current node's information; class Person
			 */
	    private Person person;//or any other property that the node should contain, like 'info'
	    /**
		 * Constructor
		 * @param person
		 * @return Node instance
		 */
	    public Node (Person nodePerson)
	    {
	        person = nodePerson; 
	    }
	    /**
		 * Method addChild
		 * @param childNode
		 * @return 
		 */
		public void addChild(Node childNode)
	    {
				if(this.getChildren() == null) {
					this.setChildren( new ArrayList<Node>()); 
					//System.out.println("This parent had no children");
				}
	            childNode.setParent(this);
	            this.getChildren().add(childNode);
			//	System.out.println("We added " + childNode.getPerson().getPerson_id() + " to the parent " + this.getPerson().getPerson_id());

	    }
	    /**
		 * Method isLeaf
		 * @param 
		 * @return boolean
		 */		
		public boolean isLeaf()
		{
			if (this.getChildren() == null)
				return true;
			return false;

		}
		public boolean equals(Node node) {
			if(this.getPerson().equals(node.getPerson()))
				return true;
			return false;
		}


		public Node getParent() {
			return this.parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public List<Node> getChildren() {
			return this.children;
		}

		public void setChildren(List<Node> children) {
			this.children = children;
		}

		public Person getPerson() {
			return this.person;
		}

		public void setPerson(Person person) {
			this.person = person;
		}

		
		
		
}

