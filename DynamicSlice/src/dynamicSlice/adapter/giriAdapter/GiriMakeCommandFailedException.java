package dynamicSlice.adapter.giriAdapter;

public class GiriMakeCommandFailedException extends RuntimeException {
	
	public GiriMakeCommandFailedException(String containerName) {
		super("Container Name is "+ containerName);
	}

}
