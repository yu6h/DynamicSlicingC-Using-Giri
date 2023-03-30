package dynamicSlice.adapter.giriAdapter;
import java.io.IOException;

import dynamicSlice.usecase.in.Giri;

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
	public static void main(String[] args) {
		GiriImpl giri = new GiriImpl();
		String dir = "/giri/test/UnitTests/ST";
		giri.setContainerName("giriContainer");
//		giri.createWorkDirectoryInContainer(dir);
		giri.deleteWorkDirectoryInContainer(dir);
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
		}
		
	}

}
