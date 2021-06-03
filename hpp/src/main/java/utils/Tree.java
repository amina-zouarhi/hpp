package utils;



public class Tree {

	/**the root of the tree
	 * */
	 
    private Node root;
    
    /**Constructor
     * 
     * @param nodePerson
     */
    public Tree(Node nodePerson)
    {
        this.root = nodePerson;
    }


    public Node getRoot() {
  		return this.root;
  	}

  	public void setRoot(Node root) {
  		this.root = root;
  	}

  	/**
  	 * This method finds a node in a tree
  	 * @param node : the node we are trying to find
  	 * @param id : the id we are matching the node to 
  	 * @return Node : the node if we find it and null if not
  	 * */
    public Node findNode(Node node,int id ) {

    	//if we find the node which means that the node's person's id is the id we are looking for
        if (node.getPerson().getId() == id) {
        	//we return the node
            return node;
            
        }
        //if not 
        else {
        	//if the node is not a leaf
        	if(!node.isLeaf())
        	{	
        		//we browse the node's children
            for (Node child: node.getChildren()) {
                Node result = findNode(child,id);
                if (result != null) {
                	//we return the node once it's found
                    return result;
                }
            }
        }
        	
        }
        //if we can't find it we return null
        return null;
    }
    



}
