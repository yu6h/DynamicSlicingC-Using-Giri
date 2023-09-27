package dynamicSlice.adapter.giriAdapter;


import java.io.File;
import java.io.IOException;
import java.util.List;

import dynamicSlice.adapter.fileHandler.GiriFileHandler;
import dynamicSlice.usecase.service.DynamicSliceToolAdapter;

import java.util.ArrayList;
public class GiriAdapter implements DynamicSliceToolAdapter{
	
	private String containerName;
	private String studentID;
	private int questionID;
	private String workDirectory;
	private String workDirectoryInContainer;
	private GiriFileHandler giriFileHandler;
	private String cProgramFileName;
	private String cFileNameWithoutExtension;
	private List<Integer> lineNumbersOfDynamicSlice;

	
	public GiriAdapter(GiriFileHandler giriFileHandler) {
		this.giriFileHandler = giriFileHandler;
		this.lineNumbersOfDynamicSlice = new ArrayList<Integer>();
		this.containerName = "giriContainer";
	}
	@Override
	public void iniitilizeDirectory(String studentID,int questionID,String cProgramFileName) {
		this.studentID = studentID;
		this.questionID = questionID;
		this.cProgramFileName = cProgramFileName;
		this.cFileNameWithoutExtension = cProgramFileName.substring(0, cProgramFileName.indexOf("."));
		this.workDirectory = "/home/aaron/Desktop/GiriWorkDirectory/"+ this.studentID+"/"
				+ this.questionID + "/";
		this.workDirectoryInContainer = "/giri/test/UnitTests/ST"+this.studentID+"HW"+ this.questionID +"/";
		this.giriFileHandler.deleteWorkDirectory(new File(this.workDirectory));
		this.giriFileHandler.createWorkDirectory(new File(this.workDirectory));
	}
	
	@Override
	public void setInputData(String inputData) {
		// TODO Auto-generated method stub
		this.giriFileHandler.writeMakeFile(this.workDirectory, this.cFileNameWithoutExtension, inputData);
	}

	@Override
	public void setPreprocessedCprogramContent(String preprocessedprogramContent) {
		this.giriFileHandler.writePreprocessedCprogramFile(this.workDirectory, this.cProgramFileName, preprocessedprogramContent);
	}

	@Override
	public void setTargetLineNumber(int lineNumber) {
		this.giriFileHandler.writeLocTxtFile(this.workDirectory, this.cProgramFileName, lineNumber);
	}

	@Override
	public void execute() {
		this.deleteWorkDirectoryInContainer(this.workDirectoryInContainer);
		this.createWorkDirectoryInContainer(this.workDirectoryInContainer);
		this.copyMakeFileIntoContainer(this.workDirectory, this.workDirectoryInContainer);
		this.copyLotTxtFileIntoContainer(this.workDirectory, this.workDirectoryInContainer);
		this.copyCProgramFileIntoContainer(this.workDirectory, workDirectoryInContainer, this.cProgramFileName);
		this.giriFileHandler.deleteSliceLocFile(this.workDirectory, this.cFileNameWithoutExtension);
		this.makeAndDownloadSlicLocFileIntoWorkDirectory(workDirectoryInContainer, workDirectory, cFileNameWithoutExtension);
		this.lineNumbersOfDynamicSlice = this.giriFileHandler.readSliceLocFile(workDirectory, cFileNameWithoutExtension);
	}

	@Override
	public List<Integer> getLineNumbersOfDynamicSlice() {
		return this.lineNumbersOfDynamicSlice;
	}
	
	@Override
	protected void finalize() {
		this.giriFileHandler.deleteWorkDirectory(new File(this.workDirectory));
	}

	private void copyMakeFileIntoContainer(String workDirectory, String workDirectoryInContainer) {
		copyFileIntoContainer(this.containerName, "Makefile" , workDirectory, workDirectoryInContainer);
	}
	
	private void copyLotTxtFileIntoContainer(String workDirectory, String workDirectoryInContainer) {
		copyFileIntoContainer(this.containerName, "loc.txt" , workDirectory, workDirectoryInContainer);
	}

	private void copyCProgramFileIntoContainer(String workDirectory, String workDirectoryInContainer, String cFileName) {
		copyFileIntoContainer(this.containerName, cFileName, workDirectory, workDirectoryInContainer);		
	}
	
	private void copyFileIntoContainer(String containerName , String fileName, String workDirectory, String workDirectoryInContainer) {
		Process process = null;
		String command = String.format("docker cp %s%s %s:%s%s", workDirectory, fileName, containerName, workDirectoryInContainer,fileName);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  finally {
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//在測資輸入長度較長的情況下 會有.slice.loc檔案等了很久還是抓不下來的情況(如果直接手動進container 下make指令還是可以成功)
	//如果有這種情況 就下第二次make指令就可以成功得到.slice.loc檔案(裡面是動態切片結果的行號)
	private void makeAndDownloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory
			,String cFileNameWithoutExtension) {
		this.doMakeCommandWithTimeLimit(workDirectoryInContainer, 10000);
		this.downloadSlicLocFileIntoWorkDirectory(workDirectoryInContainer, workDirectory, cFileNameWithoutExtension);
		boolean fileExist = this.giriFileHandler.checkIfSliceLocFileExist(workDirectory, cFileNameWithoutExtension);
		if(!fileExist) {
			this.doMakeCommandWithTimeLimit(workDirectoryInContainer, 10000);
			this.downloadSlicLocFileIntoWorkDirectory(workDirectoryInContainer, workDirectory, cFileNameWithoutExtension);
			fileExist = this.giriFileHandler.checkIfSliceLocFileExist(workDirectory, cFileNameWithoutExtension);
		}
		if(fileExist)System.out.println("Dynamic Slicing Succeeded!");
		else System.out.println("Dynamic Slicing Failed! ");
	}
	
	private void doMakeCommandWithTimeLimit(String workDirectoryInContainer,int timeLimitByMillisecond) {
		GiriMakeCommandRunner makeRunner = new GiriMakeCommandRunner(workDirectoryInContainer,this.containerName);
		makeRunner.start();
		try {
			makeRunner.join(timeLimitByMillisecond);
			if(makeRunner.isAlive()) {
				makeRunner.interrupt();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
        
	private void downloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory,String cFileNameWithoutExtension) {
		String sliceLocFileName = cFileNameWithoutExtension + ".slice.loc";
		this.downloadFileFromContainer(this.containerName, sliceLocFileName, workDirectoryInContainer, workDirectory);
	}

	private void downloadFileFromContainer(String containerName , String fileName, String workDirectoryInContainer, String  workDirectory) {
		Process process = null;
		String command = String.format("docker cp %s:%s%s %s%s", containerName, workDirectoryInContainer,fileName, workDirectory, fileName);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private void createWorkDirectoryInContainer(String workDirectoryInContainer) {
		Process process = null;
		String command = String.format("docker exec -i %s mkdir %s", containerName, workDirectoryInContainer);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  finally {
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void deleteWorkDirectoryInContainer(String workDirectoryInContainer) {
		Process process = null;
		String command = String.format("docker exec -i %s rm -r %s", containerName, workDirectoryInContainer);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  finally {
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	class GiriMakeCommandRunner extends Thread{
		private String workDirectoryInContainer;
		private String containerName;
		
		public GiriMakeCommandRunner(String workDirectoryInContainer, String containerName) {
			this.workDirectoryInContainer = workDirectoryInContainer;
			this.containerName = containerName;
		}
		
		public void run() {
			Process p =null;
			String commandOfMake = String.format("docker exec -i -w %s %s make", this.workDirectoryInContainer, this.containerName);
			try {
				p = Runtime.getRuntime().exec(commandOfMake);
				p.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
//				System.out.println("process of make is interrupted!");
            }finally {
				p.destroy();
			}
			
		}
	}




}
