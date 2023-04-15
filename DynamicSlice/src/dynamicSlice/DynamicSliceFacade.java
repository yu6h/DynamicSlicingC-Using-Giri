package dynamicSlice;

import java.util.List;

import dynamicSlice.DTO.StudentProgramDTO;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.giriAdapter.GiriImpl;
import dynamicSlice.adapter.program.CprogramConverter;
import dynamicSlice.adapter.program.CprogramConverterUseArgvAsInput;
import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.usecase.in.DynamicSliceInput;
import dynamicSlice.usecase.in.DynamicSliceOutput;
import dynamicSlice.usecase.in.DynamicSliceUseCase;
import dynamicSlice.usecase.service.GiriDynamciSliceService;

public class DynamicSliceFacade {
	
	private StudentProgramDTO studentProgramDTO;
	
	public DynamicSliceFacade() {
		this.studentProgramDTO = new StudentProgramDTO();

	}
	
	public void setStudentID(String studentID) {
		this.studentProgramDTO.setStudentID(studentID);
	}
	
	public void setQuetionID(int quetionID) {
		this.studentProgramDTO.setQuetionID(quetionID);
	}
	
	public void setInputData(String inputData) {
		this.studentProgramDTO.setInputData(inputData);
	}
	
	public void setcProgramContent(String programContent) {
		this.studentProgramDTO.setcProgramContent(programContent);
	}
	
	public List<Integer> execute() {
		CprogramConverter converter = new CprogramConverterUseArgvAsInput(studentProgramDTO);
		converter.convert();
		CprogramUsedArgvAsInput formattedCProgramDTO = converter.generateCprogramExpertUsedArgvAsInput();
		DynamicSliceUseCase dynamicSliceservice = new GiriDynamciSliceService(new FileUtil(),new GiriImpl());
		DynamicSliceInput input = new DynamicSliceInput();
		input.setcProgram(formattedCProgramDTO);
		DynamicSliceOutput output = dynamicSliceservice.execute(input);
		List<Integer> lineNumbersOfResult = output.getLineNumbersOfDynamicSlicing();
		lineNumbersOfResult = converter.convertChangedLineNumbersToOriginalLineNumbers(lineNumbersOfResult);
		return lineNumbersOfResult;
	}

}
