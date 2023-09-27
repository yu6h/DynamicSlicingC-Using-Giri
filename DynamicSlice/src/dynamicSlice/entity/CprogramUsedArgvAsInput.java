package dynamicSlice.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CprogramUsedArgvAsInput {

	private String cFileName;
	private String cFileNameWithoutExtension;
	private int quetionID;
	private String studentID;
	private String inputData;
	private String programContent;
	private List<Integer> lineNumbersOfOutputStatement;

	public CprogramUsedArgvAsInput(String cFileName, String cFileNameWithoutExtension, int quetionID, String studentID, String inputData, String programContent, List<Integer> lineNumbersOfOutputStatement) {
		this.cFileName = cFileName;
		this.cFileNameWithoutExtension = cFileNameWithoutExtension;
		this.quetionID = quetionID;
		this.studentID = studentID;
		this.inputData = inputData;
		this.programContent = programContent;
		this.lineNumbersOfOutputStatement = lineNumbersOfOutputStatement;
	}

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
	public int getQuestionID() {
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
	public List<Integer> getLineNumbersOfCoveredOutputStatement() {
		return lineNumbersOfOutputStatement;
	}
	public void setLineNumbersOfOutputStatement(List<Integer> lineNumbersOfOutputStatement) {
		this.lineNumbersOfOutputStatement = lineNumbersOfOutputStatement;
	}

}
