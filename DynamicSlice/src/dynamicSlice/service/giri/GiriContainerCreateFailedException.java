package dynamicSlice.service.giri;

public class GiriContainerCreateFailedException extends RuntimeException {
	
	public GiriContainerCreateFailedException(String containerName) {
		super("Container Name is "+ containerName);
	}

}
