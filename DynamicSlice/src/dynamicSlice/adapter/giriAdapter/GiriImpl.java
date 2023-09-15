package dynamicSlice.adapter.giriAdapter;


import java.io.IOException;


import dynamicSlice.usecase.out.Giri;
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
			}  finally {
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//在測資輸入長度較長的情況下 會有.slice.loc檔案等了很久還是抓不下來的情況(如果直接手動進container 下make指令還是可以成功)
	//如果有這種情況 就下第二次make指令就可以成功得到.slice.loc檔案(裡面是動態切片結果的行號)
	public void makeAndDownloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory
			,String cFileNameWithoutExtension,FileRepository fileHandler) {
		this.doMakeCommandWithTimeLimit(workDirectoryInContainer, 10000);
		this.downloadSlicLocFileIntoWorkDirectory(workDirectoryInContainer, workDirectory, cFileNameWithoutExtension);
		boolean fileExist = fileHandler.checkIfSliceLocFileExist(workDirectory, cFileNameWithoutExtension);
		if(!fileExist) {
			this.doMakeCommandWithTimeLimit(workDirectoryInContainer, 10000);
			this.downloadSlicLocFileIntoWorkDirectory(workDirectoryInContainer, workDirectory, cFileNameWithoutExtension);
			fileExist = fileHandler.checkIfSliceLocFileExist(workDirectory, cFileNameWithoutExtension);
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
			} finally {
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
			}  finally {
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
