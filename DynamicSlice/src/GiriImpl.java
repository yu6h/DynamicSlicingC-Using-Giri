import java.io.IOException;

public class GiriImpl implements Giri{
	
	private String containerName;

	public void createContainer(String containerName) {
		this.containerName = containerName;
		String createGiriContainerCommand = String.format("sudo docker run --name %s -d -i liuml07/giri /bin/bash", containerName);
		Process process = null;
		try {
			 process = Runtime.getRuntime().exec(createGiriContainerCommand);
			 waitUntilThisProcessForCreatingContainerIsFinished(process);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void waitUntilThisProcessForCreatingContainerIsFinished(Process process) {
		 ExecutingCommandThread thread = new ExecutingCommandThread(process);
		 thread.start();
		 try {
			thread.join(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(thread.isAlive()) {
			thread.interrupt();
			throw new GiriContainerCreateFailedException(containerName);
		}
	}

	public void copyMakeFileIntoContainer(String workDirectory, String workDirectoryInContainer) {
		copyFileIntoContainer(this.containerName, "Makefile" , workDirectory, workDirectoryInContainer);
	}
	
	public void copyLotTxtFileIntoContainer(String workDirectory, String workDirectoryInContainer) {
		copyFileIntoContainer(this.containerName, "loc.txt" , workDirectory, workDirectoryInContainer);
		
	}
	
	private void copyFileIntoContainer(String containerName , String fileName, String workDirectory, String workDirectoryInContainer) {
		Process process = null;
		String command = String.format("sudo docker cp %s%s %s:%s%s", workDirectory, fileName, containerName, workDirectoryInContainer,fileName);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void make(String workDirectoryInContainer) {
		Process process = null;
		String command = String.format("sudo docker exec -it -w %s %s make", workDirectoryInContainer, containerName);
		try {
			process = Runtime.getRuntime().exec(command);
			waitUntilProcessForMakeCommandIsFinished(process);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void waitUntilProcessForMakeCommandIsFinished(Process process) {
		ExecutingCommandThread thread = new ExecutingCommandThread(process);
		 thread.start();
		 try {
			thread.join(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(thread.isAlive()) {
			thread.interrupt();
			throw new GiriMakeCommandFailedException(containerName);
		}
		
	}

	public void downloadSlicLocFileIntoWorkDirectory(String workDirectoryInContainer, String workDirectory,String cFileNameWithoutExtension) {
		String sliceLocFileName = cFileNameWithoutExtension + ".slice.loc";
		this.downloadFileFromContainer(this.containerName, sliceLocFileName, workDirectoryInContainer, workDirectory);

		
	}

	private void downloadFileFromContainer(String containerName , String fileName, String workDirectoryInContainer, String  workDirectory) {
		Process process = null;
		String command = String.format("sudo docker cp %s:%s%s %s%s", containerName, workDirectoryInContainer,fileName, workDirectory, fileName);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
