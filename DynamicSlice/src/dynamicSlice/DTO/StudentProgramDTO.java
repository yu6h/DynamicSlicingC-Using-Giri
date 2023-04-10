package dynamicSlice.DTO;

public class StudentProgramDTO {
	
	private int quetionID;
	
	private String studentID;
	
	private String cFileName;
	
	private String inputData;
	
	private String cProgramContent;

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

	public String getcFileName() {
		if(this.cFileName == null) {
			this.cFileName = this.studentID + "_" + this.quetionID + ".c";
		}
		return cFileName;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public String getcProgramContent() {
		return cProgramContent;
	}

	public void setcProgramContent(String cProgramContent) {
		this.cProgramContent = cProgramContent;
	}
	
	

}
