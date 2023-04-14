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
				.buildInputData("2\n3\n")
				.buildProgramContent("#include<stdio.h>\n" + 
				"#include<stdlib.h>\n" + 
				"int main(){\n" + 
				"    int a,b;\n" + 
				"    scanf(\"%d %d\",&a,&b);\n" + 
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
