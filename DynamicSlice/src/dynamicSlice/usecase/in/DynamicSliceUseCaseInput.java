package dynamicSlice.usecase.in;

import dynamicSlice.entity.CprogramUsedArgvAsInput;

import java.util.List;

public class DynamicSliceUseCaseInput {

	private String cFileName;
	private String cFileNameWithoutExtension;
	private int quetionID;
	private String studentID;
	private String inputData;
	private String programContent;
	private List<Integer> lineNumbersOfOutputStatement;

	public String getcFileName() {
		return cFileName;
	}

	public void setcFileName(String cFileName) {
		this.cFileName = cFileName;
	}

	public String getcFileNameWithoutExtension() {
		return cFileNameWithoutExtension;
	}

	public void setcFileNameWithoutExtension(String cFileNameWithoutExtension) {
		this.cFileNameWithoutExtension = cFileNameWithoutExtension;
	}

	public int getQuetionID() {
		return quetionID;
	}

	public void setQuetionID(int quetionID) {
		this.quetionID = quetionID;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public String getProgramContent() {
		return programContent;
	}

	public void setProgramContent(String programContent) {
		this.programContent = programContent;
	}

	public List<Integer> getLineNumbersOfOutputStatement() {
		return lineNumbersOfOutputStatement;
	}

	public void setLineNumbersOfOutputStatement(List<Integer> lineNumbersOfOutputStatement) {
		this.lineNumbersOfOutputStatement = lineNumbersOfOutputStatement;
	}

}
