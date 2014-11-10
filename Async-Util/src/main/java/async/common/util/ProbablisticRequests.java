package async.common.util;

// TODO: Auto-generated Javadoc
/**
 * The Class ProbablisticRequests.
 */
public class ProbablisticRequests {
	
	/** The seed. */
	int seed;
	
	/** The client id. */
	String clientId;
	
	/** The probability. */
	float probability[] = new float[3];

	/**
	 * Instantiates a new probablistic requests.
	 */
	public ProbablisticRequests() {

	}

	/**
	 * Instantiates a new probablistic requests.
	 *
	 * @param seed the seed
	 * @param clientId the client id
	 * @param probability the probability
	 */
	public ProbablisticRequests(int seed, String clientId, float[] probability) {
		super();
		this.seed = seed;
		this.clientId = clientId;
		this.probability = probability;
	}

	/**
	 * Gets the client id.
	 *
	 * @return the client id
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Gets the probability.
	 *
	 * @return the probability
	 */
	public float[] getProbability() {
		return probability;
	}

	/**
	 * Gets the seed.
	 *
	 * @return the seed
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * Sets the client id.
	 *
	 * @param clientId the new client id
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * Sets the probability.
	 *
	 * @param probability the new probability
	 */
	public void setProbability(float[] probability) {
		this.probability = probability;
	}

	/**
	 * Sets the seed.
	 *
	 * @param seed the new seed
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}
}
