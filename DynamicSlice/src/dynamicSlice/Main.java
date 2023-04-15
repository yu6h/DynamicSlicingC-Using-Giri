package dynamicSlice;
import java.util.List;

import dynamicSlice.DTO.StudentProgramDTO;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.giriAdapter.GiriImpl;
import dynamicSlice.adapter.program.CprogramConverter;
import dynamicSlice.adapter.program.CprogramConverterUseArgvAsInput;
import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.usecase.in.DynamicSliceUseCase;
import dynamicSlice.usecase.service.GiriDynamciSliceService;

public class Main {

	public static void main(String[] args) {
		
		DynamicSliceFacade dynamciSlice = new DynamicSliceFacade();
		dynamciSlice.setStudentID("110598067");
		dynamciSlice.setQuetionID(1);
		dynamciSlice.setInputData("0\n0\n");
		dynamciSlice.setcProgramContent("#include<stdio.h>\n" + 
				"#include<stdlib.h>\n" + 
				"int main(){\n" + 
				"    int a,b;\n" + 
				"    scanf(\"%d %d\",&a,&b);\n" + 
				"    int n = a+b;\n" + 
				"    if(a>0 && b>0){ \n" + 
				"        a = a-b;//a*b;\n" + 
				"        printf(\"%d\\n\",b);\n" + 
				"    }else \n" + 
				"        a = a+b;// a-b;\n" + 
				"    printf(\n" + 
				"        \"%d\\n\",a);\n" + 
				"    return 0;\n" + 
				"}\n");
		List<Integer> lineNumbersOfResult = dynamciSlice.execute();
		for(Integer lineNumber:lineNumbersOfResult) {
			System.out.println(lineNumber);
		}

	}

}
