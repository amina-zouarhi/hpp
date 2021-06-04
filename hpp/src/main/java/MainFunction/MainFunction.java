package MainFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import model.Person;
import threads.ProcessingThread;
import threads.ReadingThreadRunnable;
import threads.WritingThread;

/**
 * 
 * @author Team
 * 
 * This is our main function, It creates the threads, launches them and write result on output file
 *
 */
public class MainFunction {

	/*
	 * Input Files
	 */
	private FileReader franceCsv;
	private FileReader italyCsv;
	private FileReader spainCsv;

	/**
	 * Our three threads
	 */
	public static Thread readingThread;
	public static Thread processThread;
	public static Thread writeThread;

	/**
	 * The writer, it's used to write in output file
	 */
	static PrintWriter writer = null;

	public MainFunction(FileReader franceCsv, FileReader italyCsv, FileReader spainCsv) {
		super();
		this.franceCsv = franceCsv;
		this.italyCsv = italyCsv;
		this.spainCsv = spainCsv;
	}

	/**
	 * getters and setters
	 * 
	 */
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

	/**
	 * The main function, it does literally everything, from initializing the threads to writing using the classes that implement Runnable
	 * @param franceCsv
	 * @param italyCsv
	 * @param spainCsv
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 */
	public void getContaminationChain(FileReader franceCsv, FileReader italyCsv, FileReader spainCsv)
			throws InterruptedException, FileNotFoundException {

		try {
			writer = new PrintWriter(new File("./src/main/resources/output/output.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		BlockingQueue<Person> readingQueue = new LinkedBlockingDeque<>(1024);
		BlockingQueue<String> writingQueue = new LinkedBlockingDeque<>(1024);
		FileReader[] countryCsv = new FileReader[] { franceCsv, italyCsv, spainCsv };
		ReadingThreadRunnable readingThreadRunnable = new ReadingThreadRunnable(readingQueue, countryCsv);
		ProcessingThread processingThread = new ProcessingThread(readingThreadRunnable.getReadingQueue(), writingQueue);
		WritingThread writingThread = new WritingThread(writer, writingQueue);

		readingThread = new Thread(readingThreadRunnable);
		processThread = new Thread(processingThread);
		writeThread = new Thread(writingThread);

		// Start counting the time of execution from here
		long start = System.nanoTime();

		readingThread.start();
		processThread.start();
		writeThread.start();

		try {
			readingThread.join();
			processThread.join();
			writeThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Stop counting the time of execution
		long end = System.nanoTime();

		// Elapsed time
		long elapsedTime = end - start;
		// ELAPSED TIME
		System.out.println("Elapsed time using Thread is: " + Math.abs(elapsedTime) + "ns");
	}

	public static void main(String[] args) throws FileNotFoundException {
		FileReader franceCsv = new FileReader("./src/main/resources/input/France.csv");
		FileReader italyCsv = new FileReader("./src/main/resources/input/Italy.csv");
		FileReader spainCsv = new FileReader("./src/main/resources/input/Spain.csv");

		MainFunction covid = new MainFunction(franceCsv, italyCsv, spainCsv );
		try {
			covid.getContaminationChain(franceCsv, italyCsv, spainCsv);
		} catch (FileNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
