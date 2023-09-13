package dynamicSlice.adapter.service;

import dynamicSlice.adapter.commandTool.factory.CommandToolFactory;
import dynamicSlice.adapter.fileHandler.FileHandlerTool;

import java.util.List;
import java.util.Optional;


public class CCoverageServiceBuilder {
    private String cFileName;

    private String programContent;

    private String studentID;

    private int quetionID;

    private String inputDataAtCMD;

    private CommandToolFactory factory;

    private FileHandlerTool fileTool;

	public static CCoverageServiceBuilder newInstance(){
        return new CCoverageServiceBuilder();
    }
	
    public CCoverageServiceBuilder buildCFileName(String cFileName) {
        this.cFileName = cFileName;
        return this;
    }
	
    public CCoverageServiceBuilder buildProgramContent(String programContent) {
        this.programContent = programContent;
        return this;
    }

    public CCoverageServiceBuilder buildStudentID(String studentID) {
        this.studentID = studentID;
        return this;
    }
    public CCoverageServiceBuilder buildQuetionID(int quetionID) {
        this.quetionID = quetionID;
        return this;
    }
    public CCoverageServiceBuilder buildInputDataAtCMD(String inputDataAtCMD) {
        this.inputDataAtCMD = inputDataAtCMD;
        return this;
    }
    public CCoverageServiceBuilder buildCommandToolFactory(CommandToolFactory factory) {
        this.factory = factory;
        return this;
    }
    public CCoverageServiceBuilder buildFileHandlerTool(FileHandlerTool fileTool) {
        this.fileTool = fileTool;
        return this;
    }
    public CCoverageService build() {
        return new CCoverageService( cFileName,  programContent,  "",  studentID,  quetionID,  inputDataAtCMD,  factory,  fileTool);
    }
	
}
