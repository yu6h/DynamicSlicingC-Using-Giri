package dynamicSlice.usecase.service;

import java.util.List;

public interface DynamicSliceToolAdapter {

	void iniitilizeDirectory(String studentID,int questionID,String cProgramFileName);
	void setInputData(String inputData);
	void setPreprocessedCprogramContent(String preprocessedprogramContent);
	void setTargetLineNumber(int lineNumber);
	void execute();
	void deleteDirectory();
	List<Integer> getLineNumbersOfDynamicSlice();
}
