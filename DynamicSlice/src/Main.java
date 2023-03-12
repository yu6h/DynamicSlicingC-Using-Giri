import java.util.List;

public class Main {

	public static void main(String[] args) {
		
		
		StudentProgramDTO studentProgramDTO = new StudentProgramDTO();
		studentProgramDTO.setcFileName("DD.c");
		studentProgramDTO.setStudentID("110598067");
		studentProgramDTO.setQuetionID(1);
		studentProgramDTO.setInputData("2 1");
		studentProgramDTO.setcProgramContent("");
		
		CprogramAdapter programAdapter = new CprogramAdapterStub();
		programAdapter.setOriginalCProgramDTO(studentProgramDTO);
		
		CprogramExpertUsedArgvAsInput formattedCProgramDTO = programAdapter.generateCprogramExpertUsedArgvAsInput();
		
		DynamicSliceUseCase dynamicSliceUseCase = new DynamicSliceUseCase();
		
		List<Integer> lineNumbersOfResult = dynamicSliceUseCase.execute(formattedCProgramDTO);
		
		DynamicResultAdapter resultAdapter = new DynamicResultAdapter();
		
		lineNumbersOfResult = resultAdapter.adapteToResultUsedScanfAsInput(lineNumbersOfResult, programAdapter.getNumbersOfDummyLines());
		
		for(Integer lineNumber:lineNumbersOfResult) {
			System.out.println(lineNumber);
		}

	}

}
