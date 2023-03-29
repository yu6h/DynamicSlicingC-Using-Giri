package dynamicSlice;

public interface Giri {
	
	public void createContainer(String containerName);
	
	public void createWorkDirectoryInContainer(String workDirectoryInContainer); 

	public void copyMakeFileIntoContainer(String workDirectory, String workDirectoryInContainer); 
	
	public void copyLotTxtFileIntoContainer(String workDirectory, String workDirectoryInContainer);
	
	public void copyCProgramFileIntoContainer(String workDirectory, String workDirectoryInContainer, String cFileName);

	public void make(String workDirectoryInContainer);

	public void downloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory,String cFileNameWithoutExtension);
	
	public void stopContainer(String containerName);
	
	public void removeContainer(String containerName);
}
