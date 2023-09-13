package dynamicSlice.usecase.service;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.entity.CprogramUsedArgvAsInputBuilder;
import dynamicSlice.usecase.in.DynamicSliceUseCaseInput;
import dynamicSlice.usecase.in.DynamicSliceUseCaseOutput;
import dynamicSlice.usecase.in.DynamicSliceUseCase;
import dynamicSlice.usecase.out.Giri;
import dynamicSlice.usecase.out.FileRepository;

public class DynamicSliceServiceGiriImpl implements DynamicSliceUseCase{
	
	private String workDirectory;
	
	private String containerName;
	
	private String workDirectoryInContainer;

	private CprogramUsedArgvAsInput cprogramUsedArgvAsInput;

	private List<Integer> linNumbersOfSliceResults;

	private FileRepository fileHandler;

	private Giri giri;
	
	public DynamicSliceServiceGiriImpl(FileRepository fileHandler, Giri giri) {
		this.fileHandler = fileHandler;
		this.giri = giri;
		this.setContainerName("giriContainer");
		giri.setContainerName(this.containerName);
	}
	
	public DynamicSliceUseCaseOutput execute(DynamicSliceUseCaseInput input){
		this.cprogramUsedArgvAsInput = CprogramUsedArgvAsInputBuilder.newInstance()
				.buildcFileName(input.getcFileName())
				.buildcFileNameWithoutExtension(input.getcFileNameWithoutExtension())
				.buildQuetionID(input.getQuetionID())
				.buildStudentID(input.getStudentID())
				.buildInputData(input.getInputData())
				.buildProgramContent(input.getProgramContent())
				.buildLineNumbersOfOutputStatement(input.getLineNumbersOfOutputStatement())
				.build();
		TreeSet<Integer> lineNumbersSet = new TreeSet<Integer>();

		
		this.initializeWorkDirectory();
		this.initializeWorkDirectoryInContainerName();
		fileHandler.deleteWorkDirectory(new File(this.workDirectory));
		fileHandler.createWorkDirectory(new File(this.workDirectory));
		
		fileHandler.writePreprocessedCprogramFile(this.workDirectory, this.cprogramUsedArgvAsInput.getcFileName(), this.cprogramUsedArgvAsInput.getProgramContent());
		fileHandler.writeMakeFile(this.workDirectory,this.cprogramUsedArgvAsInput.getcFileNameWithoutExtension(), this.cprogramUsedArgvAsInput.getInputData());
		List<Integer> lineNumberOfTargetStatement = this.cprogramUsedArgvAsInput.getLineNumbersOfOutputStatement();

		for(Integer lineNumberOfTarget:lineNumberOfTargetStatement) {
			fileHandler.writeLocTxtFile(this.workDirectory,this.cprogramUsedArgvAsInput.getcFileName(),lineNumberOfTarget);
			giri.deleteWorkDirectoryInContainer(this.workDirectoryInContainer);
			giri.createWorkDirectoryInContainer(this.workDirectoryInContainer);
			giri.copyMakeFileIntoContainer(this.workDirectory, this.workDirectoryInContainer);
			giri.copyLotTxtFileIntoContainer( this.workDirectory, this.workDirectoryInContainer);
			giri.copyCProgramFileIntoContainer(this.workDirectory, this.workDirectoryInContainer, this.cprogramUsedArgvAsInput.getcFileName());
			fileHandler.deleteSliceLocFile(this.workDirectory, this.cprogramUsedArgvAsInput.getcFileNameWithoutExtension());
			giri.makeAndDownloadSlicLocFileIntoWorkDirectory(this.workDirectoryInContainer, this.workDirectory, this.cprogramUsedArgvAsInput.getcFileNameWithoutExtension(),
					fileHandler);
			List<Integer> lineNumbersOfDynamicSlice = fileHandler.readSliceLocFile(this.workDirectory, this.cprogramUsedArgvAsInput.getcFileNameWithoutExtension());
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
		this.workDirectoryInContainer = "/giri/test/UnitTests/ST"+this.cprogramUsedArgvAsInput.getStudentID()+"HW"+ this.cprogramUsedArgvAsInput.getQuetionID() +"/";
	}

	private void initializeWorkDirectory() {
		this.workDirectory = "/home/aaron/Desktop/GiriWorkDirectory/"+ this.cprogramUsedArgvAsInput.getStudentID()+"/"
				+ this.cprogramUsedArgvAsInput.getQuetionID() + "/";
	}


}
