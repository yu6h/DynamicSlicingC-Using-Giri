package dynamicSlice.usecase.service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import dynamicSlice.entity.CprogramUsedArgvAsInput;
import dynamicSlice.entity.CprogramUsedArgvAsInputBuilder;
import dynamicSlice.usecase.in.DynamicSliceUseCase;
import dynamicSlice.usecase.in.DynamicSliceUseCaseInput;
import dynamicSlice.usecase.in.DynamicSliceUseCaseOutput;

public class DynamicSliceUseCaseImpl implements DynamicSliceUseCase {
	
	private DynamicSliceToolAdapter dynamicSliceTool;
	private CprogramUsedArgvAsInput cprogramUsedArgvAsInput;

	public DynamicSliceUseCaseImpl(DynamicSliceToolAdapter dynamicSliceTool) {
		this.dynamicSliceTool = dynamicSliceTool;
	}
	
	@Override
	public DynamicSliceUseCaseOutput execute(DynamicSliceUseCaseInput input) {
		this.cprogramUsedArgvAsInput = CprogramUsedArgvAsInputBuilder.newInstance()
				.buildcFileName(input.getcFileName())
				.buildcFileNameWithoutExtension(input.getcFileNameWithoutExtension())
				.buildQuetionID(input.getQuetionID())
				.buildStudentID(input.getStudentID())
				.buildInputData(input.getInputData())
				.buildProgramContent(input.getProgramContent())
				.buildLineNumbersOfOutputStatement(input.getLineNumbersOfOutputStatement())
				.build();
		TreeSet<Integer> lineNumbersSet = new TreeSet<Integer>();
		this.dynamicSliceTool.iniitilizeDirectory(this.cprogramUsedArgvAsInput.getStudentID()
				,this.cprogramUsedArgvAsInput.getQuestionID(), this.cprogramUsedArgvAsInput.getcFileName());
		this.dynamicSliceTool.setPreprocessedCprogramContent(this.cprogramUsedArgvAsInput.getProgramContent());
		this.dynamicSliceTool.setInputData(this.cprogramUsedArgvAsInput.getInputData());
		List<Integer> lineNumberOfTargetStatement = this.cprogramUsedArgvAsInput.getLineNumbersOfCoveredOutputStatement();
		
		for(Integer lineNumberOfTarget:lineNumberOfTargetStatement) {
			this.dynamicSliceTool.setTargetLineNumber(lineNumberOfTarget);
			this.dynamicSliceTool.execute();
			List<Integer> lineNumbersOfDynamicSlice = this.dynamicSliceTool.getLineNumbersOfDynamicSlice();
			lineNumbersSet.addAll(lineNumbersOfDynamicSlice);
		}
		this.dynamicSliceTool.deleteDirectory();
		List<Integer> linNumbersOfSliceResults = new ArrayList<Integer>(lineNumbersSet);
		DynamicSliceUseCaseOutput output = new DynamicSliceUseCaseOutput();
		output.setLineNumbersOfDynamicSlicing(linNumbersOfSliceResults);
		return output;
	}

}
