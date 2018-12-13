/**
 * 
 */

package analysis.jgibbslda;

import org.kohsuke.args4j.Option;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class CmdOption {
	@Option(name = "-est", usage = "Specify whether we will perform model estimation from scratch")
	protected boolean est = false;

	@Option(name = "-estc", usage = "Specify whether we will continue to perform model estimation from a previously estimated model")
	protected boolean estc = false;

	@Option(name = "-inf", usage = "Specify whether we will perform topic inference for new data")
	protected boolean inf = false;

	@Option(name = "-dir", usage = "Specify the directory where the model was saved")
	protected String modelDirectory = "./";

	@Option(name = "-dfile", usage = "Specify the data file")
	protected String dataFile = "trndocs.dat";

	@Option(name = "-model", usage = "Specify the model name")
	protected String modelName = "model-final";

	@Option(name = "-alpha", usage = "Specify alpha value")
	protected double alpha = 0.5;

	@Option(name = "-beta", usage = "Specify beta value")
	protected double beta = 0.1;

	@Option(name = "-ntopics", usage = "Specify the number of LDA topics")
	protected int K = 100;

	@Option(name = "-niters", usage = "Specify the number of Gibbs sampling iterations for model estimation or inference")
	protected int nIters = 1000;

	@Option(name = "-savestep", usage = "Specify the number of steps to save the model since the last save")
	protected int saveStep = 200;

	@Option(name = "-twords", usage = "Specify the number of most likely (top) words to be printed for each topic")
	protected int tWords = 500;

	/**
	 * Checking if the input options are valid.
	 * 
	 * @return True if all the input options are valid, false otherwise
	 */
	public boolean validOptions() {
		int count = 0;

		if (est) {
			count++;
		}
		if (estc) {
			count++;
		}
		if (inf) {
			count++;
		}

		if (count == 0) {
			System.out.println("You must specify model status!");
			return false;
		}

		if (count > 1) {
			System.out.println("You must specify only one status for the model!");
			return false;
		}

		return true;
	}
}
