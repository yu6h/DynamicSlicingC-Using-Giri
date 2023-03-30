package dynamicSlice;
import java.util.List;

import dynamicSlice.adapter.fileHandler.FileRepositoryImpl;
import dynamicSlice.adapter.giriAdapter.GiriImpl;
import dynamicSlice.adapter.program.CprogramConverter;
import dynamicSlice.adapter.program.CprogramConverterUseArgvAsInput;
import dynamicSlice.entity.CprogramExpertUsedArgvAsInput;
import dynamicSlice.usecase.in.DynamicSlicingUseCase;
import dynamicSlice.usecase.service.GiriDynamciSliceService;

public class Main {

	public static void main(String[] args) {
		
		
		StudentProgramDTO studentProgramDTO = new StudentProgramDTO();
		studentProgramDTO.setStudentID("110598067");
		studentProgramDTO.setQuetionID(1);
		studentProgramDTO.setInputData("2 1");
		studentProgramDTO.setcProgramContent("");
		
		CprogramConverter converter = new CprogramConverterUseArgvAsInput(studentProgramDTO);

		converter.convert();
		
		CprogramExpertUsedArgvAsInput formattedCProgramDTO = converter.generateCprogramExpertUsedArgvAsInput();
		

		DynamicSlicingUseCase dynamicSliceservice = new GiriDynamciSliceService(new FileRepositoryImpl(),new GiriImpl());
		List<Integer> lineNumbersOfResult = dynamicSliceservice.execute(formattedCProgramDTO);
		
		lineNumbersOfResult = converter.convertChangedLineNumbersToOriginalLineNumbers(lineNumbersOfResult);
		
		for(Integer lineNumber:lineNumbersOfResult) {
			System.out.println(lineNumber);
		}

	}

}
