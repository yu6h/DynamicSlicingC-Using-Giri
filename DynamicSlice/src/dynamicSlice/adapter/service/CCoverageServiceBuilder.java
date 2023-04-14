package dynamicSlice.adapter.service;

import dynamicSlice.adapter.commandTool.factory.CommandToolFactory;
import dynamicSlice.adapter.fileHandler.FileHandlerTool;


public class CCoverageServiceBuilder {
	
	private CCoverageService cCoverageService;
	
	public CCoverageServiceBuilder() {
		this.cCoverageService = new CCoverageService();
	}
	
    public CCoverageServiceBuilder buildCFileName(String cFileName) {
        this.cCoverageService.setCFileName(cFileName);
        return this;
    }
	
    public CCoverageServiceBuilder buildProgramContent(String programContent) {
        this.cCoverageService.setProgramContent(programContent);
        return this;
    }

    public CCoverageServiceBuilder buildStudentID(String studentID) {
        this.cCoverageService.setStudentID(studentID);
        return this;
    }
    public CCoverageServiceBuilder buildQuetionID(int quetionID) {
        this.cCoverageService.setQuetionID(quetionID);
        return this;
    }
    public CCoverageServiceBuilder buildInputData(String inputData) {
        this.cCoverageService.setInputData(inputData);
        return this;
    }
    public CCoverageServiceBuilder buildCommandToolFactory(CommandToolFactory factory) {
        this.cCoverageService.setFactory(factory);
        return this;
    }
    public CCoverageServiceBuilder buildFileHandlerTool(FileHandlerTool fileTool) {
        this.cCoverageService.setFileTool(fileTool);
        return this;
    }
    public CCoverageService build() {
        return this.cCoverageService;
    }
	
}
