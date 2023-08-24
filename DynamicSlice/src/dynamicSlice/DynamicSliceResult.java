package dynamicSlice;

import java.util.List;

public class DynamicSliceResult {
	private List<Integer> lineNumbersOfCoveredOutputStatement;
	private List<Integer> lineNumbersOfDynamicSlice;
	
	public List<Integer> getLineNumbersOfCoveredOutputStatement() {
		return lineNumbersOfCoveredOutputStatement;
	}
	public void setLineNumbersOfCoveredOutputStatement(List<Integer> lineNumbersOfCoveredOutputStatement) {
		this.lineNumbersOfCoveredOutputStatement = lineNumbersOfCoveredOutputStatement;
	}
	public List<Integer> getLineNumbersOfDynamicSlice() {
		return lineNumbersOfDynamicSlice;
	}
	public void setLineNumbersOfDynamicSlice(List<Integer> lineNumbersOfDynamicSlice) {
		this.lineNumbersOfDynamicSlice = lineNumbersOfDynamicSlice;
	}

}
