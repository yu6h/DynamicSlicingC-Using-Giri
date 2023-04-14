package dynamicSlice.adapter.commandTool.factory;

import dynamicSlice.adapter.commandTool.MyCompiler;
import dynamicSlice.adapter.commandTool.ProgramLanguage;
import dynamicSlice.adapter.commandTool.compiler.CCompilerUbuntu;
import dynamicSlice.adapter.commandTool.gcov.GcovTool;
import dynamicSlice.adapter.commandTool.gcov.GcovUbuntu;

public class CommandToolFactoryUbuntuImpl implements CommandToolFactory {

	@Override
	public MyCompiler createCompiler(ProgramLanguage programLanguage, String directoryPath) {
        MyCompiler myCompiler = null;
        switch (programLanguage){
            case C:
                myCompiler = new CCompilerUbuntu(directoryPath);
                break;
		default:
			break;
        }
        return myCompiler;
	}

	@Override
	public GcovTool createGcovTool(String directory) {
        GcovTool gcovTool = new GcovUbuntu(directory);
        return gcovTool;
	}

}
