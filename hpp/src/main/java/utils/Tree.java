package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import model.Person;

/**
 * 
 * Tree class
 *
 */
public class Tree {

	private Person root;
	private List<Chain> chains;
	private List<Person> whereToUpdate;

	private final List<Person> waitingList;

	private Chain[] topChains;
	private int topChainWeight;
	private int potential_topChainWeight;
	private Double lastUpdate;

	public Tree(Person root) {
		this.root = root;
		this.root.setScore(10);
		this.root.setMotherTree(this);
		this.root.setInTree(true);

		topChains = new Chain[3];
		chains = new ArrayList<>();
		topChains[0] = new Chain(root, root);
		chains.add(topChains[0]);

		topChainWeight = 10;
		potential_topChainWeight = 10;

		whereToUpdate = new ArrayList<>();
		whereToUpdate.add(root);
		waitingList = new ArrayList<>();
		lastUpdate = root.getContamination_time();
	}

	@Override
	public String toString() {
		return "Tree{" + "chains=" + chains + '}';
	}

	/**
	 * It adds a person to a tree
	 * 
	 * @param newPerson
	 * @param contaminated_by
	 */
	public void addPersonToTree(Person newPerson, Person contaminated_by) {

		contaminated_by.addInfected(newPerson);
		newPerson.setContaminated_by(contaminated_by);
		newPerson.setScore(contaminated_by.getScore() + 10);
		newPerson.setMotherTree(this);
		newPerson.setInTree(true);

		if (topChainWeight < newPerson.getScore()) {
			topChainWeight = newPerson.getScore();
			potential_topChainWeight = newPerson.getScore();
		}

		if (contaminated_by.getInfectedPpl().size() == 1) {
			for (Chain c : chains) {
				if (c.getEnd().equals(contaminated_by)) {
					c.setWeight(newPerson.getScore());
					c.setEnd(newPerson);
					if (!c.equals(topChains[0]) && !c.equals(topChains[1]) && !c.equals(topChains[2])) {
						updateTopChains(c);
					} else {
						sortTopChains();
					}
				}
			}
		} else {
			Chain c = new Chain(root, newPerson);
			chains.add(c);
			updateTopChains(c);
		}
	}

	/**
	 * It updates the tree using the current contamination time
	 * 
	 * @param actualTs
	 */
	public void updateTree(Double actualTs) {

		chains.clear();
		topChains = new Chain[3];

		// Update only where it is needed
		List<Person> whereToUpdate_now = new ArrayList<>(whereToUpdate);
		whereToUpdate.clear();
		for (Person p : whereToUpdate_now) {
			p.update(actualTs, 0, root, chains, true);
		}

		topChainWeight = 0;

		// Remove chains with weight = 0 and get top chain weight
		ListIterator<Chain> iterator = chains.listIterator();
		while (iterator.hasNext()) {
			Chain c = iterator.next();
			topChainWeight = Integer.max(topChainWeight, c.getWeight());
			if (c.getEnd().getScore() == 0) {
				deleteChain(c.getEnd());
				iterator.remove();
			} else {
				updateTopChains(c);
			}
		}

		potential_topChainWeight = topChainWeight;
		lastUpdate = actualTs;
	}

	/**
	 * takes the root of a chain and removes that chain
	 * 
	 * @param p
	 */
	public void deleteChain(Person p) {
		if (p.getInfectedPpl().isEmpty()) {
			if (!p.equals(root)) {
				p.getContaminated_by().getInfectedPpl().remove(p);
				deleteChain(p.getContaminated_by());
			} else {
				this.root = null;
			}
		}
	}

	/**
	 * deletes the tree
	 */
	public void deleteTree() {
		for (Person p : whereToUpdate) {
			for (Person infect : p.getInfectedPpl()) {
				PersonsHashMap.removePersonFromMap(infect);
			}
			PersonsHashMap.removePersonFromMap(p);
		}
	}

	/**
	 * uses the waiting list to keep a person waiting
	 * 
	 * @param p
	 */
	public void addPersonToWaiting(Person p) {
		p.setMotherTree(this);
		p.setInTree(false);

		waitingList.add(p);
		potential_topChainWeight += 10;
		lastUpdate = p.getContamination_time();
	}

	public int getWeightOfChainEndingWith(Person end) {
		for (Chain c : chains) {
			if (c.getEnd().equals(end)) {
				return c.getWeight();
			}
		}
		return 0;
	}

	/**
	 * Updates the top chains
	 * 
	 * @param c
	 */
	public void updateTopChains(Chain c) {
		if (topChains[0] == null || c.compareTo(topChains[0]) > 0) {
			topChains[2] = topChains[1];
			topChains[1] = topChains[0];
			topChains[0] = c;
		} else if (topChains[1] == null || c.compareTo(topChains[1]) > 0) {
			topChains[2] = topChains[1];
			topChains[1] = c;
		} else if (topChains[2] == null || c.compareTo(topChains[2]) > 0) {
			topChains[2] = c;
		}
	}

	/**
	 * sort by contamination time
	 */
	private void sortTopChains() {
		if (topChains[1] != null) {
			if (topChains[1].compareTo(topChains[0]) > 0) {
				Chain temp = topChains[0];
				topChains[0] = topChains[1];
				topChains[1] = temp;
			} else if (topChains[2] != null) {
				if (topChains[2].compareTo(topChains[1]) > 0) {
					if (topChains[2].compareTo(topChains[0]) > 0) {
						Chain temp = topChains[0];
						topChains[0] = topChains[2];
						topChains[2] = topChains[1];
						topChains[1] = temp;
					} else {
						Chain temp = topChains[1];
						topChains[1] = topChains[2];
						topChains[2] = temp;
					}
				}
			}
		}
	}

	/**
	 * getters and setters
	 */

	public List<Chain> getChains() {
		return chains;
	}

	public void setChains(List<Chain> chains) {
		this.chains = chains;
	}

	public Person getRoot() {
		return root;
	}

	public void setRoot(Person root) {
		this.root = root;
	}

	public List<Person> getWhereToUpdate() {
		return whereToUpdate;
	}

	public void setwhereToUpdate(List<Person> whereToUpdate) {
		this.whereToUpdate = whereToUpdate;
	}

	public List<Person> getWaitingList() {
		return waitingList;
	}

	public int getPotential_topChainWeight() {
		return potential_topChainWeight;
	}

	public Double getLastUpdate() {
		return lastUpdate;
	}

	public Chain[] getTopChains() {
		return topChains;
	}

}
