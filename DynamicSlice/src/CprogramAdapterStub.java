
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

    private String removeComments(String programContent){
        String result="";
        int n = programContent.length();
        boolean s_cmt = false;
        boolean m_cmt = false;

        for(int i=0; i<n; i++){
            if (s_cmt == true && programContent.charAt(i) == '\n') {
                s_cmt = false;
                result += "\n";
            }else if ( m_cmt == true && programContent.charAt(i) == '*' && programContent.charAt(i +1) == '/') {
                m_cmt = false;
                i++;
            } else if (s_cmt || m_cmt) {
                if(programContent.charAt(i) == '\n') result += "\n";
                continue;
            } else if (programContent.charAt(i) == '/' && programContent.charAt(i+1) == '/') {
                s_cmt = true;
                i++;
            } else if (programContent.charAt(i) == '/' && programContent.charAt(i+1) == '*') {
                m_cmt = true;
                i++;
            }
            else result += programContent.charAt(i);

        }

        return result;
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
		String programContent = this.removeComments(this.studentProgramDTO.getcProgramContent());
		adaptedProgram.setcProgramContentUsedArgvAsInput(this.generateCprogramContentUsedArgvAsInput(programContent));
		return adaptedProgram;
	}
	
	


	public int getNumbersOfDummyLines() {
		return 2;
	}

}
