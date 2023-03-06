import java.util.List;

public class DynamicSliceResultDTO {
	
	private List<Integer> lineNumberOfDynamicSlice;
	
	private int lineNumberOfTargetToBeSliced;

	public int getLineNumberOfTargetToBeSliced() {
		return lineNumberOfTargetToBeSliced;
	}

	public void setLineNumberOfTargetToBeSliced(int lineNumberOfTargetToBeSliced) {
		this.lineNumberOfTargetToBeSliced = lineNumberOfTargetToBeSliced;
	}

	public List<Integer> getLineNumberOfDynamicSlice() {
		return lineNumberOfDynamicSlice;
	}

	public void setLineNumberOfDynamicSlice(List<Integer> lineNumberOfDynamicSlice) {
		this.lineNumberOfDynamicSlice = lineNumberOfDynamicSlice;
	}
	

}
