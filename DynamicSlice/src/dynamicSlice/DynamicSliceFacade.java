package dynamicSlice;

import java.util.List;

import dynamicSlice.DTO.StudentProgramDTO;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.giriAdapter.GiriImpl;
import dynamicSlice.adapter.program.CprogramConverter;
import dynamicSlice.adapter.program.CprogramConverterToCUsedArgvAsInput;
import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.usecase.in.DynamicSliceUseCaseInput;
import dynamicSlice.usecase.in.DynamicSliceUseCaseOutput;
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
	
	public DynamicSliceResult execute() {
		CprogramConverter converter = new CprogramConverterToCUsedArgvAsInput(studentProgramDTO);
		converter.convert();
		CprogramUsedArgvAsInput formattedCProgramDTO = converter.generateCprogramExpertUsedArgvAsInput();
		List<Integer> lineNumbersOfCoveredOutputStatement = formattedCProgramDTO.getLineNumbersOfOutputStatement();
		
		DynamicSliceUseCase dynamicSliceservice = new GiriDynamciSliceService(new FileUtil(),new GiriImpl());
		DynamicSliceUseCaseInput input = new DynamicSliceUseCaseInput();
		input.setcProgram(formattedCProgramDTO);
		DynamicSliceUseCaseOutput output = dynamicSliceservice.execute(input);
		List<Integer> lineNumbersOfDynamicSlice = output.getLineNumbersOfDynamicSlicing();
		
		lineNumbersOfDynamicSlice = converter.convertChangedLineNumbersToOriginalLineNumbers(lineNumbersOfDynamicSlice);
		lineNumbersOfCoveredOutputStatement = converter.convertChangedLineNumbersToOriginalLineNumbers(lineNumbersOfCoveredOutputStatement);
		
		DynamicSliceResult result = new DynamicSliceResult();
		result.setLineNumbersOfCoveredOutputStatement(lineNumbersOfCoveredOutputStatement);
		result.setLineNumbersOfDynamicSlice(lineNumbersOfDynamicSlice);
		return result;
	}

}
