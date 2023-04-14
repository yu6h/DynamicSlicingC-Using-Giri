package dynamicSlice.adapter.commandTool.factory;

import dynamicSlice.adapter.commandTool.MyCompiler;
import dynamicSlice.adapter.commandTool.ProgramLanguage;
import dynamicSlice.adapter.commandTool.gcov.GcovTool;

public interface CommandToolFactory {
	MyCompiler createCompiler(ProgramLanguage programLanguage, String directoryPath);
    GcovTool createGcovTool(String directory);
}
