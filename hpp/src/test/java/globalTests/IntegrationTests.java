package globalTests;

import org.junit.Test;

import MainFunction.MainFunction;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class IntegrationTests {

	@Test
	public void mainTest() throws InterruptedException {
		try {
			FileReader franceCsv = new FileReader("./src/main/resources/input/France.csv");
			FileReader italyCsv = new FileReader("./src/main/resources/input/Italy.csv");
			FileReader spainCsv = new FileReader("./src/main/resources/input/Spain.csv");
			MainFunction mainFunction = new MainFunction(franceCsv, italyCsv, spainCsv);
			mainFunction.getContaminationChain(franceCsv, italyCsv, spainCsv);
			FileReader expected = new FileReader("./src/main/resources/output/output.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testCase1() throws InterruptedException {
		try {
			FileReader franceCsv = new FileReader("./src/test/resources/input_tests/test1/France.csv");
			FileReader italyCsv = new FileReader("./src/test/resources/input_tests/test1/Italy.csv");
			FileReader spainCsv = new FileReader("./src/test/resources/input_tests/test1/Spain.csv");
			MainFunction mainFunction = new MainFunction(franceCsv, italyCsv, spainCsv);
			mainFunction.getContaminationChain(franceCsv, italyCsv, spainCsv);
			FileReader expected = new FileReader("./src/test/resources/output_tests/test1.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testCase2() throws InterruptedException {
		try {
			FileReader franceCsv = new FileReader("./src/test/resources/input_tests/test2/France.csv");
			FileReader italyCsv = new FileReader("./src/test/resources/input_tests/test2/Italy.csv");
			FileReader spainCsv = new FileReader("./src/test/resources/input_tests/test2/Spain.csv");
			MainFunction mainFunction = new MainFunction(franceCsv, italyCsv, spainCsv);
			mainFunction.getContaminationChain(franceCsv, italyCsv, spainCsv);
			FileReader expected = new FileReader("./src/test/resources/output_tests/test2.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testCase3() throws InterruptedException {
		try {
			FileReader franceCsv = new FileReader("./src/test/resources/input_tests/test3/France.csv");
			FileReader italyCsv = new FileReader("./src/test/resources/input_tests/test3/Italy.csv");
			FileReader spainCsv = new FileReader("./src/test/resources/input_tests/test3/Spain.csv");
			MainFunction mainFunction = new MainFunction(franceCsv, italyCsv, spainCsv);
			mainFunction.getContaminationChain(franceCsv, italyCsv, spainCsv);
			FileReader expected = new FileReader("./src/test/resources/output_tests/test3.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
