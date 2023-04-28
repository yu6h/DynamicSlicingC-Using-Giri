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
		dynamciSlice.setInputData("100\n" + 
				"1 6 0 7 5 0 7 1 8 5 2 2 5 4 8 4 5 7 1 4 4 3 9 9 0 8 3 8 1 5 7 1 0 9 6 7 3 0 3 1 2 0 5 3 8 7 7 0 3 1 5 9 2 4 6 2 7 2 1 9 8 0 9 7 6 2 7 3 8 6 1 0 1 8 7 5 2 0 8 0 5 8 6 0 2 0 3 6 3 3 6 1 5 9 3 2 8 2 2 4\n");
		dynamciSlice.setcProgramContent("#include <stdio.h>\n" + 
				"#include <string.h>\n" + 
				"\n" + 
				"int printArray(char array[])\n" + 
				"{\n" + 
				"    int i = 0;\n" + 
				"    while (array[i] != '\\0')\n" + 
				"    {\n" + 
				"        printf(\"%c\", array[i]);\n" + 
				"        i++;\n" + 
				"    }\n" + 
				"    printf(\"\\n\");\n" + 
				"}\n" + 
				"\n" + 
				"int S(char X[], char Y[])\n" + 
				"{\n" + 
				"    int count = 0;\n" + 
				"    if (strlen(X) == 0)\n" + 
				"    {\n" + 
				"        return 0;\n" + 
				"    }\n" + 
				"    for (int i = 0; i < strlen(Y); i++)\n" + 
				"    {\n" + 
				"        if (*X > *(Y + i))\n" + 
				"        {\n" + 
				"            // printf(\"%c %c\\n\", *X, *(Y + i));\n" + 
				"            count++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"    X = X + 1;\n" + 
				"    return count + S(X, Y);\n" + 
				"}\n" + 
				"\n" + 
				"int W(char X[])\n" + 
				"{\n" + 
				"    int count = 0;\n" + 
				"    if (strlen(X) == 1)\n" + 
				"    {\n" + 
				"        return 0;\n" + 
				"    }\n" + 
				"    for (int i = 1; i < strlen(X); i++)\n" + 
				"    {\n" + 
				"        if (*X > *(X + i))\n" + 
				"        {\n" + 
				"            // printf(\"%c %c\\n\", *X, *(X + i));\n" + 
				"            count++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"    X = X + 1;\n" + 
				"    return count + W(X);\n" + 
				"}\n" + 
				"int main()\n" + 
				"{\n" + 
				"    // input\n" + 
				"    int lengthOfInput = 0;\n" + 
				"    scanf(\"%d\\n\", &lengthOfInput);\n" + 
				"    char X[lengthOfInput / 2];\n" + 
				"    for (int i = 0; i < lengthOfInput / 2; i++)\n" + 
				"    {\n" + 
				"        scanf(\"%c \", &X[i]);\n" + 
				"    }\n" + 
				"    X[lengthOfInput / 2] = '\\0';\n" + 
				"\n" + 
				"    // printArray(X);\n" + 
				"    char Y[lengthOfInput - (lengthOfInput / 2)];\n" + 
				"    for (int i = 0; i < lengthOfInput - (lengthOfInput / 2); i++)\n" + 
				"    {\n" + 
				"        scanf(\" %c\", &Y[i]);\n" + 
				"    }\n" + 
				"    Y[lengthOfInput - (lengthOfInput / 2)] = '\\0';\n" + 
				"    // printArray(Y);\n" + 
				"    // printf(\"\\n\");\n" + 
				"    // printf(\"%d\\n\", W(X));\n" + 
				"    // printf(\"%d\\n\", W(Y));\n" + 
				"    printf(\"haha\");\n" + 
				"    printf(\"%d\", W(X) + W(Y) + S(X, Y));\n" + 
				"}\n");
		List<Integer> lineNumbersOfResult = dynamciSlice.execute();
		for(Integer lineNumber:lineNumbersOfResult) {
			System.out.println(lineNumber);
		}

	}

}
