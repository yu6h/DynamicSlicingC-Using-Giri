package dynamicSlice;

import java.util.List;

import dynamicSlice.adapter.program.DTO.StudentProgramDTO;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.giriAdapter.GiriImpl;
import dynamicSlice.adapter.program.ProgramConversionAdapter;
import dynamicSlice.usecase.in.DynamicSliceUseCaseInput;
import dynamicSlice.usecase.in.DynamicSliceUseCaseOutput;
import dynamicSlice.usecase.in.DynamicSliceUseCase;
import dynamicSlice.usecase.service.DynamicSliceServiceGiriImpl;

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
		DynamicSliceUseCaseInput input = adapter.createDynamicSliceUseCaseInput();

		DynamicSliceUseCase dynamicSliceUseCase = new DynamicSliceServiceGiriImpl(new FileUtil(),new GiriImpl());

		DynamicSliceUseCaseOutput output = dynamicSliceUseCase.execute(input);

		List<Integer> lineNumbersOfDynamicSlicingInConvertedProgram = output.getLineNumbersOfDynamicSlicingInConvertedProgram();
		
		List<Integer> lineNumbersOfDynamicSlicingInOriginalProgram = adapter.convertChangedLineNumbersToOriginalLineNumbers(lineNumbersOfDynamicSlicingInConvertedProgram);

		DynamicSliceResult result = new DynamicSliceResult();

		result.setLineNumbersOfDynamicSlice(lineNumbersOfDynamicSlicingInOriginalProgram);
		return result;
	}

}
