package resampledMean;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project goal is to read a file with numbers and, select 100 numbers at random.
 * These 100 random numbers will then be displayed, along with an average for those 100 numbers
 * @author qstr2
 *
 */
public class ResampledMean {

	public static void main(String[] args) {

		// 1234 is used to give a predetermined seed, can be removed to allow truly
		// random results

		ResampledMean mean = new ResampledMean(1234);

		if (mean.readOriginal("resampledMean/ages.txt")) {
			System.out.println("File loaded successfully");
		} else {
			System.out.println("File not found");
		}

		// will create NumFiles files with different values

		int numFiles = 5;
		for (int i = 1; i <= numFiles; i++) {
			mean.randomAges(100);
			System.out.println(mean.calculateMean());
			System.out.println(mean.displayAges());
			mean.createFile(i);
		}
	}

	/**
	 * seed used for creating randomness
	 */
	private Random rand;
	/**
	 * holds original list
	 */
	private List<Double> origAges = new ArrayList<Double>();
	/**
	 * holds lists that will be created
	 */
	private List<Double> newAges = new ArrayList<Double>();

	/**
	 * generates results using a random seed
	 */
	public ResampledMean() {
		// this will generate results using a random seed
		rand = new Random();
	}

	/**
	 * generates results using a predetermined seed
	 * 
	 * @param seed
	 */
	public ResampledMean(int seed) {

		rand = new Random(seed);
	}

	/**
	 * calculates mean of generated list
	 * 
	 * @return mean of the newAges list
	 */
	public double calculateMean() {
		// calculates the mean of the generated list
		double total = 0;
		for (Double age : newAges) {
			total += age;
		}
		return total / newAges.size();
	}

	/**
	 * creates a file using the newly generated list along with the mean
	 * 
	 * @param fileNumber used to give each file being generated a different name
	 * @return returns true if file is successfully created
	 */
	public boolean createFile(int fileNumber) {
		/*
		 * 
		 */
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")
						+ System.getProperty("file.separator") + "newAges " + fileNumber + ".txt"), "utf-8"))) {
			// not using toString because of different way text is displayed
			// this method allows a random list to be created from newly generated list
			for (int i = 0; i < newAges.size(); i++) {

				writer.write(newAges.get(i) + "\n");
			}

			writer.write("Average\t" + Double.toString(calculateMean()));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * returns list of ages using toString method
	 * 
	 * @return
	 */
	public String displayAges() {
		return newAges.toString();
	}

	/**
	 * creates list of ages from original list using amt amount of results
	 * 
	 * @param amt
	 */
	public void randomAges(int amt) {
		newAges.clear();
		for (int i = 0; i < amt; i++) {
			newAges.add(origAges.get(rand.nextInt(origAges.size())));
		}

	}

	/**
	 * reads the original file. weeds out any non entries such as words/letters
	 * 
	 * @param fileName name of file with extension
	 * @return returns true if file read successfully, else false
	 */
	public boolean readOriginal(String fileName) {
		//
		newAges.clear();
		BufferedReader fileReader;
		try {

			fileReader = new BufferedReader(
					new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator") + fileName));
			String line;
			while ((line = fileReader.readLine()) != null) {
				if (line.matches("-?\\d+(\\.\\d+)?")) {
					// makes sure value is a number/decimal and does not contain letters
					origAges.add(Double.valueOf(line));
				} else {
					System.out.println(line + " Is not a number");
				}

			}
			fileReader.close();

		} catch (FileNotFoundException e) {
			// file wasn't found
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// something happened while reading file
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
