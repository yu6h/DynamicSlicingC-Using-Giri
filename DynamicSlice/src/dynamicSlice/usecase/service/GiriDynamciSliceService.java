package dynamicSlice.usecase.service;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;



import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.usecase.in.DynamicSliceUseCaseInput;
import dynamicSlice.usecase.in.DynamicSliceUseCaseOutput;
import dynamicSlice.usecase.in.DynamicSliceUseCase;
import dynamicSlice.usecase.in.Giri;
import dynamicSlice.usecase.out.FileRepository;

public class GiriDynamciSliceService implements DynamicSliceUseCase{
	
	private String workDirectory;
	
	private String containerName;
	
	private String workDirectoryInContainer;

	private CprogramUsedArgvAsInput cPrgramDTO;

	private List<Integer> linNumbersOfSliceResults;

	private FileRepository fileHandler;

	private Giri giri;
	
	public GiriDynamciSliceService(FileRepository fileHandler,Giri giri) {
		this.fileHandler = fileHandler;
		this.giri = giri;
		this.setContainerName("giriContainer");
		giri.setContainerName(this.containerName);
	}

	public String getWorkDirectory() {
		return workDirectory;
	}

	public String getWorkDirectoryInContainer() {
		return workDirectoryInContainer;
	}
	
	public DynamicSliceUseCaseOutput execute(DynamicSliceUseCaseInput input){
		this.cPrgramDTO = input.getcProgram();
		TreeSet<Integer> lineNumbersSet = new TreeSet<Integer>();

		
		this.initializeWorkDirectory();
		this.initializeWorkDirectoryInContainerName();
		fileHandler.deleteWorkDirectory(new File(this.workDirectory));
		fileHandler.createWorkDirectory(new File(this.workDirectory));
		
		fileHandler.writePreprocessedCprogramFile(this.workDirectory, this.cPrgramDTO.getcFileName(), this.cPrgramDTO.getProgramContent());
		fileHandler.writeMakeFile(this.workDirectory,this.cPrgramDTO.getcFileNameWithoutExtension(), this.cPrgramDTO.getInputData());
		List<Integer> lineNumberOfTargetStatement = this.cPrgramDTO.getLineNumbersOfOutputStatement();

		for(Integer lineNumberOfTarget:lineNumberOfTargetStatement) {
			fileHandler.writeLocTxtFile(this.workDirectory,this.cPrgramDTO.getcFileName(),lineNumberOfTarget);
			giri.deleteWorkDirectoryInContainer(this.workDirectoryInContainer);
			giri.createWorkDirectoryInContainer(this.workDirectoryInContainer);
			giri.copyMakeFileIntoContainer(this.workDirectory, this.workDirectoryInContainer);
			giri.copyLotTxtFileIntoContainer( this.workDirectory, this.workDirectoryInContainer);
			giri.copyCProgramFileIntoContainer(this.workDirectory, this.workDirectoryInContainer, this.cPrgramDTO.getcFileName());
			fileHandler.deleteSliceLocFile(this.workDirectory, this.cPrgramDTO.getcFileNameWithoutExtension());
			giri.makeAndDownloadSlicLocFileIntoWorkDirectory(this.workDirectoryInContainer, this.workDirectory, this.cPrgramDTO.getcFileNameWithoutExtension(),
					fileHandler);
			List<Integer> lineNumbersOfDynamicSlice = fileHandler.readSliceLocFile(this.workDirectory, this.cPrgramDTO.getcFileNameWithoutExtension());
			lineNumbersSet.addAll(lineNumbersOfDynamicSlice);
		}

		fileHandler.deleteWorkDirectory(new File(this.workDirectory));
		this.linNumbersOfSliceResults = new ArrayList<Integer>(lineNumbersSet);
		DynamicSliceUseCaseOutput output = new DynamicSliceUseCaseOutput();
		output.setLineNumbersOfDynamicSlicing(this.linNumbersOfSliceResults);
		return output;
	}

	private void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	private void initializeWorkDirectoryInContainerName() {
		this.workDirectoryInContainer = "/giri/test/UnitTests/ST"+this.cPrgramDTO.getStudentID()+"HW"+ this.cPrgramDTO.getQuetionID() +"/";		
	}

	private void initializeWorkDirectory() {
		this.workDirectory = "/home/aaron/Desktop/GiriWorkDirectory/"+ this.cPrgramDTO.getStudentID()+"/"
				+ this.cPrgramDTO.getQuetionID() + "/";
	}


	public List<Integer> getDynamicResult() {
		return this.linNumbersOfSliceResults;
	}

}
