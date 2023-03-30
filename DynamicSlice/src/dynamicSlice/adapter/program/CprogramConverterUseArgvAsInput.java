package dynamicSlice.adapter.program;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dynamicSlice.DTO.StudentProgramDTO;
import dynamicSlice.entity.CprogramExpertUsedArgvAsInput;

public class CprogramConverterUseArgvAsInput implements CprogramConverter {
	
	CprogramExpertUsedArgvAsInput adaptedProgram;
	
	StudentProgramDTO studentProgramDTO;
	
	private String programContent;
	
	private ArrayDeque<InsertedLines> stackOfLineChanges;
	
	private String functionOfConvertSpace =
            "void convertSpace110598067(char* str){\n" +
                    "    char *result;char *specialSpace = \"nbsp;\";\n" +
                    "    char *ptrResult;char *ptrOriginal;char *nextPtrOriginal;\n" +
                    "    nextPtrOriginal = ptrOriginal = strstr(str, specialSpace);\n" +
                    "    ptrResult = strstr(str,specialSpace);\n" +
                    "    while(ptrOriginal!= NULL && nextPtrOriginal!=NULL){\n" +
                    "        nextPtrOriginal  = strstr(ptrOriginal+ strlen(specialSpace), specialSpace);\n" +
                    "        ptrOriginal = ptrOriginal + strlen(specialSpace);*ptrResult = ' ';\n" +
                    "        while( *ptrResult++!='\\0' && ptrOriginal!=nextPtrOriginal  ){\n" +
                    "            *ptrResult = *ptrOriginal++;};\n" +
                    "    }}\n";

    private String functionOfConvertNewLine =
            "void convertNewLine110598067(char* str){\n" +
                    "    char *result;    char *newLine = \"brnl;\";\n" +
                    "    char *ptrResult;char *ptrOriginal;char *nextPtrOriginal;\n" +
                    "    nextPtrOriginal = ptrOriginal = strstr(str, newLine);\n" +
                    "    ptrResult = strstr(str,newLine);\n" +
                    "    while(ptrOriginal!= NULL && nextPtrOriginal!=NULL){\n" +
                    "        nextPtrOriginal  = strstr(ptrOriginal+ strlen(newLine), newLine);\n" +
                    "        ptrOriginal = ptrOriginal + strlen(newLine);*ptrResult = '\\n';\n" +
                    "        while( *ptrResult++!='\\0' && ptrOriginal!=nextPtrOriginal  ){\n" +
                    "            *ptrResult = *ptrOriginal++;};\n" +
                    "    }}\n";

    private String functionOfReadScanf =
            "int read110598067Scanf(const char * format, ... ){\n" +
                    "    va_list args;\n" +
                    "    va_start(args, format);\n" +
                    "    int n = vsscanf(INPUT110598067, format, args);\n" +
                    "    va_end(args);\n" +
                    "    INPUT110598067 = INPUT110598067 + USED110598067;\n" +
                    "    return n;}\n";

    private String functionOfReadGetchar =
            "char read110598067Getchar(){return *INPUT110598067++;}\n";

    private String functionOfReadGets =
            "char* read110598067Gets(char* str,const char* format, ...){\n" +
                    "    char *ptr;\n" +
                    "    if(*INPUT110598067 == '\\n'){\n" +
                    "        strcpy(str,\"\");\n" +
                    "        INPUT110598067++;\n" +
                    "        return str;\n" +
                    "    }else if(*INPUT110598067 == '\\0'){\n" +
                    "        return NULL;\n" +
                    "    }else{\n" +
                    "    va_list args;\n" +
                    "    va_start(args, format);\n" +
                    "    vsscanf(INPUT110598067, format, args);\n" +
                    "    va_end(args);\n" +
                    "    INPUT110598067 = INPUT110598067 + USED110598067;\n" +
                    "    return str;}\n" +
                    "}\n";

    private String functionOfReadFgets =
            "char* read110598067Fgets(char* str,int size){\n" +
                    "    char *format =  (char*)malloc(20 * sizeof(char));\n" +
                    "    char* temp;int i;size--;\n" +
                    "    if(*INPUT110598067 == '\\0')return NULL;\n" +
                    "    size_t npos = strcspn(INPUT110598067, \"\\n\");\n" +
                    "    sprintf(format,\"%%%d[^\\\\n]%%n\",size);\n" +
                    "    if(npos<size){\n" +
                    "        temp =  (char*)malloc((npos+1) * sizeof(char));\n" +
                    "        for(i=0;i<=npos;i++)temp[i] = INPUT110598067[i];\n" +
                    "        temp[npos+1] ='\\0';\n" +
                    "        sscanf(temp,format,str,&USED110598067);\n" +
                    "        INPUT110598067 = INPUT110598067 + npos +1;\n" +
                    "        free(temp);\n" +
                    "    }else{\n" +
                    "        sscanf(INPUT110598067,format,str,&USED110598067);\n" +
                    "        INPUT110598067 = INPUT110598067 + USED110598067;\n" +
                    "    }\n" +
                    "    free(format);\n" +
                    "    return str;\n" +
                    "}\n";
	
	public CprogramConverterUseArgvAsInput(StudentProgramDTO studentProgramDTO) {
		this.studentProgramDTO = studentProgramDTO;
		this.programContent = studentProgramDTO.getcProgramContent();
        this.stackOfLineChanges = new ArrayDeque<InsertedLines>();
		
	}

	public CprogramExpertUsedArgvAsInput generateCprogramExpertUsedArgvAsInput() {
		adaptedProgram = new CprogramExpertUsedArgvAsInput();
		adaptedProgram.setcFileName(this.studentProgramDTO.getcFileName());
		adaptedProgram.setQuetionID(this.studentProgramDTO.getQuetionID());
		adaptedProgram.setStudentID(this.studentProgramDTO.getStudentID());
		adaptedProgram.setInputData(this.convertInputDataInOneLine(this.studentProgramDTO.getInputData()));
		adaptedProgram.setcProgramContentUsedArgvAsInput(this.programContent);
		return adaptedProgram;
	}

	private String convertInputDataInOneLine(String inputData) {
		return inputData.replaceAll(" ","nbsp;").replaceAll("\n","brnl;");
	}

	public void convert(){
		this.removeComments();
        insertHeader("#include <string.h>\n");
        insertHeader("#include <stdarg.h>\n");
        insertVariableForSscanf("int USED110598067;\n");
        insertVariableForSscanf("char* INPUT110598067;\n");
        int beginningOfMain = findBeginningOfMain();
        int lineNumber = countLineNumberInProgram( beginningOfMain);
        int indexToInsert = beginningOfMain;
        if(hasAnyStatementBeforeMainFunction(beginningOfMain)){
            lineNumber++;
            this.insertNewLineAndRecordInStack(indexToInsert,lineNumber);
            indexToInsert = findBeginningOfMain();
        }
        this.insertStatementsAndRecordInStack(this.functionOfConvertSpace,indexToInsert,lineNumber);
        this.insertStatementsAndRecordInStack(this.functionOfConvertNewLine,indexToInsert,lineNumber);
        this.insertStatementsAndRecordInStack(this.functionOfReadScanf,indexToInsert,lineNumber);
        this.insertStatementsAndRecordInStack(this.functionOfReadGetchar,indexToInsert,lineNumber);
        this.insertStatementsAndRecordInStack(this.functionOfReadGets,indexToInsert,lineNumber);
        this.insertStatementsAndRecordInStack(this.functionOfReadFgets,indexToInsert,lineNumber);
        this.convertMain();


        int indexOfLeftCurlyBracketOfMain = findLeftCurlyBracketOfMain();
        this.insertStatementsForInputProcessing(indexOfLeftCurlyBracketOfMain);

        this.convertInputFunction();

    }
    public List<Integer> convertChangedLineNumbersToOriginalLineNumbers(List<Integer> list) {
        List<Integer> result = list;
        while (!this.stackOfLineChanges.isEmpty()){
            InsertedLines insertedLines = this.stackOfLineChanges.pop();
            int lineNumberWhereInserted = insertedLines.getLineNumbersWhereInserted();
            int numbersOfLine = insertedLines.getNumbersOfLinesInserted();
            result = result.stream().filter(x -> ! (x>=lineNumberWhereInserted && x< lineNumberWhereInserted +numbersOfLine))
                    .map( x -> (x>=lineNumberWhereInserted +numbersOfLine)?x-numbersOfLine:x).collect(Collectors.toList());
        }
        return result;
    }
	
    private void insertStatementsForInputProcessing(int indexOfLeftCurlyBracketOfMain) {
        String str = this.programContent.substring(indexOfLeftCurlyBracketOfMain + 1);
        String strAfterLeftCurlyBracketOfMain = str.substring(0,str.indexOf('\n'));
        boolean statementExistInMainInTheSameLine = checkIfAnyStatementExists( strAfterLeftCurlyBracketOfMain);

        int indexToInsert = indexOfLeftCurlyBracketOfMain + 1;
        int lineNumber = countLineNumberInProgram(indexOfLeftCurlyBracketOfMain);

        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        if( statementExistInMainInTheSameLine){
            stringBuilder.insert(indexToInsert,"\nINPUT110598067=argv[1];convertNewLine110598067(INPUT110598067);convertSpace110598067(INPUT110598067);\n");
            this.recordInsertedNewLinesInStack(lineNumber+1,2);
        }else {
            stringBuilder.insert(indexToInsert,"\nINPUT110598067=argv[1];convertNewLine110598067(INPUT110598067);convertSpace110598067(INPUT110598067);");
            this.recordInsertedNewLinesInStack(lineNumber+1,1);
        }
        this.programContent = stringBuilder.toString();
    }
	
	private void convertInputFunction() {
        this.convertScanf();
        this.convertGetchar();
        this.convertGets();
        this.convertFgets();
        this.convertFgetc();
        this.convertGetc();
    }
	
    
    private void convertScanf() {
        List<Integer> indexes = this.getStartIndexesOfScanfFunction();
        Collections.reverse(indexes);
        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        for(int index:indexes){
            this.insertIntegerVariableForINPUTString(stringBuilder,index);
            this.convertStringFormantForScanf(stringBuilder,index);
            stringBuilder.replace(index,index+1,"read110598067S");
        }
        this.programContent = stringBuilder.toString();
    }
    
    private void insertIntegerVariableForINPUTString(StringBuilder stringBuilder,int index) {
        while (stringBuilder.charAt(index)!=')')index++;
        stringBuilder.insert(index,",&USED110598067");
    }
    private void convertStringFormantForScanf(StringBuilder stringBuilder, int index) {
        int countDoubleQuotes = 0;
        while (countDoubleQuotes<2){
            if(stringBuilder.charAt(index) == '\"')countDoubleQuotes++;
            if(countDoubleQuotes<2)index++;
        }
        int indexOfRightDoubleQuote = index;
        stringBuilder.insert(indexOfRightDoubleQuote,"%n");
    }
    
    private List<Integer> getStartIndexesOfScanfFunction(){
        List<Integer> result = getStartIndexes(this.programContent,"(?<!\\w)scanf[\\s]*\\(");
        result = result.stream().filter( x -> !isInsideStringDoubleQuotes(x)).collect(Collectors.toList());
        return result;
    }
    private void convertGetchar() {
        List<Integer> indexes = this.getStartIndexesOfGetcharFunction();
        Collections.reverse(indexes);
        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        for(int index:indexes){
            stringBuilder.replace(index,index+1,"read110598067G");
        }
        this.programContent = stringBuilder.toString();
    }
    
    private void convertGets() {
        List<Integer> indexes = this.getStartIndexesOfGetsFunction();
        Collections.reverse(indexes);
        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        for(int index:indexes){
            this.convertArgumentForGets(stringBuilder,index);
            stringBuilder.replace(index,index+1,"read110598067G");
        }
        this.programContent = stringBuilder.toString();
    }
    private void convertFgets() {
        List<Integer> indexes = this.getStartIndexesOfFgetsFunction();
        Collections.reverse(indexes);
        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        for(int index:indexes){
            this.convertArgumentForfGets(stringBuilder,index);
            stringBuilder.replace(index,index+1,"read110598067F");
        }
        this.programContent = stringBuilder.toString();
    }
    private void convertFgetc() {
        List<Integer> indexes = this.getStartIndexesOfFgetcFunction();
        Collections.reverse(indexes);
        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        for(int index:indexes){
            this.removeStdin(stringBuilder,index);
            stringBuilder.replace(index,index+5,"read110598067Getchar");
        }
        this.programContent = stringBuilder.toString();
    }
    private void convertGetc() {
        List<Integer> indexes = this.getStartIndexesOfGetcFunction();
        Collections.reverse(indexes);
        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        for(int index:indexes){
            this.removeStdin(stringBuilder,index);
            stringBuilder.replace(index,index+4,"read110598067Getchar");
        }
        this.programContent = stringBuilder.toString();
    }
    private void removeStdin(StringBuilder stringBuilder, int indexOfFgetc) {
        int rightBracketOfFget = findRightBracketOfFget(indexOfFgetc);
        String subStr = stringBuilder.substring(indexOfFgetc,rightBracketOfFget+1);
        int indexOfStdin = this.getStartIndex(subStr,"(?<!\\w)stdin(?=([\\s]*\\)))")+indexOfFgetc;
        if(indexOfStdin !=-1){
            stringBuilder.replace(indexOfStdin,indexOfStdin+5,"");
        }
    }
    private int getStartIndex(String statement, String regex){

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(statement);
        int startIndex = -1;
        if(matcher.find()){
            startIndex =  matcher.start();
        }else {
            startIndex = -1;
        }
        return startIndex;
    }
    private List<Integer> getStartIndexesOfFgetcFunction(){
        List<Integer> result = getStartIndexes(this.programContent,"(?<!\\w)fgetc[\\s]*\\(");
        result = result.stream().filter( x -> !isInsideStringDoubleQuotes(x)).filter(startIndex -> {
            int rightBracket = findRightBracketOfFget(startIndex);
            String fget = this.programContent.substring(startIndex,rightBracket+1);
            return this.hasMatch(fget,"(?<!\\w)stdin(?=([\\s]*\\)))");
        }).collect(Collectors.toList());
        return result;
    }
    private boolean hasMatch(String str,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
    private List<Integer> getStartIndexesOfGetcFunction() {
        List<Integer> result = getStartIndexes(this.programContent,"(?<!\\w)getc[\\s]*\\(");
        result = result.stream().filter( x -> !isInsideStringDoubleQuotes(x)).filter(startIndex -> {
            int rightBracket = findRightBracketOfFget(startIndex);
            String fget = this.programContent.substring(startIndex,rightBracket+1);
            return this.hasMatch(fget,"(?<!\\w)stdin(?=([\\s]*\\)))");
        }).collect(Collectors.toList());
        return result;
    }
    private List<Integer> getStartIndexesOfFgetsFunction(){
        List<Integer> result = getStartIndexes(this.programContent,"(?<!\\w)fgets[\\s]*\\(");
        result = result.stream().filter( x -> !isInsideStringDoubleQuotes(x)).filter(startIndex -> {
            int rightBracket = findRightBracketOfFget(startIndex);
            String fget = this.programContent.substring(startIndex,rightBracket+1);
            return this.hasMatch(fget,"(?<!\\w)stdin(?=([\\s]*\\)))");
        }).collect(Collectors.toList());
        return result;
    }
    private void convertArgumentForfGets(StringBuilder stringBuilder, int indexOfFgets) {
        int rightBracketOfFget = findRightBracketOfFget(indexOfFgets);
        String subStr = stringBuilder.substring(indexOfFgets,rightBracketOfFget+1);
        int indexOfStdin = this.getStartIndex(subStr,"(?<!\\w)stdin(?=([\\s]*\\)))")+indexOfFgets;
        if(indexOfStdin !=-1){
            stringBuilder.replace(indexOfStdin,indexOfStdin+5,"");
            int indexOfComma = indexOfStdin-1;
            while(stringBuilder.charAt(indexOfComma)!=','){
                indexOfComma--;
            }
            stringBuilder.replace(indexOfComma,indexOfComma+1,"");
        }
    }
    private void convertArgumentForGets(StringBuilder stringBuilder,int index) {
        boolean isFind = false;
        int brackets = 0;
        int left = -1;
        int right = 0;
        while (!isFind){
            if(stringBuilder.charAt(index)=='('){
                brackets++;
                if(left == -1)left = index;
            }else if(stringBuilder.charAt(index)==')'){
                brackets--;
                if(brackets==0){
                    right =index;
                    isFind = true;
                }
            }
            index++;
        }
        String argument = stringBuilder.substring(left+1,right).trim();
        String insertedStr = ",\"%[^\\n]\\n%n\","+argument+",&USED110598067";
        stringBuilder.insert(right,insertedStr);
    }
    private int findRightBracketOfFget(int indexOfFgets) {
        boolean isFind = false;
        int brackets = 0;
        while (!isFind){
            if(this.programContent.charAt(indexOfFgets)=='('){
                brackets++;
            }else if(this.programContent.charAt(indexOfFgets)==')'){
                brackets--;
                if(brackets==0)isFind =true;
            }
            if(!isFind)indexOfFgets++;
        }
        int rightBracketOfFget = indexOfFgets;
        return rightBracketOfFget;
    }
    private List<Integer> getStartIndexesOfGetsFunction(){
        List<Integer> result = getStartIndexes(this.programContent,"(?<!\\w)gets[\\s]*\\(");
        result = result.stream().filter( x -> !isInsideStringDoubleQuotes(x)).collect(Collectors.toList());
        return result;
    }

    private List<Integer> getStartIndexesOfGetcharFunction(){
        List<Integer> result = getStartIndexes(this.programContent,"(?<!\\w)getchar[\\s]*\\(");
        result = result.stream().filter( x -> !isInsideStringDoubleQuotes(x)).collect(Collectors.toList());
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
    
    private void convertMain() {
        int indexOfLeftBracketOfMain = findLeftBracketOfMain();
        int indexOfRightBracketOfMain = findRightBracketOfMain(indexOfLeftBracketOfMain);
        int numberOfNewLines = countHowManyNewlineCharacterInBrackets(indexOfLeftBracketOfMain,indexOfRightBracketOfMain);
        StringBuilder cProgramBuilder = new StringBuilder(this.programContent);
        cProgramBuilder.replace(indexOfLeftBracketOfMain+1,indexOfRightBracketOfMain,"int argc, char *argv[]");
        for(int i =0;i<numberOfNewLines;i++){
            cProgramBuilder.insert(indexOfLeftBracketOfMain+1,'\n');
        }
        this.programContent = cProgramBuilder.toString();
    }
    
    private int findLeftBracketOfMain() {
        Pattern pattern = Pattern.compile("(?<!\\w)(?:void|int)[\\s]*(?<!\\w)main[\\s]*\\(");
        Matcher matcher = pattern.matcher(this.programContent);
        int indexOfLeftBracketOfMain = -1;
        while(matcher.find()){
            indexOfLeftBracketOfMain = matcher.end() -1;
            if(!isInsideStringDoubleQuotes(indexOfLeftBracketOfMain))break;
        }
        return indexOfLeftBracketOfMain;
    }
    
    private int findRightBracketOfMain(int indexOfLeftBracketOfMain) {
        int indexOfRightBracketOfMain = indexOfLeftBracketOfMain + 1;
        while(this.programContent.charAt(indexOfRightBracketOfMain) != ')'){
            indexOfRightBracketOfMain++;
        }
        return indexOfRightBracketOfMain;
    }
    
    private int countHowManyNewlineCharacterInBrackets(int indexOfLeftBracketOfMain, int indexOfRightBracketOfMain) {
        String stringInBrackets = this.programContent.substring(indexOfLeftBracketOfMain,indexOfRightBracketOfMain);
        int numberOfNewLines = countMatches(stringInBrackets,"\n");
        return numberOfNewLines;
    }
    
    private int findLeftCurlyBracketOfMain() {
        int indexOfLeftBracketOfMain = this.findLeftBracketOfMain();
        int indexOfLeftCurlyBracketsOfMain = indexOfLeftBracketOfMain;
        while(this.programContent.charAt(indexOfLeftCurlyBracketsOfMain) != '{'){
            indexOfLeftCurlyBracketsOfMain++;
        }
        return indexOfLeftCurlyBracketsOfMain;
    }

    private void insertHeader(String headerStatement) {
        insertAtBeginningOfProgram(headerStatement);
    }
    
    private void insertVariableForSscanf(String statement) {
        insertAtBeginningOfProgram(statement);
    }
    
    public int countLineNumberInProgram(int indexInProgram) {
        String str = this.programContent.substring(0,indexInProgram);
        int lineNumber = this.countNewLines(str) + 1;
        return lineNumber;
    }
    
    private boolean hasAnyStatementBeforeMainFunction(int beginningOfMain) {
        String stringBeforeMain = this.programContent.substring(0,beginningOfMain);
        int index = stringBeforeMain.lastIndexOf("\n");
        if(index == -1) index =0;
        return checkIfAnyStatementExists(stringBeforeMain.substring(index));
    }
    
    private boolean checkIfAnyStatementExists(String string) {
        return  !string.trim().isEmpty();
    }
    
    private int findBeginningOfMain() {
        Pattern pattern = Pattern.compile("(?<!\\w)(?:void|int)[\\s]*(?<!\\w)main[\\s]*\\(");
        Matcher matcher = pattern.matcher(this.programContent);
        int beginningOfMain = 0;
        while(matcher.find()){
            beginningOfMain = matcher.start();
            if(!isInsideStringDoubleQuotes(beginningOfMain)){
                break;
            }
        }
        return beginningOfMain;
    }
    
    private void insertNewLineAndRecordInStack(int indexToInsert, int lineNumber) {
        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        stringBuilder.insert(indexToInsert,"\n");
        this.programContent = stringBuilder.toString();
        this.recordInsertedNewLinesInStack(lineNumber,1);
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
    
    private void insertAtBeginningOfProgram(String statement){
        this.insertStatementsAndRecordInStack(statement,0,1);
    }
    
    private void insertStatementsAndRecordInStack(String statements, int indexInProgramContent, int lineNumber){
        StringBuilder cProgramBuilder = new StringBuilder(this.programContent);
        String arr[] = statements.split("\n");
        List<String> list = Arrays.asList(arr);
        Collections.reverse(list);
        for(String str:list){
            cProgramBuilder.insert(indexInProgramContent , str + "\n");
        }
        int numbersOfLinesInserted = countNewLines(statements);
        this.recordInsertedNewLinesInStack(lineNumber,numbersOfLinesInserted);
        this.programContent = cProgramBuilder.toString();
    }
    
    private int countNewLines(String str){
        return countMatches(str, "\n");
    }
    
    private void recordInsertedNewLinesInStack(int lineNumber, int numbersOfLinesInserted) {
        InsertedLines insertedLines = new InsertedLines();
        insertedLines.setLineNumbersWhereInserted(lineNumber);
        insertedLines.setNumbersOfLinesInserted(numbersOfLinesInserted);
        this.stackOfLineChanges.push(insertedLines);
    }
    
    private int countMatches(String str,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

	private void removeComments(){
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
        this.programContent = result;
    }
	
	

}
