
public class CprogramExpertUsedArgvAsInput {

	private String cFileName;
	private String cFileNameWithoutExtension;
	private int quetionID;
	private String studentID;
	private String inputData;
	private int[] lineNumbersOfTargetStatement;
	private String cProgramContentUsedArgvAsInput;

	public String getcFileName() {
		return cFileName;
	}

	public void setcFileName(String cFileName) {
		this.cFileName = cFileName;
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

	public String getcProgramContentUsedArgvAsInput() {
		return cProgramContentUsedArgvAsInput;
	}

	public void setcProgramContentUsedArgvAsInput(String cProgramContentUsedArgvAsInput) {
		this.cProgramContentUsedArgvAsInput = cProgramContentUsedArgvAsInput;
	}
	
	public String getcFileNameWithoutExtension() {
		if(this.cFileNameWithoutExtension == null) {
			this.cFileNameWithoutExtension = this.cFileName.substring(0, this.cFileName.indexOf("."));
		}
		return this.cFileNameWithoutExtension;
	}
	
	
	public String generateInputDataInOneLine() {
	    String str = this.inputData.replace("\r\n", "\n"); // convert windows line endings to linux format 
	    str = str.replace("\r", "\n"); // convert (remaining) mac line endings to linux format
	    return str.replace("\n", " "); // count total line endings
	}
	
	public int[] generateLineNumbersOfTargetStatement() {
		this.lineNumbersOfTargetStatement = new int[]{19, 20};
		return this.lineNumbersOfTargetStatement;
	}

}
