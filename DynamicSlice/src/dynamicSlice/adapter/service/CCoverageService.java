package dynamicSlice.adapter.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dynamicSlice.adapter.commandTool.ProgramLanguage;
import dynamicSlice.adapter.commandTool.compiler.MyCompiler;
import dynamicSlice.adapter.commandTool.factory.CommandToolFactory;
import dynamicSlice.adapter.commandTool.gcov.GcovTool;
import dynamicSlice.adapter.fileHandler.FileHandlerTool;
import dynamicSlice.usecase.out.FileRepository;

public class CCoverageService {
	
	private String cFileName;
	
	private String programContent;
	
	private String workdirectory;
	
	private String studentID;
	
	private int quetionID;

	private String inputDataAtCMD;
	
	private List<Integer> lineNumbersOfUncoveredStatement;
	
    private CommandToolFactory factory;

	private FileHandlerTool fileTool;

	private String cFileNameWithoutExtension;
    
    public CCoverageService() {
    }
	
    public CCoverageService(FileHandlerTool fileRepository,CommandToolFactory factory) {
    	this.fileTool = fileRepository;
    	this.factory = factory;
    }
    
    public void analyze() {
    	this.initializeWorkdirectory("/home/aaron/Desktop/GcovWorkDirectory/"+this.quetionID+File.separator+studentID+File.separator);
    	MyCompiler myCompiler = factory.createCompiler(ProgramLanguage.C,this.workdirectory);
    	this.fileTool.createWorkDirectory(new File(this.workdirectory));
    	this.fileTool.writeFile(this.workdirectory, this.cFileName, this.programContent);
    	myCompiler.compile(this.cFileName);
    	myCompiler.run(this.cFileName, this.inputDataAtCMD);
    	this.fileTool.renameFile(this.workdirectory,"exe-"+this.cFileNameWithoutExtension+".gcda", this.cFileNameWithoutExtension+".gcda");
    	this.fileTool.renameFile(this.workdirectory,"exe-"+this.cFileNameWithoutExtension+".gcno", this.cFileNameWithoutExtension+".gcno");
    	GcovTool gcovTool = factory.createGcovTool(this.workdirectory);
    	gcovTool.runGcovCommand(this.cFileName);
    	String gcovFileContent = this.fileTool.readFile(this.workdirectory , this.cFileName+".gcov");
    	this.lineNumbersOfUncoveredStatement = findLineNumberOfUncovered(gcovFileContent,"(?<=#####:)[\\s]*[\\d]*(?=:)");
    	this.fileTool.deleteWorkDirectory(new File(this.workdirectory));
    }

	public List<Integer> getLineNumbersOfUncoveredStatement() {
		return this.lineNumbersOfUncoveredStatement;
	}
	
    private List<Integer> findLineNumberOfUncovered(String fileContent, String regex){
        List<Integer> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find()) {
            int index = matcher.start();
            if(!isInsideStringDoubleQuotes(fileContent , index)){
                result.add(Integer.parseInt(matcher.group().trim()));
            }
        }
        return result;
    }

    private boolean isInsideStringDoubleQuotes(String fileContent,int indexAtProgram){
        boolean isInside;
        if(fileContent.charAt(indexAtProgram) =='\"'){
            isInside = true;
        }else {
            String substr = fileContent.substring(indexAtProgram);
            String line = substr.substring(0,substr.indexOf('\n'));
            int numbersOfDoubleQuote = countMatches(line,"(?<!\\\\)\"");
            isInside = (numbersOfDoubleQuote%2 != 0);
        }
        return isInside;
    }

    public int countMatches(String str,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
    

	public CommandToolFactory getFactory() {
		return factory;
	}

	public void setFactory(CommandToolFactory factory) {
		this.factory = factory;
	}

	public FileHandlerTool getFileTool() {
		return fileTool;
	}

	public void setFileTool(FileHandlerTool fileTool) {
		this.fileTool = fileTool;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public int getQuetionID() {
		return this.quetionID;
	}

	public void setQuetionID(int quetionID) {
		this.quetionID = quetionID;
	}

	public void setProgramContent(String programContent) {
		this.programContent = programContent;
	}

	public void setCFileName(String cFileName) {
		this.cFileName = cFileName;
	}

	public void setInputDataAtCMD(String inputDataAtCMD) {
		this.inputDataAtCMD = inputDataAtCMD;
	} 
	
	private void initializeWorkdirectory(String workdirectory) {
		this.workdirectory = workdirectory;
	}

	public void setCFileNameWithOutExtension(String cFileNameWithoutExtension) {
		this.cFileNameWithoutExtension = cFileNameWithoutExtension;
	}

}
