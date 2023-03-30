package dynamicSlice.adapter.fileHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dynamicSlice.usecase.out.FileRepository;

public class FileRepositoryImpl implements FileRepository{
	
	public boolean deleteWorkDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteWorkDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	public void createWorkDirectory(File file) {
		file.mkdirs();
	}
	
	public void writePreprocessedCprogramFile(String workDirectory, String cFileName, String preprocessedprogramContent) {
		try {
		      FileWriter myWriter = new FileWriter(workDirectory+cFileName);
		      myWriter.write(preprocessedprogramContent);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
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

	public void createFile(String workDirectory, String fileName) {
		File myObj = new File(workDirectory+fileName);
		try {
			if (myObj.createNewFile()) {
			    System.out.println("File created: " + myObj.getName());
			  } else {
			    System.out.println(fileName + " File already exists.");
			  }
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
	
}
