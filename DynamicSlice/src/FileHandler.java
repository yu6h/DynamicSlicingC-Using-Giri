import java.io.File;

public interface FileHandler {
	public boolean deleteWorkDirectory(File directoryToBeDeleted);
	public void createWorkDirectory(File file);
	public void writePreprocessedCprogramFile(String workDirectory, String cFileName, String preprocessedprogramContent);
	public void writeMakeFile(String workDirectory, String cFileNameWithoutExtension, String InputDataInOneLine);
	public void writeLocTxtFile(String workDirectory, String cFileName, int lineNumberOfTargetStatement );
	public void createFile(String workDirectory, String string);
}
