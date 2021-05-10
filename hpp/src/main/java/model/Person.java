package model;


public class Person implements Comparable<Person>{

	private int country;	// 0 for France, 1 for Italy, 2 for Spain
	private int id;
	private double contamination_time;
	private int contaminated_by;
	
	

	



	public Person(int country, int id, double contamination_time, int contaminated_by) {
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
		this.contaminated_by = words[5].equals("unknown") ? -1 : Integer.parseInt(words[5]);
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

	public double getContamination_time() {
		return contamination_time;
	}

	public void setContamination_time(double contamination_time) {
		this.contamination_time = contamination_time;
	}

	public int getContaminated_by() {
		return contaminated_by;
	}

	public void setContaminated_by(int contaminated_by) {
		this.contaminated_by = contaminated_by;
	}
	
	@Override
	public String toString() {
		String count = null;
		switch(country) {
			case 0 :
				count = "FRANCE"; break;
			case 1 :
				count = "ITALY"; break;
			case 2 :
				count = "SPAIN"; break;
		}
		return "Person [country=" + count + ", id=" + id + ", contamination_time=" + contamination_time
				+ ", contaminated_by=" + contaminated_by + "]";
	}

	@Override
	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return Double.compare(this.contamination_time, o.contamination_time);
	}

}
