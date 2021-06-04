package model;

import java.util.List;

import utils.Chain;
import utils.PersonsHashMap;
import utils.Tree;

public class Person implements Comparable<Person> {

	private int country; // 0 for France, 1 for Italy, 2 for Spain
	private int id;
	private Double contamination_time;
	private Person contaminated_by;
	private int contaminatedById;

	private int weight;
	private List<Person> infectedPpl;
	private Tree motherTree;
	private boolean isInTree;

	public Person(int country, int id, Double contamination_time, Person contaminated_by) {
		super();
		this.country = country;
		this.id = id;
		this.contamination_time = contamination_time;
		this.contaminated_by = contaminated_by;
	}

	public Person(int country, String entry) {
		this.country = country;
		// Parser : using .split() is better in this case than using StringTokenizer
		// as we don't collect all the values (words)
		String[] words = entry.split(", ");
		this.id = Integer.parseInt(words[0]);
		this.contamination_time = Double.parseDouble(words[4]);
		this.contaminatedById = words[5].equals("unknown") ? -1 : Integer.parseInt(words[5]);
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getContamination_time() {
		return contamination_time;
	}

	public void setContamination_time(Double contamination_time) {
		this.contamination_time = contamination_time;
	}

	public Person getContaminated_by() {
		return contaminated_by;
	}

	public void setContaminated_by(Person contaminated_by) {
		this.contaminated_by = contaminated_by;
	}

	public int getContaminatedById() {
		return contaminatedById;
	}

	public void setContaminatedById(int contaminatedById) {
		this.contaminatedById = contaminatedById;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public List<Person> getInfectedPpl() {
		return infectedPpl;
	}

	public void setInfectedPpl(List<Person> infectedPpl) {
		this.infectedPpl = infectedPpl;
	}

	public Tree getMotherTree() {
		return motherTree;
	}

	public void setMotherTree(Tree motherTree) {
		this.motherTree = motherTree;
	}

	public boolean isInTree() {
		return isInTree;
	}

	public void setInTree(boolean isInTree) {
		this.isInTree = isInTree;
	}

	@Override
	public String toString() {
		String count = null;
		switch (country) {
		case 0:
			count = "FRANCE";
			break;
		case 1:
			count = "ITALY";
			break;
		case 2:
			count = "SPAIN";
			break;
		}
		return "Person [country=" + count + ", id=" + id + ", contamination_time=" + contamination_time
				+ ", contaminated_by=" + contaminatedById + "]";
	}

	@Override
	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return Double.compare(this.contamination_time, o.contamination_time);
	}

	public void addInfected(Person p) {
		this.infectedPpl.add(p);
	}

	public void update(Double actualTs, int chain_weight, Person root, List<Chain> chains, boolean HasToBeAdded) {
		Double ts_elapsed = actualTs - contamination_time;
		weight = chain_weight;
		if (ts_elapsed <= 604800) {
			weight += 10;
		} else if (ts_elapsed <= 1209600) {
			weight += 4;
		}

		if (weight == 0) {
			PersonsHashMap.removePersonFromMap(this);
		} else if (HasToBeAdded) {
			this.getMotherTree().getWhereToUpdate().add(this);
			HasToBeAdded = false;
		}

		if (this.infectedPpl.isEmpty()) {
			chains.add(new Chain(root, this));
		} else {
			for (Person p : infectedPpl) {
				p.update(actualTs, weight, root, chains, HasToBeAdded);
			}
		}
	}
}
