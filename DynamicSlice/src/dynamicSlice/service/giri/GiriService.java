package dynamicSlice.service.giri;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import dynamicSlice.entity.CprogramExpertUsedArgvAsInput;
import dynamicSlice.entity.DynamicSlicing;
import dynamicSlice.service.FileHandler;
import dynamicSlice.service.FileHandlerImpl;

public class GiriService implements DynamicSlicing{
	
	private String workDirectory;
	
	private String studentID;
	
	private String questiotnID;
	
	private String cFileName;
	
	private String containerName;
	
	private String inputData;
	
	private String workDirectoryInContainer;

	private CprogramExpertUsedArgvAsInput cPrgramDTO;

	private List<Integer> linNumbersOfSliceResults;
	
	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}



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
	
	public void execute(){
		TreeSet<Integer> lineNumbersSet = new TreeSet<Integer>();
		
		FileHandler fileHandler = new FileHandlerImpl();

		this.initializeContainerName();
		this.initializeWorkDirectory();
		
		fileHandler.deleteWorkDirectory(new File(this.workDirectory));
		fileHandler.createWorkDirectory(new File(this.workDirectory));
		
		fileHandler.writePreprocessedCprogramFile(this.workDirectory, this.cPrgramDTO.getcFileName(), this.cPrgramDTO.getcProgramContentUsedArgvAsInput());
		fileHandler.writeMakeFile(this.workDirectory,this.cPrgramDTO.getcFileNameWithoutExtension(), this.cPrgramDTO.generateInputDataInOneLine());
		List<Integer> lineNumberOfTargetStatement = this.cPrgramDTO.getLineNumbersOfArgumentInOutputStatement();
		Giri giri = new GiriImpl();
		giri.createContainer(containerName);
		for(Integer lineNumberOfTarget:lineNumberOfTargetStatement) {
			fileHandler.writeLocTxtFile(this.workDirectory,this.cPrgramDTO.getcFileName(),lineNumberOfTarget);
			this.initializeWorkDirectoryInContainerName(lineNumberOfTarget);
			giri.createWorkDirectoryInContainer(this.workDirectoryInContainer);
			giri.copyMakeFileIntoContainer(this.workDirectory, this.workDirectoryInContainer);
			giri.copyLotTxtFileIntoContainer( this.workDirectory, this.workDirectoryInContainer);
			giri.copyCProgramFileIntoContainer(this.workDirectory, this.workDirectoryInContainer, this.cPrgramDTO.getcFileName());
			giri.make(this.workDirectoryInContainer);
			giri.downloadSlicLocFileIntoWorkDirectory(this.workDirectoryInContainer, this.workDirectory, this.cPrgramDTO.getcFileNameWithoutExtension());
			List<Integer> lineNumbersOfDynamicSlice = fileHandler.readSliceLocFile(this.workDirectory, this.cPrgramDTO.getcFileNameWithoutExtension());
			lineNumbersSet.addAll(lineNumbersOfDynamicSlice);
			
		}
		giri.stopContainer(containerName);
		giri.removeContainer(containerName);
		this.linNumbersOfSliceResults = new ArrayList<Integer>(lineNumbersSet);
	}
	
	

	private void initializeContainerName() {
		this.containerName = this.cPrgramDTO.getStudentID() + "_" + this.cPrgramDTO.getQuetionID();
	}

	private void initializeWorkDirectoryInContainerName(int lineNumberOfTarget) {
		this.workDirectoryInContainer = "/giri/test/UnitTests/HWbyTarget" +lineNumberOfTarget+"/";		
	}

	private void initializeWorkDirectory() {
		this.workDirectory = "/home/aaron/Desktop/GiriWorkDirectory/"+this.cPrgramDTO.getStudentID()+"/"
				+ this.cPrgramDTO.getQuetionID() + "/";
	}

	public void setCProgramInfoUsedArgvAsInput(CprogramExpertUsedArgvAsInput cPrgramDTO) {
		this.cPrgramDTO = cPrgramDTO;
	}

	public List<Integer> getDynamicResult() {
		return this.linNumbersOfSliceResults;
	}

}
