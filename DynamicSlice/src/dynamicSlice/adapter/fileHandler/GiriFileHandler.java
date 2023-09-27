package dynamicSlice.adapter.fileHandler;

import java.io.File;
import java.util.List;

public interface GiriFileHandler {
	public boolean deleteWorkDirectory(File directoryToBeDeleted);
	public void createWorkDirectory(File workDirectory);
	public void writePreprocessedCprogramFile(String workDirectory, String cFileName, String preprocessedprogramContent);
	public void writeMakeFile(String workDirectory, String cFileNameWithoutExtension, String InputDataInOneLine);
	public void writeLocTxtFile(String workDirectory, String cFileName, int lineNumberOfTargetStatement );
	public List<Integer> readSliceLocFile(String workDirectory, String cFileNameWithoutExtension);
	public boolean checkIfSliceLocFileExist(String workDirectory, String cFileNameWithoutExtension);
	public void deleteSliceLocFile(String workDirectory, String getcFileNameWithoutExtension);
}
