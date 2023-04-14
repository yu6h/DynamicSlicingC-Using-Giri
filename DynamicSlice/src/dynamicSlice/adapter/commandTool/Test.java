package dynamicSlice.adapter.commandTool;

import dynamicSlice.adapter.commandTool.factory.CommandToolFactoryUbuntuImpl;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.service.CCoverageService;
import dynamicSlice.adapter.service.CCoverageServiceBuilder;

public class Test {

	public static void main(String[] args) {
		CCoverageServiceBuilder builder = new CCoverageServiceBuilder();
		CCoverageService service = builder.buildCFileName("11059.c")
				.buildStudentID("110598067")
				.buildQuetionID(2)
				.buildInputDataAtCMD("2*NbsP133*BrnL31")
				.buildProgramContent("#include <stdarg.h>\n" + 
						"#include <string.h>\n" + 
						"#include <stdlib.h>\n" + 
						"char* INPUT110598067;\n" + 
						"int USED110598067;\n" + 
						"#include<stdio.h>\n" + 
						"#include<stdlib.h>\n" + 
						"char* read110598067Fgets(char* str,int size){\n" + 
						"    char *format =  (char*)malloc(20 * sizeof(char));\n" + 
						"    char* temp;int i;size--;\n" + 
						"    if(*INPUT110598067 == '\\0')return NULL;\n" + 
						"    size_t npos = strcspn(INPUT110598067, \"\\n\");\n" + 
						"    sprintf(format,\"%%%d[^\\\\n]%%n\",size);\n" + 
						"    if(npos<size){\n" + 
						"        temp =  (char*)malloc((npos+1) * sizeof(char));\n" + 
						"        for(i=0;i<=npos;i++)temp[i] = INPUT110598067[i];\n" + 
						"        temp[npos+1] ='\\0';\n" + 
						"        sscanf(temp,format,str,&USED110598067);\n" + 
						"        INPUT110598067 = INPUT110598067 + npos +1;\n" + 
						"        free(temp);\n" + 
						"    }else{\n" + 
						"        sscanf(INPUT110598067,format,str,&USED110598067);\n" + 
						"        INPUT110598067 = INPUT110598067 + USED110598067;\n" + 
						"    }\n" + 
						"    free(format);\n" + 
						"    return str;\n" + 
						"}\n" + 
						"char* read110598067Gets(char* str,const char* format, ...){\n" + 
						"    char *ptr;\n" + 
						"    if(*INPUT110598067 == '\\n'){\n" + 
						"        strcpy(str,\"\");\n" + 
						"        INPUT110598067++;\n" + 
						"        return str;\n" + 
						"    }else if(*INPUT110598067 == '\\0'){\n" + 
						"        return NULL;\n" + 
						"    }else{\n" + 
						"    va_list args;\n" + 
						"    va_start(args, format);\n" + 
						"    vsscanf(INPUT110598067, format, args);\n" + 
						"    va_end(args);\n" + 
						"    INPUT110598067 = INPUT110598067 + USED110598067;\n" + 
						"    return str;}\n" + 
						"}\n" + 
						"char read110598067Getchar(){return *INPUT110598067++;}\n" + 
						"int read110598067Scanf(const char * format, ... ){\n" + 
						"    va_list args;\n" + 
						"    va_start(args, format);\n" + 
						"    int n = vsscanf(INPUT110598067, format, args);\n" + 
						"    va_end(args);\n" + 
						"    INPUT110598067 = INPUT110598067 + USED110598067;\n" + 
						"    return n;}\n" + 
						"void convertSpecialCharacter110598067(char *str,char *old,char new){\n" + 
						"    char *result;    \n" + 
						"    char *ptrResult;char *ptrOriginal;char *nextPtrOriginal;\n" + 
						"    nextPtrOriginal = ptrOriginal = strstr(str, old);\n" + 
						"    ptrResult = strstr(str,old);\n" + 
						"    while(ptrOriginal!= NULL && nextPtrOriginal!=NULL){\n" + 
						"        nextPtrOriginal  = strstr(ptrOriginal+ strlen(old), old);\n" + 
						"        ptrOriginal = ptrOriginal + strlen(old);\n" + 
						"        *ptrResult = new;\n" + 
						"        while( *ptrResult++!='\\0' && ptrOriginal!=nextPtrOriginal  ){\n" + 
						"            *ptrResult = *ptrOriginal++;\n" + 
						"        };\n" + 
						"    }\n" + 
						"}\n" + 
						"void convertInputData110598067(char *inputData){\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*BrnL\",'\\n');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*NbsP\",' ');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*AsD38\",'&');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*LtS60\",'<');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*GtS62\",'>');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*QuT878\",'\\\"');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*SqT877\",'\\'');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*LpT203\",'(');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*RpT301\",')');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*EpT33\",'!');\n" + 
						"    convertSpecialCharacter110598067(inputData,\"*PdS410\",'#');\n" + 
						"}\n" + 
						"int main(int argc, char *argv[]){\n" + 
						"INPUT110598067=argv[1];convertInputData110598067(INPUT110598067);\n" + 
						"    int a,b;\n" + 
						"    read110598067Scanf(\"%d %d%n\",&a,&b,&USED110598067);\n" + 
						"    int n = a+b;\n" + 
						"    if(a>0 && b>0){ \n" + 
						"        a = a-b;//a*b;\n" + 
						"    }else \n" + 
						"        a = a+b;// a-b;\n" + 
						"    printf(\n" + 
						"        \"%d\\n\",a);\n" + 
						"    return 0;\n" + 
						"}\n")
				.buildFileHandlerTool(new FileUtil())
				.buildCommandToolFactory(new CommandToolFactoryUbuntuImpl())
				.build();
		
		service.analyze();
		System.out.println(service.getLineNumbersOfUncoveredStatement());
		
	}

}
