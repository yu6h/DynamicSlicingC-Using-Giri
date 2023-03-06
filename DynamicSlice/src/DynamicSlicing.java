import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public class DynamicSlicing {
	
	private String workDirectory;
	
	private String studentID;
	
	private String questiotnID;
	
	private String cFileName;
	
	private int lineNumberOfTargetStatement;
	
	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	private String inputData;
	
	private String workDirectoryInContainer;

	public String getWorkDirectory() {
		return workDirectory;
	}

	public void setWorkDirectory(String workDirectory) {
		this.workDirectory = workDirectory;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getQuestiotnID() {
		return questiotnID;
	}

	public void setQuestiotnID(String questiotnID) {
		this.questiotnID = questiotnID;
	}

	public String getcFileName() {
		return cFileName;
	}

	public void setcFileName(String cFileName) {
		this.cFileName = cFileName;
	}

	public String getWorkDirectoryInContainer() {
		return workDirectoryInContainer;
	}

	public void setWorkDirectoryInContainer(String workDirectoryInContainer) {
		this.workDirectoryInContainer = workDirectoryInContainer;
	}
	
	private String getInputDataInOneLine() {
	    String str = this.inputData.replace("\r\n", "\n"); // convert windows line endings to linux format 
	    str = str.replace("\r", "\n"); // convert (remaining) mac line endings to linux format
	    return str.replace("\n", " "); // count total line endings
	}
	
	private String getcFileNameWithoutExtension() {
		String cFileNameWithoutExtension;
		if(cFileName.contains(".")) {
			cFileNameWithoutExtension =  cFileName.substring(0, cFileName.indexOf("."));
		}else {
			cFileNameWithoutExtension = cFileName;
		}
		return cFileNameWithoutExtension;
	}
	
	private void createMakeFile() {
		File myObj = new File(this.workDirectory+"Makefile");
		try {
			if (myObj.createNewFile()) {
			    System.out.println("File created: " + myObj.getName());
			  } else {
			    System.out.println("Makefile File already exists.");
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeMakeFile() {
		 try {
			FileWriter myWriter = new FileWriter(this.workDirectory+"Makefile");
			myWriter.write("NAME = "+ this.getcFileNameWithoutExtension() +"\n" + 
					"LDFLAGS = -lm\n" + 
					"INPUT ?= "+ this.getInputDataInOneLine() +"\n" + 
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
	
	private boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	private void createLocTxtFile() {
		File myObj = new File(this.workDirectory+"loc.txt");
		try {
			if (myObj.createNewFile()) {
			    System.out.println("File created: " + myObj.getName());
			  } else {
			    System.out.println("loc.txt File already exists.");
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execute(){

		String containerName = this.studentID+"_"+this.questiotnID;
		Giri giri = new GiriImpl();
		giri.createContainer(containerName);
		deleteDirectory(new File(this.workDirectory));
		createDirectory(new File(this.workDirectory));
		createMakeFile();
		writeMakeFile();
		createLocTxtFile();
		writeLocTxtFile();
		giri.copyMakeFileIntoContainer(this.workDirectory, this.workDirectoryInContainer);
		giri.copyLotTxtFileIntoContainer( this.workDirectory, this.workDirectoryInContainer);
		giri.make(this.workDirectoryInContainer);
		giri.downloadSlicLocFileIntoWorkDirectory(this.getcFileNameWithoutExtension() , this.workDirectoryInContainer, this.workDirectory);
		DynamicSliceResultDTO result = generateDynamicSliceResult();
	}

	private DynamicSliceResultDTO generateDynamicSliceResult() {
		// TODO Auto-generated method stub
		return null;
	}

	private void writeLocTxtFile() {
		try {
			FileWriter myWriter = new FileWriter(this.workDirectory+"Makefile");
			myWriter.write(this.cFileName + " " + this.lineNumberOfTargetStatement);
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createDirectory(File file) {
		file.mkdirs();
	}



	
	

}
