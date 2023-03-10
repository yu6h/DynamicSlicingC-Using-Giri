import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public class DynamicSlicing {
	
	private String workDirectory;
	
	private String studentID;
	
	private String questiotnID;
	
	private String cFileName;
	
	private int lineNumberOfTargetStatement;
	
	private String containerName;
	
	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	private String inputData;
	
	private String workDirectoryInContainer;

	public String getWorkDirectory() {
		return workDirectory;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getQuestiotnID() {
		return questiotnID;
	}

	public void setQuestiotnID(String questiotnID) {
		this.questiotnID = questiotnID;
	}

	public String getcFileName() {
		return cFileName;
	}

	public void setcFileName(String cFileName) {
		this.cFileName = cFileName;
	}

	public String getWorkDirectoryInContainer() {
		return workDirectoryInContainer;
	}
	
	public void setLineNumberOfTargetStatement(int lineNumber) {
		this.lineNumberOfTargetStatement = lineNumber;
	}

	private String getInputDataInOneLine() {
	    String str = this.inputData.replace("\r\n", "\n"); // convert windows line endings to linux format 
	    str = str.replace("\r", "\n"); // convert (remaining) mac line endings to linux format
	    return str.replace("\n", " "); // count total line endings
	}
	
	private String getcFileNameWithoutExtension() {
		String cFileNameWithoutExtension;
		if(cFileName.contains(".")) {
			cFileNameWithoutExtension =  cFileName.substring(0, cFileName.indexOf("."));
		}else {
			cFileNameWithoutExtension = cFileName;
		}
		return cFileNameWithoutExtension;
	}
	
	public void execute(){
		
		FileHandler fileHandler = new FileHandlerImpl();

		this.initializeContainerName();
		this.initializeWorkDirectory();
		this.initializeWorkDirectoryInContainerName();
		fileHandler.deleteWorkDirectory(new File(this.workDirectory));
		fileHandler.createWorkDirectory(new File(this.workDirectory));
		CprogramFilePreprocessor preprocessor  = new CprogramFilePreprocessorStub();
		fileHandler.writePreprocessedCprogramFile(this.workDirectory, this.cFileName, preprocessor.getPreprocessProgramContent());
		fileHandler.writeMakeFile(this.workDirectory,this.getcFileNameWithoutExtension(),this.getInputDataInOneLine());
		fileHandler.writeLocTxtFile(this.workDirectory,this.cFileName,this.lineNumberOfTargetStatement);
		Giri giri = new GiriImpl();
		giri.createContainer(containerName);
		giri.createWorkDirectoryInContainer(this.workDirectoryInContainer);
		giri.copyMakeFileIntoContainer(this.workDirectory, this.workDirectoryInContainer);
		giri.copyLotTxtFileIntoContainer( this.workDirectory, this.workDirectoryInContainer);
		giri.copyCProgramFileIntoContainer(this.workDirectory, this.workDirectoryInContainer, this.cFileName);
		giri.make(this.workDirectoryInContainer);
		giri.downloadSlicLocFileIntoWorkDirectory(this.workDirectoryInContainer, this.workDirectory, this.getcFileNameWithoutExtension());
		giri.stopContainer(containerName);
		giri.removeContainer(containerName);

	}

	private void initializeContainerName() {
		this.containerName = this.studentID+"_"+this.questiotnID;
	}

	private void initializeWorkDirectoryInContainerName() {
		this.workDirectoryInContainer = "/giri/test/UnitTests/HW/";		
	}

	private void initializeWorkDirectory() {
		this.workDirectory = "/home/aaron/Desktop/B/";
	}


}
