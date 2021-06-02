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
			FileReader franceCsv = new FileReader("./hpp/src/main/resources/input/France.csv");
			FileReader italyCsv = new FileReader("./hpp/src/main/resources/input/Italy.csv");
			FileReader spainCsv = new FileReader("./hpp/src/main/resources/input/Spain.csv");
			MainFunction mainFunction = new MainFunction(franceCsv, italyCsv, spainCsv);
			FileReader actual = mainFunction.getContaminationChain();
			FileReader expected = new FileReader("./hpp/src/main/resources/output/output.csv");
			assertEquals(expected, actual);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
