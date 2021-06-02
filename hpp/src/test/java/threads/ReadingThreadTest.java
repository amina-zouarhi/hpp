package threads;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.jupiter.api.Test;

import model.Person;

class ReadingThreadTest {

	@Test
	void test() throws FileNotFoundException, InterruptedException {
		FileReader franceCsv = new FileReader("./src/main/resources/input/France.csv");
		FileReader italyCsv = new FileReader("./src/main/resources/input/Italy.csv");
		FileReader spainCsv = new FileReader("./src/main/resources/input/Spain.csv");
		
		BlockingQueue<Person> readingQueue = new LinkedBlockingDeque<>(1024);
		FileReader[] countryCsv = new FileReader[] {franceCsv, italyCsv, spainCsv};
		ReadingThreadRunnable readingThreadRunnable = new ReadingThreadRunnable(readingQueue, countryCsv);
		Thread readingThread = new Thread(readingThreadRunnable);
		readingThread.start();
		readingThread.join(); // waiting for thread to finish completely to verify the final readingQueue
		
		String expected = "[Person [country=SPAIN, id=0, contamination_time=1.5739452E9, contaminated_by=-1], "
				+ "Person [country=ITALY, id=1, contamination_time=1.5774836207382095E9, contaminated_by=-1], "
				+ "Person [country=SPAIN, id=2, contamination_time=1.5795534641818361E9, contaminated_by=0], "
				+ "Person [country=SPAIN, id=3, contamination_time=1.5810220414764192E9, contaminated_by=0], "
				+ "Person [country=FRANCE, id=4, contamination_time=1.5821611585235808E9, contaminated_by=-1], "
				+ "Person [country=FRANCE, id=5, contamination_time=1.5830918849200459E9, contaminated_by=-1], "
				+ "Person [country=ITALY, id=6, contamination_time=1.5838788028757234E9, contaminated_by=1], "
				+ "Person [country=SPAIN, id=7, contamination_time=1.5845604622146287E9, contaminated_by=3], "
				+ "Person [country=SPAIN, id=8, contamination_time=1.5851617283636725E9, contaminated_by=3], "
				+ "Person [country=FRANCE, id=9, contamination_time=1.5856995792617905E9, contaminated_by=4], "
				+ "Person [country=ITALY, id=10, contamination_time=1.586186124581804E9, contaminated_by=6], "
				+ "Person [country=SPAIN, id=11, contamination_time=1.5866303056582553E9, contaminated_by=3], "
				+ "Person [country=ITALY, id=12, contamination_time=1.5870389126391647E9, contaminated_by=1], "
				+ "Person [country=FRANCE, id=13, contamination_time=1.5874172236139328E9, contaminated_by=4], "
				+ "Person [country=FRANCE, id=14, contamination_time=1.5877694227054172E9, contaminated_by=5], "
				+ "Person [country=ITALY, id=15, contamination_time=1.5880988829528382E9, contaminated_by=-1], "
				+ "Person [country=SPAIN, id=16, contamination_time=1.5884083632841413E9, contaminated_by=11], "
				+ "Person [country=ITALY, id=17, contamination_time=1.588700149101882E9, contaminated_by=-1], "
				+ "Person [country=ITALY, id=18, contamination_time=1.5889761548079798E9, contaminated_by=-1], "
				+ "Person [country=FRANCE, id=19, contamination_time=1.589238E9, contaminated_by=13]]";
		
		assertEquals(expected, readingThreadRunnable.getReadingQueue().toString());
	}

}
