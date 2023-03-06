
public interface Giri {
	
	public void createContainer(String containerName);

	public void copyMakeFileIntoContainer(String workDirectory, String workDirectoryInContainer); 
	
	public void copyLotTxtFileIntoContainer(String workDirectory, String workDirectoryInContainer);

	public void make(String workDirectoryInContainer);

	public void downloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory,String cFileNameWithoutExtension);
}
