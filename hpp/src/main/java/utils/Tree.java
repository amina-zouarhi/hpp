package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import model.Person;

public class Tree {


    private Person root;
    private List<Chain> chains;
    private List<Person> where_update;

    private final List<Person> waitingList;

    private Chain[] top_chains;
    private int top_chain_weight;
    private int potential_top_chain_weight;
    private Double last_update;

    public Tree(Person root) {
        this.root = root;
        this.root.setWeight(10);
        this.root.setMotherTree(this);
        this.root.setInTree(true);

        top_chains = new Chain[3];
        chains = new ArrayList<>();
        top_chains[0] = new Chain(root, root);
        chains.add(top_chains[0]);

        top_chain_weight = 10;
        potential_top_chain_weight = 10;

        where_update = new ArrayList<>();
        where_update.add(root);
        waitingList = new ArrayList<>();
        last_update = root.getContamination_time();
    }

    @Override
    public String toString() {
        return "Tree{" +
                "chains=" + chains +
                '}';
    }

    public void addPersonToTree(Person new_person, Person contaminated_by) {

        contaminated_by.addInfected(new_person);
        new_person.setContaminated_by(contaminated_by);
        new_person.setWeight(contaminated_by.getWeight() + 10);
        new_person.setMotherTree(this);
        new_person.setInTree(true);

        if (top_chain_weight < new_person.getWeight()) {
            top_chain_weight = new_person.getWeight();
            potential_top_chain_weight = new_person.getWeight();
        }

        if (contaminated_by.getInfectedPpl().size() == 1) {
            for (Chain c : chains) {
                if (c.getEnd().equals(contaminated_by)) {
                    c.setWeight(new_person.getWeight());
                    c.setEnd(new_person);
                    if (!c.equals(top_chains[0]) && !c.equals(top_chains[1]) && !c.equals(top_chains[2])) {
                        updateTopChains(c);
                    } else {
                        sortTopChains();
                    }
                }
            }
        } else {
            Chain c = new Chain(root, new_person);
            chains.add(c);
            updateTopChains(c);
        }
    }

    public void updateTree(Double actual_ts) {

        chains.clear();
        top_chains = new Chain[3];

        // Update only where it is needed
        List<Person> where_update_now = new ArrayList<>(where_update);
        where_update.clear();
        for (Person p : where_update_now) {
            p.update(actual_ts, 0, root, chains, true);
        }

        top_chain_weight = 0;

        // Remove chains with weight = 0 and get top chain weight
        ListIterator<Chain> iterator = chains.listIterator();
        while (iterator.hasNext()) {
            Chain c = iterator.next();
            top_chain_weight = Integer.max(top_chain_weight, c.getWeight());
            if (c.getEnd().getWeight() == 0) {
                deleteChain(c.getEnd());
                iterator.remove();
            } else {
                updateTopChains(c);
            }
        }

        potential_top_chain_weight = top_chain_weight;
        last_update = actual_ts;
    }


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

    public void deleteTree() {
        for (Person p : where_update) {
            for (Person infect : p.getInfectedPpl()) {
                PersonsHashMap.removePersonFromMap(infect);
            }
            PersonsHashMap.removePersonFromMap(p);
        }
    }


    public void addPersonToWaiting(Person p) {
        p.setMotherTree(this);
        p.setInTree(false);

        waitingList.add(p);
        potential_top_chain_weight += 10;
        last_update = p.getContamination_time();
    }

    public int getWeightOfChainEndingWith(Person end) {
        for (Chain c : chains) {
            if (c.getEnd().equals(end)) {
                return c.getWeight();
            }
        }
        return 0;
    }

    public void updateTopChains(Chain c) {
        if (top_chains[0] == null || c.compareTo(top_chains[0]) > 0) {
            top_chains[2] = top_chains[1];
            top_chains[1] = top_chains[0];
            top_chains[0] = c;
        } else if (top_chains[1] == null || c.compareTo(top_chains[1]) > 0) {
            top_chains[2] = top_chains[1];
            top_chains[1] = c;
        } else if (top_chains[2] == null || c.compareTo(top_chains[2]) > 0) {
            top_chains[2] = c;
        }
    }


    private void sortTopChains() {
        if (top_chains[1] != null) {
            if (top_chains[1].compareTo(top_chains[0]) > 0) {
                Chain temp = top_chains[0];
                top_chains[0] = top_chains[1];
                top_chains[1] = temp;
            } else if (top_chains[2] != null) {
                if (top_chains[2].compareTo(top_chains[1]) > 0) {
                    if (top_chains[2].compareTo(top_chains[0]) > 0) {
                        Chain temp = top_chains[0];
                        top_chains[0] = top_chains[2];
                        top_chains[2] = top_chains[1];
                        top_chains[1] = temp;
                    } else {
                        Chain temp = top_chains[1];
                        top_chains[1] = top_chains[2];
                        top_chains[2] = temp;
                    }
                }
            }
        }
    }

    // Generated method

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

    public List<Person> getWhere_update() {
        return where_update;
    }

    public void setWhere_update(List<Person> where_update) {
        this.where_update = where_update;
    }

    public List<Person> getWaitingList() {
        return waitingList;
    }

    public int getPotential_top_chain_weight() {
        return potential_top_chain_weight;
    }

    public Double getLast_update() {
        return last_update;
    }

    public Chain[] getTop_chains() {
        return top_chains;
    }



}
