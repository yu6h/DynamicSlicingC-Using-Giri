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
		studentProgramDTO.setInputData("2 1\n");
		studentProgramDTO.setcProgramContent("#include<stdio.h>\n" + 
				"#include<stdlib.h>\n" + 
				"int main(){\n" + 
				"    int n,a,i,s;\n" + 
				"    scanf(\"%d\",&n);\n" + 
				"    scanf(\"%d\",&a);\n" + 
				"    i = 1; \n" + 
				"    s = 1; \n" + 
				"    if (a > 0) \n" + 
				"        s = 0; \n" + 
				"    while (i<=n){ \n" + 
				"        if(a>0){ \n" + 
				"            s+=2;\n" + 
				"            a-=1; \n" + 
				"        }else \n" + 
				"            s*=2; \n" + 
				"        i++;\n" + 
				"    }\n" + 
				"    printf(\"%d\\n\",s);\n" + 
				"    printf(\"%d\\n\",a);\n" + 
				"    return 0;\n" + 
				"}\n");
		
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
