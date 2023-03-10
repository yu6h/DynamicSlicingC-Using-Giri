import java.io.IOException;

public class GiriImpl implements Giri{
	
	private String containerName;
	
	public static void main(String[] args) {
		Giri giri = new GiriImpl();
		giri.createContainer("110598067_1");
	}

	public void createContainer(String containerName) {
		this.containerName = containerName;
		String createGiriContainerCommand = String.format("docker run --name %s -d -i liuml07/giri /bin/bash", containerName);
		Process process = null;
		try {
			 process = Runtime.getRuntime().exec(createGiriContainerCommand);
			 wait20SecUntilThisProcessForCreatingContainerIsFinished(process);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void wait20SecUntilThisProcessForCreatingContainerIsFinished(Process process) {
		 ExecutingCommandThread thread = new ExecutingCommandThread(process);
		 thread.start();
		 try {
			thread.join(20000);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void make(String workDirectoryInContainer) {
		Process process = null;
		String command = String.format("docker exec -i -w %s %s make", workDirectoryInContainer, this.containerName);
		try {
			process = Runtime.getRuntime().exec(command);
			waitUntilProcessForMakeCommandIsFinished(process);
		} catch (IOException e) {
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopContainer(String containerName) {
		Process process = null;
		String command = String.format("docker stop %s", containerName);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeContainer(String containerName) {
		Process process = null;
		String command = String.format("docker rm %s", containerName);
		try {
			 process = Runtime.getRuntime().exec(command);
			 try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
