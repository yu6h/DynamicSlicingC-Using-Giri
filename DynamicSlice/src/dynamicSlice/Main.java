package dynamicSlice;
import java.util.List;

import dynamicSlice.DTO.StudentProgramDTO;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.giriAdapter.GiriImpl;
import dynamicSlice.adapter.program.CprogramConverter;
import dynamicSlice.adapter.program.CprogramConverterToCUsedArgvAsInput;
import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.usecase.in.DynamicSliceUseCase;
import dynamicSlice.usecase.service.GiriDynamciSliceService;

public class Main {

	public static void main(String[] args) {
		
		DynamicSliceFacade dynamciSlice = new DynamicSliceFacade();
		dynamciSlice.setStudentID("110598066");
		dynamciSlice.setQuetionID(1);
		dynamciSlice.setInputData("1\n");
		dynamciSlice.setcProgramContent("#include <math.h>\n"
				+ "#include <stdio.h>\n"
				+ "#include <stdlib.h>\n"
				+ "\n"
				+ "int main()\n"
				+ " {\n"
				+ "   int x, y, z;\n"
				+ "\n"
				+ "   scanf(\"%d\",&x);\n"
				+ "\n"
				+ "   if (x < 0)\n"
				+ "   {\n"
				+ "      y = sqrt(x);\n"
				+ "      z = pow(2, x);\n"
				+ "    } else {\n"
				+ "       if (x == 0)\n"
				+ "        {\n"
				+ "           y = sqrt(x * 2);\n"
				+ "           z = pow(3, x);\n"
				+ "        } else {\n"
				+ "            y = sqrt(x * 3);\n"
				+ "            z = pow(4, x);\n"
				+ "       }\n"
				+ "    }\n"
				+ "\n"
				+ "    printf(\"%d\\n\", y);\n"
				+ "    printf(\"%d\\n\", z);\n"
				+ "   \n"
				+ "   return z;\n"
				+ "}\n");
		List<Integer> lineNumbersOfResult = dynamciSlice.execute().getLineNumbersOfDynamicSlice();
		for(Integer lineNumber:lineNumbersOfResult) {
			System.out.println(lineNumber);
		}

	}

}
