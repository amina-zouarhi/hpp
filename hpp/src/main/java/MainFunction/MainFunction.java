package MainFunction;

import java.io.FileReader;

public class MainFunction {

	
	private FileReader franceCsv;
	private FileReader italyCsv;
	private FileReader spainCsv;
	
	public MainFunction(FileReader franceCsv, FileReader italyCsv, FileReader spainCsv) {
		super();
		this.franceCsv = franceCsv;
		this.italyCsv = italyCsv;
		this.spainCsv = spainCsv;
	}

	public FileReader getFranceCsv() {
		return franceCsv;
	}

	public void setFranceCsv(FileReader franceCsv) {
		this.franceCsv = franceCsv;
	}

	public FileReader getItalyCsv() {
		return italyCsv;
	}

	public void setItalyCsv(FileReader italyCsv) {
		this.italyCsv = italyCsv;
	}

	public FileReader getSpainCsv() {
		return spainCsv;
	}

	public void setSpainCsv(FileReader spainCsv) {
		this.spainCsv = spainCsv;
	}
	
	public FileReader getContalinationChain() {
		return null;
	}
	
}
