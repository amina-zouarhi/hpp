package globalTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;

import org.junit.Before;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import MainFunction.MainFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IntegrationTests {

	@Test
	public void mainTest() throws InterruptedException {
		try {
			System.out.println("Working Directory = " + System.getProperty("user.dir"));
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
}
