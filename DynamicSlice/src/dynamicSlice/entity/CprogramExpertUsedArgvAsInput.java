package dynamicSlice.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CprogramExpertUsedArgvAsInput {

	private String cFileName;
	private String cFileNameWithoutExtension;
	private int quetionID;
	private String studentID;
	private String inputData;
	private String programContent;
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
		return this.programContent;
	}

	public void setcProgramContentUsedArgvAsInput(String cProgramContentUsedArgvAsInput) {
		this.programContent = cProgramContentUsedArgvAsInput;
	}
	
	public String getcFileNameWithoutExtension() {
		if(this.cFileNameWithoutExtension == null) {
			this.cFileNameWithoutExtension = this.cFileName.substring(0, this.cFileName.indexOf("."));
		}
		return this.cFileNameWithoutExtension;
	}
	
	public List<Integer> getLineNumbersOfArgumentInOutputStatement() {
		if(this.lineNumbersOfOutput==null) {
	        List<Integer> startIndexes = this.getStartIndexesOfOutputFunctionStatement();
	        lineNumbersOfOutput = new ArrayList<Integer>();
	        for(Integer index:startIndexes){
	            boolean isFind = false;
	            int brackets = 0;
	            int indexOfFgets =index;
	            int left = -1;
	            int right = 0;
	            while (!isFind){
	                if(this.programContent.charAt(indexOfFgets)=='('){
	                    brackets++;
	                    if(left == -1)left = indexOfFgets;
	                }else if(this.programContent.charAt(indexOfFgets)==')'){
	                    brackets--;
	                    if(brackets==0){
	                        right =indexOfFgets;
	                        isFind = true;
	                    }
	                }
	                indexOfFgets++;
	            }
	            int indexOfNextNewline = this.programContent.indexOf('\n',left+1);
	            int lineNumberAfterLeftBracket = countLineNumberInProgram(left+1);
	            if(indexOfNextNewline>right){
	                lineNumbersOfOutput.add(lineNumberAfterLeftBracket);
	            }else {
	                String stringInBrackets = this.programContent.substring(left+1,right);
	                String arr[] = stringInBrackets.split("\n");
	                for(int i =0;i<arr.length;i++){
	                    if(hasMatch(arr[i],"\\w")){
	                        lineNumbersOfOutput.add(lineNumberAfterLeftBracket+i);
	                    }
	                }
	            }
	        }
		}

        return this.lineNumbersOfOutput;
	}
	
	private int countLineNumberInProgram(int indexInProgram) {
        String str = this.programContent.substring(0,indexInProgram);
        int lineNumber = this.countNewLines(str) + 1;
        return lineNumber;
    }
    public int countNewLines(String str){
        return countMatches(str, "\n");
    }
	private boolean hasMatch(String str,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
    private List<Integer> getStartIndexesOfOutputFunctionStatement(){
        List<Integer> startIndexes = getStartIndexes(this.programContent,"[\\s]*(?<!\\w)(?:printf|puts|putc|putchar|fputs)[\\s]*\\(");
        List<Integer> result = startIndexes.stream().filter(x-> !isInsideStringDoubleQuotes(x)).collect(Collectors.toList());
        return result;
    }
    private List<Integer> getStartIndexes(String statement, String regex){
        List<Integer> result = new ArrayList<Integer>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(statement);
        while (matcher.find()) {
            result.add(matcher.start());
        }
        return result;
    }
    private boolean isInsideStringDoubleQuotes(int indexAtProgram){
        boolean isInside;
        if(this.programContent.charAt(indexAtProgram) =='\"'){
            isInside = true;
        }else {
            String substr = this.programContent.substring(indexAtProgram);
            String line = substr.substring(0,substr.indexOf('\n'));
            int numbersOfDoubleQuote = countMatches(line,"(?<!\\\\)\"");
            isInside = (numbersOfDoubleQuote%2 != 0);
        }
        return isInside;
    }
    
    private int countMatches(String statement,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(statement);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

}
