/**
 * 
 */

package analysis.jgibbslda;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class Constants {
	/* unknown status */
	protected static final int MODEL_STATUS_UNKNOWN = 0;
	/* estimate from scratch */
	protected static final int MODEL_STATUS_EST = 1;
	/* continue to estimate from a previously estimated model */
	protected static final int MODEL_STATUS_ESTC = 2;
	/* inference for new data */
	protected static final int MODEL_STATUS_INF = 3;

	/* the default number of Gibbs sampling iterations for inference */
	protected static final int nInfIters = 200;
}
