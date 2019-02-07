package resampledMean;
/*
 * Project goal is to read a file with numbers and, select 100 numbers at random.
 * These 100 random numbers will then be displayed, along with an average for those 100 numbers
 */

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

public class ResampledMean {

	public static void main(String[] args) {
		/*
		 * 1234 is used to give a predetermined seed, can be removed to allow truly
		 * random results
		 */
		ResampledMean mean = new ResampledMean(1234);

		if (mean.readOriginal("resampledMean/ages.txt")) {
			System.out.println("File loaded successfully");
		} else {
			System.out.println("File not found, creating random array of ages");
			mean.createRandom(100, 40, 90);
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

	// seed used for creating randomness
	private Random rand;
	// holders for original list and lists that will be created
	private List<Double> origAges = new ArrayList<Double>();
	private List<Double> newAges = new ArrayList<Double>();

	public ResampledMean() {
		// this will generate results using a random seed
		rand = new Random();
	}

	public ResampledMean(int seed) {
		/*
		 * can set random to a predetermined seed, easier to verify results due to
		 * consistency
		 */
		rand = new Random(seed);
	}

	public double calculateMean() {
		// calculates the mean of the generated list
		double total = 0;
		for (Double age : newAges) {
			total += age;
		}
		return total / newAges.size();
	}

	public boolean createFile(int fileNumber) {
		/*
		 * creates a file using newly generated list fileNumber is what the file will be
		 * named as to differentiate each result
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

	public boolean createRandom(int amt, int min, int max) {
		/*
		 * will create a random list of amt size from min to max. Used if original list
		 * is unavailable
		 */
		if (min > max) {
			// checks to make sure range is correct
			return false;
		}

		origAges.clear();
		int range = max - min + 1;
		for (int i = 0; i < amt; i++) {
			origAges.add(rand.nextDouble() * range + min);
		}
		return true;
	}

	public String displayAges() {
		// displays age using toString method
		return newAges.toString();
	}

	public void randomAges(int amt) {
		// creates list of ages from original list using amt amount of results
		newAges.clear();
		for (int i = 0; i < amt; i++) {
			newAges.add(origAges.get(rand.nextInt(origAges.size())));
		}

	}

	public boolean readOriginal(String fileName) {
		// reads the original file. weeds out any non entries such as words/letters
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
