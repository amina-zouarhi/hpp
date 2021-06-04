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

public class MainFunction {

	private FileReader franceCsv;
	private FileReader italyCsv;
	private FileReader spainCsv;

	public static Thread readingThread;
	public static Thread processThread;
	public static Thread writeThread;

	static boolean readingThreadIsAlive = true;
	static boolean processThreadIsAlive = true;

	static PrintWriter writer = null;

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
		if (!readingThreadRunnable.getReadingQueue().isEmpty()) {
			System.out.println(readingThreadRunnable.getReadingQueue().toString()); // put inside comment later !!!
		}
		return null;
	}

	public static void main(String[] args) throws FileNotFoundException {
		FileReader franceCsv = new FileReader("./src/main/resources/input/France.csv");
		FileReader italyCsv = new FileReader("./src/main/resources/input/Italy.csv");
		FileReader spainCsv = new FileReader("./src/main/resources/input/Spain.csv");

		try {
			writer = new PrintWriter(new File("./src/main/resources/output/output.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		BlockingQueue<Person> readingQueue = new LinkedBlockingDeque<>(1024);
		BlockingQueue<String> writingQueue = new LinkedBlockingDeque<>(1024);
		FileReader[] countryCsv = new FileReader[] { franceCsv, italyCsv, spainCsv };
		ReadingThreadRunnable readingThreadRunnable = new ReadingThreadRunnable(readingQueue, countryCsv);
		ProcessingThread processingThread = new ProcessingThread(readingThreadRunnable.getReadingQueue(), writingQueue,
				readingThreadIsAlive);
		WritingThread writingThread = new WritingThread(writer, writingQueue, processThreadIsAlive);

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
			long elapsedTime= end-start; 
		//ELAPSED TIME
			System.out.println("Elapsed time using Thread is: "+Math.abs(elapsedTime));
		System.out.println(processingThread.getBlockingQueueWrite().toString());


	}

}
