package dynamicSlice;

import java.util.List;

import dynamicSlice.adapter.program.CprogramConverter;
import dynamicSlice.adapter.program.CprogramConverterUseArgvAsInput;
import dynamicSlice.entity.CprogramExpertUsedArgvAsInput;
import dynamicSlice.usecase.DynamicSliceUseCase;

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
		this.setInputData(inputData);
	}
	
	public void setcProgramContent(String programContent) {
		this.setcProgramContent(programContent);
	}
	
	public List<Integer> execute() {
		CprogramConverter converter = new CprogramConverterUseArgvAsInput(studentProgramDTO);
		converter.convert();
		CprogramExpertUsedArgvAsInput formattedCProgramDTO = converter.generateCprogramExpertUsedArgvAsInput();
		DynamicSliceUseCase dynamicSliceUseCase = new DynamicSliceUseCase();
		List<Integer> lineNumbersOfResult = dynamicSliceUseCase.execute(formattedCProgramDTO);
		lineNumbersOfResult = converter.convertChangedLineNumbersToOriginalLineNumbers(lineNumbersOfResult);
		return lineNumbersOfResult;
	}

}
