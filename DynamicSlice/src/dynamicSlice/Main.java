package dynamicSlice;
import java.util.List;

import dynamicSlice.DTO.StudentProgramDTO;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.giriAdapter.GiriImpl;
import dynamicSlice.adapter.program.CprogramConverter;
import dynamicSlice.adapter.program.CprogramConverterUseArgvAsInput;
import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.usecase.in.DynamicSlicingUseCase;
import dynamicSlice.usecase.service.GiriDynamciSliceService;

public class Main {

	public static void main(String[] args) {
		
		DynamicSliceFacade dynamciSlice = new DynamicSliceFacade();
		dynamciSlice.setStudentID("110598067");
		dynamciSlice.setQuetionID(1);
		dynamciSlice.setInputData("0 0\n");
		dynamciSlice.setcProgramContent("#include<stdio.h>\n" + 
				"#include<stdlib.h>\n" + 
				"int main(){\n" + 
				"    int a,b,i,s;\n" + 
				"    scanf(\"%d %d\",&a,&b);\n" + 
				"    i = 1; \n" + 
				"    s = 1; \n" + 
				"    if(a>0 && b>0){ \n" + 
				"            s+=5;//s+=2;\n" + 
				"            a-=3;//a-=1; \n" + 
				"    }else \n" + 
				"            s*=4;//s*=2; \n" + 
				"    printf(\n" + 
				"        \"%d\\n\",s);\n" + 
				"    printf(\"%d\\n\",a);\n" + 
				"    return 0;\n" + 
				"}\n");
		List<Integer> lineNumbersOfResult = dynamciSlice.execute();
		for(Integer lineNumber:lineNumbersOfResult) {
			System.out.println(lineNumber);
		}

	}

}
