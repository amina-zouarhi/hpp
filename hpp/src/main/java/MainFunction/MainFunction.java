package MainFunction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import model.Person;
import threads.ProcessingThread;
import threads.ReadingThreadRunnable;

public class MainFunction {

	private FileReader franceCsv;
	private FileReader italyCsv;
	private FileReader spainCsv;

	static boolean readThreadAlive = true;
	static boolean addThreadAlive = true;

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

	public FileReader getContaminationChain() throws InterruptedException {
		BlockingQueue<Person> readingQueue = new LinkedBlockingDeque<>(1024);
		FileReader[] countryCsv = new FileReader[] { franceCsv, italyCsv, spainCsv };
		ReadingThreadRunnable readingThreadRunnable = new ReadingThreadRunnable(readingQueue, countryCsv);
		Thread readingThread = new Thread(readingThreadRunnable);
		readingThread.start();
		readingThread.join();
		// to print the output
		System.out.println(readingThreadRunnable.getReadingQueue().toString()); // put inside comment later !!!
		return null;
	}

	public static void main(String[] args) throws FileNotFoundException {
		FileReader franceCsv = new FileReader("./src/main/resources/input/France.csv");
		FileReader italyCsv = new FileReader("./src/main/resources/input/Italy.csv");
		FileReader spainCsv = new FileReader("./src/main/resources/input/Spain.csv");

		BlockingQueue<Person> readingQueue = new LinkedBlockingDeque<>(1024);
		BlockingQueue<String> writingQueue = new LinkedBlockingDeque<>(1024);
		FileReader[] countryCsv = new FileReader[] { franceCsv, italyCsv, spainCsv };
		ReadingThreadRunnable readingThreadRunnable = new ReadingThreadRunnable(readingQueue, countryCsv);
		ProcessingThread processingThread = new ProcessingThread(readingThreadRunnable.getReadingQueue(), writingQueue,
				readThreadAlive);
		
		Thread readingThread = new Thread(readingThreadRunnable);
		Thread processThread = new Thread(processingThread);
		readingThread.start();
		processThread.start();
		try {
			readingThread.join();
			processThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(processingThread.getBlockingQueueWrite());
	}

}
