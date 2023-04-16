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
		dynamciSlice.setStudentID("110598066");
		dynamciSlice.setQuetionID(1);
		dynamciSlice.setInputData("14\n" + 
				"0 0 0 0 0 0 0 0 0 0 0 1 0 0\n" + 
				"0 0 0 0 0 0 1 0 0 0 0 0 0 0\n" + 
				"0 0 0 0 0 0 0 0 0 0 0 0 1 0\n" + 
				"0 0 0 1 0 0 0 0 0 0 0 0 0 0\n" + 
				"0 1 0 0 0 0 0 0 0 0 0 0 0 0\n" + 
				"0 0 0 0 0 0 0 0 0 1 0 0 0 0\n" + 
				"0 0 0 0 0 0 0 0 0 0 1 0 0 0\n" + 
				"0 0 1 0 0 0 0 0 0 0 0 0 0 0\n" + 
				"0 0 0 0 0 0 0 0 1 0 0 0 0 0\n" + 
				"0 0 0 0 1 0 0 0 0 0 0 0 0 0\n" + 
				"0 0 0 0 0 0 0 1 0 0 0 0 0 0\n" + 
				"0 0 0 0 0 0 0 0 0 0 0 0 0 1\n" + 
				"1 0 0 0 0 0 0 0 0 0 0 0 0 0\n" + 
				"0 0 0 0 0 1 0 0 0 0 0 0 0 0\n" + 
				"");
		dynamciSlice.setcProgramContent("#include <stdio.h>\n" + 
				"#include <stdlib.h>\n" + 
				"#include <string.h>\n" + 
				"\n" + 
				"void GetSequence(char *input,int *matrix)\n" + 
				"{\n" + 
				"    int counter = 0;\n" + 
				"    for (int i = 0; i < strlen(input); i++)\n" + 
				"    {\n" + 
				"        if (input[i] != ' ')\n" + 
				"        {\n" + 
				"            matrix[counter++] = input[i] - '0';\n" + 
				"        }\n" + 
				"    }\n" + 
				"}\n" + 
				"\n" + 
				"int CalculateAttacks(int m)\n" + 
				"{\n" + 
				"    int res = 0;\n" + 
				"\n" + 
				"    for (int i = 0; i < m; i++)\n" + 
				"    {\n" + 
				"        res += i;\n" + 
				"    }\n" + 
				"\n" + 
				"    return res;   \n" + 
				"}\n" + 
				"\n" + 
				"int CheckVertical(int (*matrix)[15], int n)\n" + 
				"{\n" + 
				"    int counter = 0;\n" + 
				"    int result = 0;\n" + 
				"    int helper = 0;\n" + 
				"    for (int col = 0; col < n; col++)\n" + 
				"    {\n" + 
				"        for (int row = 0; row < n; row++)\n" + 
				"        {\n" + 
				"            if (matrix[row][col] == 1)\n" + 
				"            {\n" + 
				"                counter++;\n" + 
				"            }\n" + 
				"        }\n" + 
				"\n" + 
				"        result += CalculateAttacks(counter);\n" + 
				"        counter = 0;\n" + 
				"    }\n" + 
				"\n" + 
				"    return result;\n" + 
				"}\n" + 
				"\n" + 
				"int CheckHorizontal(int (*matrix)[15], int n)\n" + 
				"{\n" + 
				"    int counter = 0;\n" + 
				"    int result = 0;\n" + 
				"    int helper = 0;\n" + 
				"    for (int row = 0; row < n; row++)\n" + 
				"    {\n" + 
				"        for (int col = 0; col < n; col++)\n" + 
				"        {\n" + 
				"            if (matrix[row][col] == 1)\n" + 
				"            {\n" + 
				"                counter++;\n" + 
				"            }\n" + 
				"        }\n" + 
				"\n" + 
				"        result += CalculateAttacks(counter);\n" + 
				"        counter = 0;\n" + 
				"    }\n" + 
				"\n" + 
				"    return result;\n" + 
				"}\n" + 
				"\n" + 
				"int CheckLefthToRight(int (*matrix)[15], int n)\n" + 
				"{\n" + 
				"    int counter = 0;\n" + 
				"    int result = 0;\n" + 
				"    int helper = 0;\n" + 
				"    for (int i = 0; i < n; i++)\n" + 
				"    {\n" + 
				"        helper = 0;\n" + 
				"        for (int col = i; col < n; col++)\n" + 
				"        {\n" + 
				"            if (matrix[helper][col] == 1)\n" + 
				"            {\n" + 
				"                counter++;\n" + 
				"            }\n" + 
				"            helper++;\n" + 
				"        }\n" + 
				"\n" + 
				"        result += CalculateAttacks(counter);\n" + 
				"        counter = 0;\n" + 
				"    }\n" + 
				"\n" + 
				"    for (int i = 1; i < n; i++)\n" + 
				"    {\n" + 
				"        helper = 0;\n" + 
				"        for (int row = i; row < n; row++)\n" + 
				"        {\n" + 
				"            if (matrix[row][helper] == 1)\n" + 
				"            {\n" + 
				"                counter++;\n" + 
				"            }\n" + 
				"            helper++;\n" + 
				"        }\n" + 
				"\n" + 
				"        result += CalculateAttacks(counter);\n" + 
				"        counter = 0;\n" + 
				"    }\n" + 
				"    \n" + 
				"    return result;\n" + 
				"}\n" + 
				"\n" + 
				"int CheckRigthToLefth(int (*matrix)[15], int n)\n" + 
				"{\n" + 
				"    int counter = 0;\n" + 
				"    int result = 0;\n" + 
				"    int helper = 0;\n" + 
				"    for (int i = n; i >= 0; i--)\n" + 
				"    {\n" + 
				"        helper = 0;\n" + 
				"        for (int col = i; col >= 0; col--)\n" + 
				"        {\n" + 
				"            if (matrix[helper][col] == 1)\n" + 
				"            {\n" + 
				"                counter++;\n" + 
				"            }\n" + 
				"            helper++;\n" + 
				"        }\n" + 
				"\n" + 
				"        result += CalculateAttacks(counter);\n" + 
				"        counter = 0;\n" + 
				"    }\n" + 
				"\n" + 
				"    for (int i = 1; i < n; i++)\n" + 
				"    {\n" + 
				"        helper = n-1;\n" + 
				"        for (int row = i; row < n; row++)\n" + 
				"        {\n" + 
				"            if (matrix[row][helper] == 1)\n" + 
				"            {\n" + 
				"                counter++;\n" + 
				"            }\n" + 
				"            helper--;\n" + 
				"        }\n" + 
				"\n" + 
				"        result += CalculateAttacks(counter);\n" + 
				"        counter = 0;\n" + 
				"    }\n" + 
				"    \n" + 
				"    return result;\n" + 
				"}\n" + 
				"\n" + 
				"\n" + 
				"int main()\n" + 
				"{\n" + 
				"    int n;\n" + 
				"    scanf(\"%d\",&n);\n" + 
				"    int Matrix[n][15];\n" + 
				"    char input[n*2];\n" + 
				"\n" + 
				"    int res = 0;\n" + 
				"\n" + 
				"    for (int i = 0; i < n; i++)\n" + 
				"    {\n" + 
				"        getchar();\n" + 
				"        fgets(input, n*2, stdin);\n" + 
				"        GetSequence(input,Matrix[i]);\n" + 
				"    }\n" + 
				"\n" + 
				"    res += CheckLefthToRight(Matrix,n);\n" + 
				"    res += CheckRigthToLefth(Matrix,n);\n" + 
				"    res += CheckHorizontal(Matrix,n);\n" + 
				"    res += CheckVertical(Matrix,n);\n" + 
				"\n" + 
				"    if(res == 0)\n" + 
				"    {\n" + 
				"        printf(\"There is no attack.\\n\");\n" + 
				"    }\n" + 
				"    else\n" + 
				"    {\n" + 
				"        printf(\"%d\",res);\n" + 
				"    }\n" + 
				"\n" + 
				"    return 0;\n" + 
				"}\n");
		List<Integer> lineNumbersOfResult = dynamciSlice.execute();
		for(Integer lineNumber:lineNumbersOfResult) {
			System.out.println(lineNumber);
		}

	}

}
