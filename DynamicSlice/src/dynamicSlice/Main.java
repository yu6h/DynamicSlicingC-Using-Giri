package dynamicSlice;
import java.util.List;

import dynamicSlice.adapter.program.CprogramConverter;
import dynamicSlice.adapter.program.CprogramConverterUseArgvAsInput;
import dynamicSlice.entity.CprogramExpertUsedArgvAsInput;
import dynamicSlice.usecase.DynamicSliceUseCase;

public class Main {

	public static void main(String[] args) {
		
		
		StudentProgramDTO studentProgramDTO = new StudentProgramDTO();
		studentProgramDTO.setcFileName("DD.c");
		studentProgramDTO.setStudentID("110598067");
		studentProgramDTO.setQuetionID(1);
		studentProgramDTO.setInputData("2 1");
		studentProgramDTO.setcProgramContent("");
		
		CprogramConverter converter = new CprogramConverterUseArgvAsInput(studentProgramDTO);

		converter.convert();
		
		CprogramExpertUsedArgvAsInput formattedCProgramDTO = converter.generateCprogramExpertUsedArgvAsInput();
		
		DynamicSliceUseCase dynamicSliceUseCase = new DynamicSliceUseCase();
		
		List<Integer> lineNumbersOfResult = dynamicSliceUseCase.execute(formattedCProgramDTO);
		
		lineNumbersOfResult = converter.convertChangedLineNumbersToOriginalLineNumbers(lineNumbersOfResult);
		
		for(Integer lineNumber:lineNumbersOfResult) {
			System.out.println(lineNumber);
		}

	}

}
