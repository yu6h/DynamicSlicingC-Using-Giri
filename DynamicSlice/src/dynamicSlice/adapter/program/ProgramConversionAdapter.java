package dynamicSlice.adapter.program;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dynamicSlice.adapter.program.DTO.StudentProgramDTO;
import dynamicSlice.adapter.commandTool.factory.CommandToolFactoryUbuntuImpl;
import dynamicSlice.adapter.fileHandler.FileUtil;
import dynamicSlice.adapter.service.CCoverageService;
import dynamicSlice.adapter.service.CCoverageServiceBuilder;
import dynamicSlice.usecase.in.DynamicSliceUseCaseInput;

public class ProgramConversionAdapter {

    DynamicSliceUseCaseInput useCaseInput;
	
	StudentProgramDTO studentProgramDTO;
	
	private String programContent;
	
	private ArrayDeque<InsertedLines> stackOfLineChanges;

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
    
    private String functionOfConvertSpecialCharacter =
            "void convertSpecialCharacter110598067(char *str,char *old,char new){\n" +
                    "    char *result;    \n" +
                    "    char *ptrResult;char *ptrOriginal;char *nextPtrOriginal;\n" +
                    "    nextPtrOriginal = ptrOriginal = strstr(str, old);\n" +
                    "    ptrResult = strstr(str,old);\n" +
                    "    while(ptrOriginal!= NULL && nextPtrOriginal!=NULL){\n" +
                    "        nextPtrOriginal  = strstr(ptrOriginal+ strlen(old), old);\n" +
                    "        ptrOriginal = ptrOriginal + strlen(old);\n" +
                    "        *ptrResult = new;\n" +
                    "        while( *ptrResult++!='\\0' && ptrOriginal!=nextPtrOriginal  ){\n" +
                    "            *ptrResult = *ptrOriginal++;\n" +
                    "        };\n" +
                    "    }\n" +
                    "}\n";
    
    private String functionOfConvertInputData =
            "void convertInputData110598067(char *inputData){\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*BrnL31\",'\\n');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*NbsP13\",' ');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*AsD38\",'&');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*LtS60\",'<');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*GtS62\",'>');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*QuT878\",'\\\"');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*SqT877\",'\\'');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*LpT203\",'(');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*RpT301\",')');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*EpT33\",'!');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*PdS410\",'#');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*DaS717\",'$');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*AcA180\",'`');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*HaF969\",'|');\n" + 
            "    convertSpecialCharacter110598067(inputData,\"*BaS403\",'\\\\');\n" + 
            "}\n";
	
	public ProgramConversionAdapter(StudentProgramDTO studentProgramDTO) {
		this.studentProgramDTO = studentProgramDTO;
		this.programContent = studentProgramDTO.getcProgramContent();
		this.removeComments();
        this.stackOfLineChanges = new ArrayDeque<InsertedLines>();
        useCaseInput = new DynamicSliceUseCaseInput();
	}
	
    private List<Integer> findLineNumbersOfOutputStatement() {
    	CCoverageService cCoverageServiceservice= CCoverageServiceBuilder.newInstance()
                .buildStudentID(this.useCaseInput.getStudentID())
    	.buildQuetionID(this.useCaseInput.getQuetionID())
    	.buildCFileName(this.useCaseInput.getcFileName())
    	.buildCFileNameWithOutExtension(this.generateCFileNameWithoutExtension(this.useCaseInput.getcFileName()))
    	.buildInputDataAtCMD(this.useCaseInput.getInputData())
    	.buildProgramContent(this.useCaseInput.getProgramContent())
    	.buildFileHandlerTool(new FileUtil())
    	.buildCommandToolFactory(new CommandToolFactoryUbuntuImpl())
    	.build();
    	cCoverageServiceservice.analyze();
    	List<Integer> lineNumbersOfUncoveredStatement = cCoverageServiceservice.getLineNumbersOfUncoveredStatement();
        List<Integer> startIndexes = getStartIndexes(this.programContent,"(?<!\\w)(?:printf|puts|putc|putchar|fputs)[\\s]*\\(");
        List<Integer> lineNumbersOfCoveredOutputStatement = startIndexes.stream()
        		.filter(x-> !isInsideStringDoubleQuotes(x))
        		.map(x->countLineNumberInProgram(x))
        		//不會被這次測資cover到的output statement  不需要給giri做使用 所以在這裡過濾掉
        		.filter(lineNumber -> !lineNumbersOfUncoveredStatement.contains(lineNumber))
        		.collect(Collectors.toList());
        return lineNumbersOfCoveredOutputStatement;
    }

	private String generateCFileNameWithoutExtension(String cFileName) {
		String cFileNameWithoutExtension = cFileName.substring(0, cFileName.indexOf("."));
		return cFileNameWithoutExtension;
	}

	private String convertInputDataInOneLine(String inputData) {
        return inputData.replace(" ","*NbsP13")
                .replace("\n","*BrnL31")
                .replace("&","*AsD38")
                .replace("<","*LtS60")
                .replace(">","*GtS62")
                .replace("\"","*QuT878")
                .replace("\'","*SqT877")
                .replace("(","*LpT203")
                .replace(")","*RpT301")
                .replace("!","*EpT33")
                .replace("#","*PdS410")
                .replace("$","*DaS717")
                .replace("`","*AcA180")
                .replace("|","*HaF969")
                .replace("\\","*BaS403");
    }
    public DynamicSliceUseCaseInput getDynamicSliceUseCaseInput(){
        return this.useCaseInput;
    }
	public void execute(){
        this.insertFunctionAtBeginning(this.functionOfConvertInputData);
        this.insertFunctionAtBeginning(this.functionOfConvertSpecialCharacter);
        this.insertFunctionAtBeginning(this.functionOfReadScanf);
        this.insertFunctionAtBeginning(this.functionOfReadGetchar);
        this.insertFunctionAtBeginning(this.functionOfReadGets);
        this.insertFunctionAtBeginning(this.functionOfReadFgets);
        this.insertVariableForSscanfAtBeginging("int USED110598067;\n");
        this.insertVariableForSscanfAtBeginging("char* INPUT110598067;\n");
        this.insertHeaderAtBeginning("#include <stdlib.h>\n");
        this.insertHeaderAtBeginning("#include <stdio.h>\n");
        this.insertHeaderAtBeginning("#include <string.h>\n");
        this.insertHeaderAtBeginning("#include <stdarg.h>\n");

        this.convertMain();


        int indexOfLeftCurlyBracketOfMain = findLeftCurlyBracketOfMain();
        this.insertStatementsForInputProcessing(indexOfLeftCurlyBracketOfMain);

        this.convertInputFunction();

        useCaseInput.setcFileName(this.studentProgramDTO.getcFileName());
        useCaseInput.setcFileNameWithoutExtension(generateCFileNameWithoutExtension(this.studentProgramDTO.getcFileName()));
        useCaseInput.setQuetionID(this.studentProgramDTO.getQuetionID());
        useCaseInput.setStudentID(this.studentProgramDTO.getStudentID());
        useCaseInput.setInputData(this.convertInputDataInOneLine(this.studentProgramDTO.getInputData()));
        useCaseInput.setProgramContent(this.programContent);
        useCaseInput.setLineNumbersOfOutputStatement(this.findLineNumbersOfOutputStatement());
    }
    public List<Integer> convertChangedLineNumbersToOriginalLineNumbers(List<Integer> list) {
        List<Integer> result = list;
        ArrayDeque<InsertedLines> copyOfStack = this.stackOfLineChanges.clone();
        while (!copyOfStack.isEmpty()){
            InsertedLines insertedLines = copyOfStack.pop();
            int lineNumberWhereInserted = insertedLines.getLineNumbersWhereInserted();
            int numbersOfLine = insertedLines.getNumbersOfLinesInserted();
            result = result.stream().filter(x -> ! (x>=lineNumberWhereInserted && x< lineNumberWhereInserted +numbersOfLine))
                    .map( x -> (x>=lineNumberWhereInserted +numbersOfLine)?x-numbersOfLine:x).collect(Collectors.toList());
        }
        return result;
    }
    private void insertFunctionAtBeginning(String function){
        this.insertAtBeginningOfProgram(function);
    }
    private void insertStatementsForInputProcessing(int indexOfLeftCurlyBracketOfMain) {
        String str = this.programContent.substring(indexOfLeftCurlyBracketOfMain + 1);
        String strAfterLeftCurlyBracketOfMain = str.substring(0,str.indexOf('\n'));
        boolean statementExistInMainInTheSameLine = checkIfAnyStatementExists( strAfterLeftCurlyBracketOfMain);

        int indexToInsert = indexOfLeftCurlyBracketOfMain + 1;
        int lineNumber = countLineNumberInProgram(indexOfLeftCurlyBracketOfMain);

        StringBuilder stringBuilder = new StringBuilder(this.programContent);
        if( statementExistInMainInTheSameLine){
            stringBuilder.insert(indexToInsert,"\nINPUT110598067=argv[1];convertInputData110598067(INPUT110598067);\n");
            this.recordInsertedNewLinesInStack(lineNumber+1,2);
        }else {
            stringBuilder.insert(indexToInsert,"\nINPUT110598067=argv[1];convertInputData110598067(INPUT110598067);");
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

    private void insertHeaderAtBeginning(String headerStatement) {
        insertAtBeginningOfProgram(headerStatement);
    }
    
    private void insertVariableForSscanfAtBeginging(String statement) {
        insertAtBeginningOfProgram(statement);
    }
    
    public int countLineNumberInProgram(int indexInProgram) {
        String str = this.programContent.substring(0,indexInProgram);
        int lineNumber = this.countNewLines(str) + 1;
        return lineNumber;
    }
    

    
    private boolean checkIfAnyStatementExists(String string) {
        return  !string.trim().isEmpty();
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


    private static class InsertedLines {
        private int lineNumbersWhereInserted;
        private int numbersOfLinesInserted;

        public int getLineNumbersWhereInserted() {
            return lineNumbersWhereInserted;
        }

        public void setLineNumbersWhereInserted(int lineNumbersWhereInserted) {
            this.lineNumbersWhereInserted = lineNumbersWhereInserted;
        }

        public int getNumbersOfLinesInserted() {
            return numbersOfLinesInserted;
        }

        public void setNumbersOfLinesInserted(int numbersOfLinesInserted) {
            this.numbersOfLinesInserted = numbersOfLinesInserted;
        }

    }
}
