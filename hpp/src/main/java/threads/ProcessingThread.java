package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.BlockingQueue;

import model.Person;
import utils.Chain;
import utils.PersonsHashMap;
import utils.Tree;

public class ProcessingThread implements Runnable{

	BlockingQueue<Person> blockingQueueRead;
	private BlockingQueue<String> blockingQueueWrite;
	boolean readerIsAlive;

	List<Tree> trees;
	Chain top1stChain = null;
	Chain top2ndChain = null;
	Chain top3rdChain = null;
	int MinTopChain = 0;

	public ProcessingThread(BlockingQueue<Person> blockingQueueRead, BlockingQueue<String> blockingQueueWrite,
			boolean readerIsAlive) {
		this.blockingQueueRead = blockingQueueRead;
		this.setBlockingQueueWrite(blockingQueueWrite);
		this.trees = new ArrayList<>();
		this.readerIsAlive = readerIsAlive;
	}

	
	/**
	 * The processing thread main function
	 */
	public void run() {
		addNewPerson();
	}

	public void addNewPerson() {

		int i = 0;

		while (readerIsAlive || !blockingQueueRead.isEmpty()) {
			if (!blockingQueueRead.isEmpty()) {
				try {
					if (i % 1024 == 0) {
						System.out.println(i);
					}
					i++;

					Person newPerson = blockingQueueRead.take();

					// Update Part
					MinTopChain = 0;
					if (top1stChain != null) {
						top1stChain.getRoot().getMotherTree().updateTree(newPerson.getContamination_time());
						Tree t = top1stChain.getRoot().getMotherTree();
						MinTopChain = t.getWeightOfChainEndingWith(top1stChain.getEnd());
					}
					if (top2ndChain != null) {
						top2ndChain.getRoot().getMotherTree().updateTree(newPerson.getContamination_time());
						Tree t = top2ndChain.getRoot().getMotherTree();
						MinTopChain = Integer.min(MinTopChain, t.getWeightOfChainEndingWith(top2ndChain.getEnd()));
					}
					if (top3rdChain != null) {
						top3rdChain.getRoot().getMotherTree().updateTree(newPerson.getContamination_time());
						Tree t = top3rdChain.getRoot().getMotherTree();
						MinTopChain = Integer.min(MinTopChain, t.getWeightOfChainEndingWith(top3rdChain.getEnd()));
					}

					top1stChain = top2ndChain = top3rdChain = null;

					// Update trees
					List<Tree> new_trees = new ArrayList<>();
					ListIterator<Tree> treeIterator = trees.listIterator();
					while (treeIterator.hasNext()) {
						Tree t = treeIterator.next();

						// Only for trees which have a potential
						if (t.getPotential_topChainWeight() + 10 >= MinTopChain) {

							// Add every person in waiting list
							ListIterator<Person> personIterator = t.getWaitingList().listIterator();
							while (personIterator.hasNext()) {
								Person personToAdd = personIterator.next();
								Person contamined_by = PersonsHashMap
										.getPersonWithId(personToAdd.getContaminatedById());

								if (contamined_by == null) {
									new_trees.add(new Tree(personToAdd));
								} else {
									Tree tree_to_update = contamined_by.getMotherTree();
									tree_to_update.updateTree(personToAdd.getContamination_time());

									if (contamined_by.getWeight() == 0) {
										new_trees.add(new Tree(personToAdd));
									} else {
										tree_to_update.addPersonToTree(personToAdd, contamined_by);
									}
								}
								personIterator.remove();
							}
							t.updateTree(newPerson.getContamination_time());

							if (t.getChains().isEmpty())
								treeIterator.remove();

							// Update top chains
							for (Chain c : t.getTopChains()) {
								updateTopChains(c);
							}
						}
						if (newPerson.getContamination_time() - t.getLastUpdate() > 1209600) {
							t.deleteTree();
							treeIterator.remove();
						}
					}
					trees.addAll(new_trees);

					// Add person to HashMap
					PersonsHashMap.addPersonToMap(newPerson);

					Tree treeModified = null;
					// If the person is contaminated by someone unknown
					if (newPerson.getContaminatedById() == -1) {
						treeModified = new Tree(newPerson);
						trees.add(treeModified);
					} else {

						// If we found the person who contaminated the new person
						Person contaminated_by = PersonsHashMap.getPersonWithId(newPerson.getContaminatedById());

						// We have 3 cases here :
						// First, IF the contaminator does not exist : we create a tree
						// ELSE, IF the person exist (so he has a weight != 0), then IF the tree has a
						// potential to be in the top 3,
						// we add him in the tree, OTHERWISE we had him iun the waiting list
						// ELSE, IF the contaminator exist but he is not in a tree (so he is in a
						// waiting list of a tree),
						// we need to had the newPerson in the waiting list

						if (contaminated_by == null) {
							treeModified = new Tree(newPerson);
							trees.add(treeModified);
						} else if (contaminated_by.isInTree()) {
							// If the tree has no potential to be in top 3, we put the new person in the
							// waiting list,
							// otherwise we add him in the tree
							treeModified = contaminated_by.getMotherTree();
							if (treeModified.getPotential_topChainWeight() + 10 >= MinTopChain) {
								treeModified.addPersonToTree(newPerson, contaminated_by);
							} else {
								treeModified.addPersonToWaiting(newPerson);
							}
						} else if (!contaminated_by.isInTree()) {
							treeModified = contaminated_by.getMotherTree();
							treeModified.addPersonToWaiting(newPerson);
						}
					}

					// Update top chains only with the tree which has been modified
					if (treeModified != null) {
						Chain[] topThreeChains = treeModified.getTopChains();
						for (Chain c : topThreeChains) {
							if (c != null) {
								if (!c.equals(top1stChain) && !c.equals(top2ndChain) && !c.equals(top3rdChain))
									updateTopChains(c);
								else
									sortTopChains();
							}
						}
					}

					StringBuilder sb = new StringBuilder();

					if (top1stChain != null)
						sb.append(top1stChain.toString());
					if (top2ndChain != null)
						sb.append(top2ndChain.toString());
					if (top3rdChain != null)
						sb.append(top3rdChain.toString());

					getBlockingQueueWrite().put(sb.toString());
					System.out.println(getBlockingQueueWrite().toString());

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateTopChains(Chain c) {
		if (c == null)
			return;
		if (top1stChain == null || c.compareTo(top1stChain) > 0) {
			top3rdChain = top2ndChain;
			top2ndChain = top1stChain;
			top1stChain = c;
		} else if (top2ndChain == null || c.compareTo(top2ndChain) > 0) {
			top3rdChain = top2ndChain;
			top2ndChain = c;
		} else if (top3rdChain == null || c.compareTo(top3rdChain) > 0) {
			top3rdChain = c;
		}
	}

	private void sortTopChains() {
		if (top2ndChain != null) {
			if (top2ndChain.compareTo(top1stChain) > 0) {
				Chain temp = top1stChain;
				top1stChain = top2ndChain;
				top2ndChain = temp;
			} else if (top3rdChain != null) {
				if (top3rdChain.compareTo(top2ndChain) > 0) {
					if (top3rdChain.compareTo(top1stChain) > 0) {
						Chain temp = top1stChain;
						top1stChain = top3rdChain;
						top3rdChain = top2ndChain;
						top2ndChain = temp;
					} else {
						Chain temp = top2ndChain;
						top2ndChain = top3rdChain;
						top3rdChain = temp;
					}
				}
			}
		}

	}


	public BlockingQueue<String> getBlockingQueueWrite() {
		return blockingQueueWrite;
	}


	public void setBlockingQueueWrite(BlockingQueue<String> blockingQueueWrite) {
		this.blockingQueueWrite = blockingQueueWrite;
	}
}
