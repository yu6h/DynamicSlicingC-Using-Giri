package dynamicSlice;

import java.util.List;

import dynamicSlice.adapter.program.DTO.StudentProgramDTO;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.giriAdapter.GiriAdapter;
import dynamicSlice.adapter.program.ProgramConversionAdapter;
import dynamicSlice.usecase.in.DynamicSliceUseCaseInput;
import dynamicSlice.usecase.in.DynamicSliceUseCaseOutput;
import dynamicSlice.usecase.in.DynamicSliceUseCase;
import dynamicSlice.usecase.service.DynamicSliceUseCaseImpl;

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
		ProgramConversionAdapter adapter = new ProgramConversionAdapter(studentProgramDTO);
		adapter.execute();
		DynamicSliceUseCaseInput input = adapter.getDynamicSliceUseCaseInput();

		DynamicSliceUseCase dynamicSliceUseCase = new DynamicSliceUseCaseImpl(new GiriAdapter(new FileUtil()));

		DynamicSliceUseCaseOutput output = dynamicSliceUseCase.execute(input);

		List<Integer> lineNumbersOfDynamicSlicingInConvertedProgram = output.getLineNumbersOfDynamicSlicingInConvertedProgram();
		
		List<Integer> lineNumbersOfDynamicSlicingInOriginalProgram = adapter.convertChangedLineNumbersToOriginalLineNumbers(lineNumbersOfDynamicSlicingInConvertedProgram);

		DynamicSliceResult result = new DynamicSliceResult();

		result.setLineNumbersOfDynamicSlice(lineNumbersOfDynamicSlicingInOriginalProgram);
		return result;
	}

}
