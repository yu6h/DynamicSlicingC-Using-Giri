package dynamicSlice.usecase.in;

public interface Giri {
	
	public void setContainerName(String containerName);
	
	public void createWorkDirectoryInContainer(String workDirectoryInContainer);
	
	public void deleteWorkDirectoryInContainer(String workDirectoryInContainer);

	public void copyMakeFileIntoContainer(String workDirectory, String workDirectoryInContainer); 
	
	public void copyLotTxtFileIntoContainer(String workDirectory, String workDirectoryInContainer);
	
	public void copyCProgramFileIntoContainer(String workDirectory, String workDirectoryInContainer, String cFileName);

	public void make(String workDirectoryInContainer);

	public void downloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory,String cFileNameWithoutExtension);
	
}
