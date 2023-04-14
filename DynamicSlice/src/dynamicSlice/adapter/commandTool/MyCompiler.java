package dynamicSlice.adapter.commandTool;

import java.io.File;

public abstract class MyCompiler {

    protected String directory;

    protected MyCompiler(){
    }
    public abstract void compile(String filename);

    protected void setDirectory(String directoryPath){
        this.directory = directoryPath;
    }

    public abstract void run(String fileName,String testData);

}