package dynamicSlice.entity;

import java.util.List;

public class CprogramUsedArgvAsInputBuilder {

    private String cFileName;
    private String cFileNameWithoutExtension;
    private int quetionID;
    private String studentID;
    private String inputData;
    private String programContent;
    private List<Integer> lineNumbersOfOutputStatement;

    public static CprogramUsedArgvAsInputBuilder newInstance(){
        return new CprogramUsedArgvAsInputBuilder();
    }


    public CprogramUsedArgvAsInputBuilder buildcFileName(String cFileName) {
        this.cFileName = cFileName;
        return this;
    }


    public CprogramUsedArgvAsInputBuilder buildcFileNameWithoutExtension(String cFileNameWithoutExtension) {
        this.cFileNameWithoutExtension = cFileNameWithoutExtension;
        return this;
    }

    public CprogramUsedArgvAsInputBuilder buildQuetionID(int quetionID) {
        this.quetionID = quetionID;
        return this;
    }

    public CprogramUsedArgvAsInputBuilder buildStudentID(String studentID) {
        this.studentID = studentID;
        return this;
    }

    public CprogramUsedArgvAsInputBuilder buildInputData(String inputData) {
        this.inputData = inputData;
        return this;
    }

    public CprogramUsedArgvAsInputBuilder buildProgramContent(String programContent) {
        this.programContent = programContent;
        return this;
    }

    public CprogramUsedArgvAsInputBuilder buildLineNumbersOfOutputStatement(List<Integer> lineNumbersOfOutputStatement) {
        this.lineNumbersOfOutputStatement = lineNumbersOfOutputStatement;
        return this;
    }

    public CprogramUsedArgvAsInput build(){
        return new CprogramUsedArgvAsInput(cFileName,  cFileNameWithoutExtension,  quetionID,  studentID,  inputData,  programContent,  lineNumbersOfOutputStatement);
    }
}
