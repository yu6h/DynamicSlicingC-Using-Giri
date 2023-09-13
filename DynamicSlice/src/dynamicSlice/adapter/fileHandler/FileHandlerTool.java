package dynamicSlice.adapter.fileHandler;

import java.io.File;

public interface FileHandlerTool {
	boolean deleteWorkDirectory(File directoryToBeDeleted);
	void createWorkDirectory(File workDirectory);
	String readFile(String workDirectory,String fileName);
	void writeFile(String workDirectory,String fileName,String content);
	void renameFile(String workDirectory,String originalFileName, String newFileName);
}
