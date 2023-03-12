import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CprogramExpertUsedArgvAsInput {

	private String cFileName;
	private String cFileNameWithoutExtension;
	private int quetionID;
	private String studentID;
	private String inputData;
	private String cProgramContentUsedArgvAsInput;
	private List<Integer> lineNumbersOfOutput;

	public String getcFileName() {
		return cFileName;
	}

	public void setcFileName(String cFileName) {
		this.cFileName = cFileName;
	}

	public int getQuetionID() {
		return quetionID;
	}

	public void setQuetionID(int quetionID) {
		this.quetionID = quetionID;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public String getcProgramContentUsedArgvAsInput() {
		return cProgramContentUsedArgvAsInput;
	}

	public void setcProgramContentUsedArgvAsInput(String cProgramContentUsedArgvAsInput) {
		this.cProgramContentUsedArgvAsInput = cProgramContentUsedArgvAsInput;
	}
	
	public String getcFileNameWithoutExtension() {
		if(this.cFileNameWithoutExtension == null) {
			this.cFileNameWithoutExtension = this.cFileName.substring(0, this.cFileName.indexOf("."));
		}
		return this.cFileNameWithoutExtension;
	}
	
	
	public String generateInputDataInOneLine() {
	    String str = this.inputData.replace("\r\n", "\n"); // convert windows line endings to linux format 
	    str = str.replace("\r", "\n"); // convert (remaining) mac line endings to linux format
	    return str.replace("\n", " "); // count total line endings
	}
	
	public List<Integer> getLineNumbersOfOutputStatement() {
        if(this.lineNumbersOfOutput == null) {
        	this.lineNumbersOfOutput = new ArrayList<Integer>();
        	String[] arr = this.cProgramContentUsedArgvAsInput.split("\n");
            for(int i=0;i<arr.length;i++){

                if(checkIfOutputFunctionNameExistInStatement(arr[i])){
                    boolean isAOutputStatement = checkIfThereIsAnyOutputFunctionOutsideOfDoubleQuotes(arr[i]);
                    if(isAOutputStatement){
                        this.lineNumbersOfOutput.add(i+1);
                    }

                }
            }
        }
		return this.lineNumbersOfOutput;
	}

    private boolean checkIfThereIsAnyOutputFunctionOutsideOfDoubleQuotes(String statement) {
        List<Integer> indexesOfDoubleQuote = getStartIndexOfDoubleQuote(statement);
        List<Integer> startIndexesOfOutputFunction = getStartIndexesOfPrintfOrPutsOrPutcharFunction(statement);
        int length = statement.length();
        boolean isIndexInsideDoubleQuotes = false;
        boolean doesAnyOutputStatementExist = false;
        for(int index =0;index<length;index++){
            if(indexesOfDoubleQuote.contains(index))isIndexInsideDoubleQuotes = !isIndexInsideDoubleQuotes;
            else if (!isIndexInsideDoubleQuotes && startIndexesOfOutputFunction.contains(index) ) {
                doesAnyOutputStatementExist = true;
                break;
            }
        }
        return  doesAnyOutputStatementExist;
    }

    private List<Integer> getStartIndexOfDoubleQuote(String statement) {
        return getStartIndex(statement,"(?<!\\\\)\"");
	}

	private List<Integer> getStartIndexesOfPrintfOrPutsOrPutcharFunction(String statement){
        return getStartIndex(statement,"[\\s]*(?<!\\w)(?:printf|puts|putchar)[\\s]*\\(");
    }
    
    private List<Integer> getStartIndex(String statement, String regex){
        List<Integer> result = new ArrayList<Integer>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(statement);
        while (matcher.find()) {
            result.add(matcher.start());
        }
        return result;
    }

	private boolean checkIfOutputFunctionNameExistInStatement(String statement){
        return checkIfRegexExistInStatement(statement,"[\\s]*(?<!\\w)(?:printf|puts|putchar)[\\s]*\\(");
    }

    private boolean checkIfRegexExistInStatement(String statement, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(statement);
        boolean isExist = matcher.find();
        return isExist;
    }

}
