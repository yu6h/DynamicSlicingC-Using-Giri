package dynamicSlice.usecase.service;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;



import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.usecase.in.DynamicSliceInput;
import dynamicSlice.usecase.in.DynamicSliceOutput;
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
	
	public DynamicSliceOutput execute(DynamicSliceInput input){
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
			if(this.isDynamicSlicingFailed(lineNumbersOfDynamicSlice.isEmpty())) {
				/*
				 * 若一筆測資中的其中一個output statement所得到的切片失敗 (至少會有output statement那一行)
				 * 那應該判定整筆測資得到的切片都失敗 以避免誤導學生 所以把切片集合清空
				 */
				lineNumbersSet.clear();
				break;
			}
			lineNumbersSet.addAll(lineNumbersOfDynamicSlice);
		}

		fileHandler.deleteWorkDirectory(new File(this.workDirectory));
		this.linNumbersOfSliceResults = new ArrayList<Integer>(lineNumbersSet);
		DynamicSliceOutput output = new DynamicSliceOutput();
		output.setLineNumbersOfDynamicSlicing(this.linNumbersOfSliceResults);
		return output;
	}
	
	private boolean isDynamicSlicingFailed(boolean isLineNumbersOfDynamicSliceEmpty) {
		return isLineNumbersOfDynamicSliceEmpty;
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
