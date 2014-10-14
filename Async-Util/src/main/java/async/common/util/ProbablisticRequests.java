package async.common.util;

public class ProbablisticRequests {
	int seed;
	String clientId;
	float probability[] = new float[3];
	
	public ProbablisticRequests() {
	 	
	}
	
	public ProbablisticRequests(int seed, String clientId, float[] probability) {
		super();
		this.seed = seed;
		this.clientId = clientId;
		this.probability = probability;
	}
	
	public int getSeed() {
		return seed;
	}
	public void setSeed(int seed) {
		this.seed = seed;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public float[] getProbability() {
		return probability;
	}
	public void setProbability(float[] probability) {
		this.probability = probability;
	}	
}
