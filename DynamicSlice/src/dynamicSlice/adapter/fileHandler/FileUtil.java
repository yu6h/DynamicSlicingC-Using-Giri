package dynamicSlice.adapter.fileHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dynamicSlice.usecase.out.FileRepository;

public class FileUtil implements FileRepository, FileHandlerTool{
	
	public boolean deleteWorkDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteWorkDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	public void createWorkDirectory(File workDirectory) {
		workDirectory.mkdirs();
	}
	
	public void writePreprocessedCprogramFile(String workDirectory, String cFileName, String preprocessedprogramContent) {
		try {
		      FileWriter myWriter = new FileWriter(workDirectory+cFileName);
		      myWriter.write(preprocessedprogramContent);
		      myWriter.close();
		      System.out.println("Successfully wrote to the c file(used argv[] as input).");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	public void writeMakeFile(String workDirectory, String cFileNameWithoutExtension, String InputDataInOneLine) {
		 try {
			FileWriter myWriter = new FileWriter(workDirectory+"Makefile");
			myWriter.write("NAME = "+ cFileNameWithoutExtension +"\n" + 
					"LDFLAGS = -lm\n" + 
					"INPUT ?= "+ InputDataInOneLine +"\n" + 
					"CRITERION ?= -criterion-loc=loc.txt\n" + 
					"#CRITERION ?= -criterion-inst=criterion-inst.txt\n" + 
					"MAPPING ?= -mapping-function=main\n" + 
					"\n" + 
					"include ../../Makefile.common");
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeLocTxtFile(String workDirectory, String cFileName, int lineNumberOfTargetStatement ) {
		try {
			FileWriter myWriter = new FileWriter(workDirectory+"loc.txt");
			myWriter.write(cFileName + " " + lineNumberOfTargetStatement);
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Integer> readSliceLocFile(String workDirectory, String cFileNameWithoutExtension){
		File myObj = new File(workDirectory + cFileNameWithoutExtension + ".slice.loc");
		List<Integer> lineNumbersOfSliceResult = new ArrayList<Integer>();
		try {
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String temp = myReader.nextLine();
		        Integer lineNumber = Integer.valueOf(temp);
		        lineNumbersOfSliceResult.add(lineNumber);
		      }
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lineNumbersOfSliceResult;
	}

	@Override
	public boolean checkIfSliceLocFileExist(String workDirectory, String cFileNameWithoutExtension) {
		return new File(workDirectory + cFileNameWithoutExtension + ".slice.loc").exists();
	}

	@Override
	public String readFile(String workDirectory, String fileName) {
        byte[] encoded;
        String fileContent = "";
		try {
			encoded = Files.readAllBytes(Paths.get(workDirectory  + fileName));
			fileContent =new String(encoded, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return fileContent;
	}

	@Override
	public void writeFile(String workDirectory, String fileName, String content) {
		try {
		      FileWriter myWriter = new FileWriter(workDirectory+fileName);
		      myWriter.write(content);
		      myWriter.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		}
	}
	
}
