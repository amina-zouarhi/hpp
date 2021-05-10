package MainFunction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import model.Person;
import threads.ReadingThreadRunnable;

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
	
	public FileReader getContaminationChain() {
		BlockingQueue<Person> readingQueue = new LinkedBlockingDeque<>(1024);
		FileReader[] countryCsv = new FileReader[] {franceCsv, italyCsv, spainCsv};
		ReadingThreadRunnable readingThreadRunnable = new ReadingThreadRunnable(readingQueue, countryCsv);
		Thread readingThread = new Thread(readingThreadRunnable);
		readingThread.start();
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
        FileReader franceCsv = new FileReader("./src/main/resources/input/France.csv");
		FileReader italyCsv = new FileReader("./src/main/resources/input/Italy.csv");
		FileReader spainCsv = new FileReader("./src/main/resources/input/Spain.csv");
		MainFunction mainFunction = new MainFunction(franceCsv, italyCsv, spainCsv);
		mainFunction.getContaminationChain();
		
    }
	
}
