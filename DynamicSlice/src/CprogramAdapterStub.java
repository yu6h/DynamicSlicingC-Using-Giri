
public class CprogramAdapterStub implements CprogramAdapter {
	
	
	private StudentProgramDTO studentProgramDTO;

	private String generateCprogramContentUsedArgvAsInput(String programContent) {
		return "#include<stdio.h>\n" + 
				"#include<stdlib.h>\n" + 
				"int main(int argc, char *argv[]){\n" + 
				"    int n,a,i,s;\n" + 
				"    n = atoi(argv[1]);\n" + 
				"    a = atoi(argv[2]);\n" + 
				"    i = 1;\n" + 
				"    s = 1;\n" + 
				"    if (a > 0)\n" + 
				"        s = 0;\n" + 
				"    while (i<=n){\n" + 
				"        if(a>0){\n" + 
				"            s+=2;\n" + 
				"            a-=1;\n" + 
				"        }else\n" + 
				"            s*=2;\n" + 
				"        i++;\n" + 
				"    }\n" + 
				"    printf(\"%d\",s);\n" + 
				"    printf(\"%d\",a);\n" + 
				"    return 0;\n" + 
				"}";
	}



	public void setOriginalCProgramDTO(StudentProgramDTO studentProgramDTO) {
		this.studentProgramDTO = studentProgramDTO;
	}

	public CprogramExpertUsedArgvAsInput generateCprogramExpertUsedArgvAsInput() {
		CprogramExpertUsedArgvAsInput adaptedProgram = new CprogramExpertUsedArgvAsInput();
		adaptedProgram.setcFileName(this.studentProgramDTO.getcFileName());
		adaptedProgram.setQuetionID(this.studentProgramDTO.getQuetionID());
		adaptedProgram.setStudentID(this.studentProgramDTO.getStudentID());
		adaptedProgram.setInputData(this.studentProgramDTO.getInputData());
		adaptedProgram.setcProgramContentUsedArgvAsInput(this.generateCprogramContentUsedArgvAsInput(this.studentProgramDTO.getcProgramContent()));
		return adaptedProgram;
	}
	
	


	public int getNumbersOfDummyLines() {
		return 2;
	}

}
