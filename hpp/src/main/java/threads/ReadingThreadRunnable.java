package threads;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import model.Person;

/**
 * 
 * This class reads the  csv files and return a blocking queue containing the line/lines read 
 *
 */
public class ReadingThreadRunnable implements Runnable {
	private BlockingQueue<Person> readingQueue;
	private FileReader[] countryCsv;
	
	public ReadingThreadRunnable(BlockingQueue<Person> readingQueue, FileReader[] countryCsv) {
		super();
		this.setReadingQueue(readingQueue);
		this.setCountryCsv(countryCsv);
	}
	
	@Override
	public void run() {
		BufferedReader[] readers = new BufferedReader[3];
		Person[] currEntries = new Person[3];
		String entry;
		try {
			// initiate currEntries with the first entry of each country
			for (int i = 0; i < 3; i++) {
				readers[i] = new BufferedReader(countryCsv[i]);
				if ((entry = readers[i].readLine()) != null)
					currEntries[i] = new Person(i,entry);
					//System.out.println(currEntries[i].toString());
			}
			Person toInsertInBQ;
			int country;
			
			while (currEntries[0] != null || currEntries[1] != null || currEntries[2] != null) { // stops when all files have been read
				
				// compare the entries in currEntries and insert the entry with
				// the smallest contamination time into the readingQueue
				toInsertInBQ = getMinEntry(currEntries);

				readingQueue.put(toInsertInBQ);
				// System.out.println(toInsertInBQ);
				
				country = toInsertInBQ.getCountry();
				
				if ((entry = readers[country].readLine()) != null)
					currEntries[country] = new Person(country, entry);
				else
					// at the end of a file, set its entry value to null
					currEntries[country] = null;
			}
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BlockingQueue<Person> getReadingQueue() {
		return readingQueue;
	}

	public void setReadingQueue(BlockingQueue<Person> readingQueue) {
		this.readingQueue = readingQueue;
	}

	public FileReader[] getCountryCsv() {
		return countryCsv;
	}

	public void setCountryCsv(FileReader[] countryCsv) {
		this.countryCsv = countryCsv;
	}

	public Person getMinEntry(Person[] currEntries) {
		// TODO Auto-generated method stub
		Person min = null;
	    for (Person entry : currEntries) {
	        if (entry != null && (min == null || entry.compareTo(min) < 0)) { // nothing to compare if entry is null
	            min = entry;
	        }
	    }
	    return min;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		FileReader franceCsv = new FileReader("./src/main/resources/input/France.csv");
		FileReader italyCsv = new FileReader("./src/main/resources/input/Italy.csv");
		FileReader spainCsv = new FileReader("./src/main/resources/input/Spain.csv");
		
		BlockingQueue<Person> readingQueue = new LinkedBlockingDeque<>(1024);
		FileReader[] countryCsv = new FileReader[] {franceCsv, italyCsv, spainCsv};
		ReadingThreadRunnable readingThreadRunnable = new ReadingThreadRunnable(readingQueue, countryCsv);
		Thread readingThread = new Thread(readingThreadRunnable);
		readingThread.start();
		while (readingThreadRunnable.getReadingQueue().isEmpty());
		System.out.println("Before: " + readingThreadRunnable.getReadingQueue().toString());
		try {
			readingThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(readingThreadRunnable.getReadingQueue().toString());
	}

}
