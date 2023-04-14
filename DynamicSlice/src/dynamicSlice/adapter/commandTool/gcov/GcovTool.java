package dynamicSlice.adapter.commandTool.gcov;

import java.io.File;

public abstract class GcovTool {

    protected String directory;

    protected void setDirectory(String directoryPath){
        this.directory = directoryPath;
    }
    public abstract void runGcovCommand(String fileName);
}