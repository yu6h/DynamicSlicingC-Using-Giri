package dynamicSlice.adapter.commandTool.gcov;

import java.io.File;
import java.io.IOException;

public class GcovUbuntu extends GcovTool{
    public GcovUbuntu(String directoryPath){
        super.setDirectory(directoryPath);
    }

    @Override
    public void runGcovCommand(String cfileName) {
        try {
        	
			String[] cmdOfCompile =new String[]{"/bin/sh", "-c", "gcov "+ this.directory + cfileName};
			
            Process p = Runtime.getRuntime().exec(cmdOfCompile, null, new File(this.directory));
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

}
