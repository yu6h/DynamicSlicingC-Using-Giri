package dynamicSlice.adapter.giriAdapter;

import java.io.File;
import java.io.IOException;


import dynamicSlice.usecase.in.Giri;
import dynamicSlice.usecase.out.FileRepository;

public class GiriImpl implements Giri{
	
	private String containerName;
	
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public void copyMakeFileIntoContainer(String workDirectory, String workDirectoryInContainer) {
		copyFileIntoContainer(this.containerName, "Makefile" , workDirectory, workDirectoryInContainer);
	}
	
	public void copyLotTxtFileIntoContainer(String workDirectory, String workDirectoryInContainer) {
		copyFileIntoContainer(this.containerName, "loc.txt" , workDirectory, workDirectoryInContainer);
	}

	public void copyCProgramFileIntoContainer(String workDirectory, String workDirectoryInContainer, String cFileName) {
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			process.destroy();
		}
		
	}

	public void makeAndDownloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory
			,String cFileNameWithoutExtension,FileRepository fileHandler) {
		this.makeCommandWithTimeLimit(workDirectoryInContainer, 8000);
		this.downloadSlicLocFileIntoWorkDirectory(workDirectoryInContainer, workDirectory, cFileNameWithoutExtension);
		boolean fileExist = fileHandler.checkIfSliceLocFileExist(workDirectory, cFileNameWithoutExtension);
		if(!fileExist) {
			this.makeCommandWithTimeLimit(workDirectoryInContainer, 8000);
		}
		this.downloadSlicLocFileIntoWorkDirectory(workDirectoryInContainer, workDirectory, cFileNameWithoutExtension);
		fileExist = fileHandler.checkIfSliceLocFileExist(workDirectory, cFileNameWithoutExtension);
		if(fileExist)System.out.println("Dynamic Slicing Succeeded!");
		else System.out.println("Dynamic Slicing Failed! ");
	}
	
	private void makeCommandWithTimeLimit(String workDirectoryInContainer,int timeLimitByMillisecond) {
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
        
	public void downloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory,String cFileNameWithoutExtension) {
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			process.destroy();
		}
		
	}
	public static void main(String[] args) {
		GiriImpl giri = new GiriImpl();
		String dir = "/giri/test/UnitTests/ST";
		String filePath = "";
		giri.setContainerName("giriContainer");
		String dir2 = "/giri/test/UnitTests/test1/";
		String file = "extlibcalls.c";
		
//		giri.createWorkDirectoryInContainer(dir);
//		giri.deleteWorkDirectoryInContainer(dir);
		
		boolean fileExists = giri.checkIfFileExists(dir2,"/home/aaron/",file);
		System.out.println(fileExists);
	}
	
	private boolean checkIfFileExists(String workDirectoryInContainer,String workDirectory,String file) {
		this.downloadFileFromContainer(this.containerName, file, workDirectoryInContainer, workDirectory);
		return new File(workDirectory + file ).exists();
	}
	

	public void createWorkDirectoryInContainer(String workDirectoryInContainer) {
		Process process = null;
		String command = String.format("docker exec -i %s mkdir %s", containerName, workDirectoryInContainer);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			process.destroy();
		}
	}

	@Override
	public void deleteWorkDirectoryInContainer(String workDirectoryInContainer) {
		Process process = null;
		String command = String.format("docker exec -i %s rm -r %s", containerName, workDirectoryInContainer);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			process.destroy();
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
				System.out.println("process of make is interrupted!");
            }finally {
				p.destroy();
			}
			
		}
	}
}
