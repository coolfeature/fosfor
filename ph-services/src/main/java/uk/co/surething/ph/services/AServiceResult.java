package uk.co.surething.ph.services;

public abstract class AServiceResult {

	private final Long instantiationTime;
	
	{
		this.instantiationTime = System.currentTimeMillis();
	}

	/**
	 * 
	 * @return
	 */
	public Long getInstantiationTime() {
		return instantiationTime;
	}

	/**
	 * 
	 * @return
	 */
	public Long elapsedSinceInstantiation() {
		return System.currentTimeMillis() - this.instantiationTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public String displayElapsedSinceInstantiation() {
		Long millis = elapsedSinceInstantiation();
		double seconds = millis / 1000.0;
		return String.format("%.3f sec", seconds);
	}
	
}
