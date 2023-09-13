package dynamicSlice.usecase.in;

import java.util.List;

public class DynamicSliceUseCaseOutput {
	
	private List<Integer> lineNumbersOfDynamicSlicing;

	public List<Integer> getLineNumbersOfDynamicSlicingInConvertedProgram() {
		return lineNumbersOfDynamicSlicing;
	}

	public void setLineNumbersOfDynamicSlicing(List<Integer> lineNumbersOfDynamicSlicing) {
		this.lineNumbersOfDynamicSlicing = lineNumbersOfDynamicSlicing;
	}

}
