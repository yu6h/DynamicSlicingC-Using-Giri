package dynamicSlice;

public class CprogramFilePreprocessorStub implements CprogramFilePreprocessor{

	public String getPreprocessProgramContent() {
		// TODO Auto-generated method stub
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
				"        if(a>0)\n" + 
				"            s+=2;\n" + 
				"        else\n" + 
				"            s*=2;\n" + 
				"        i++;\n" + 
				"    }\n" + 
				"    printf(\"%d\",s);\n" + 
				"    return 0;\n" + 
				"}";
	}

}
