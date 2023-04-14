package dynamicSlice.adapter.commandTool.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import dynamicSlice.adapter.commandTool.MyCompiler;

public class CCompilerUbuntu extends MyCompiler {

	
    public CCompilerUbuntu(String directoryPath){
        super.setDirectory(directoryPath);
    }
    
    @Override
    public void compile(String fileName){
        String exeName = fileName.substring(0, fileName.lastIndexOf('.'));
        try {
            Process p = compileProgramFileAndReturnProcess(fileName,exeName);
            try {
                p.waitFor();
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }finally {
				p.destroy();
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Process compileProgramFileAndReturnProcess(String fileName, String exeName) throws IOException {
		String[] cmdOfCompile =new String[]{"/bin/sh", "-c", "gcc "+ this.directory + fileName
				+ " -fprofile-arcs -ftest-coverage -g3 -lm -o " + this.directory + "exe"};
        Process p =Runtime.getRuntime().exec(cmdOfCompile, null, new File(this.directory));
        return p;
    }

	@Override
    public void run(String fileName,String testData){
        CCompilerRunner runByThread = new CCompilerRunner(testData,this.directory,"exe");
        runByThread.start();
		try {			
			runByThread.join(8000);
			if(runByThread.isAlive()) {
				runByThread.interrupt();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private String getProgramResult(Process p) {
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                if (!((line = in.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stringBuilder.append(line+"\n");
        }
        return stringBuilder.toString();
    }
	class CCompilerRunner extends Thread{
		private String testData;
		private String workingDirectory;
		private String executableFile;
		private boolean lock = true;
		
		public CCompilerRunner(String testData, String workingDirectory, String executableFile) {
			this.testData = testData;
			this.workingDirectory = workingDirectory;
			this.executableFile = executableFile;
		}
		
		public void run() {
			Process p =null;
			try {
				p = Runtime.getRuntime().exec(workingDirectory+executableFile+" "+this.testData, null, new File(workingDirectory));
				p.waitFor();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
            	e.printStackTrace();
            }finally {
				p.destroy();
			}
			
		}
	}
}
