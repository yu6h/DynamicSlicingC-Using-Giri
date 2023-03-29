package dynamicSlice;
import java.util.List;

public class DynamicSliceResultDTO {
	
	private int[] lineNumberOfDynamicSlice;
	
	private int lineNumberOfTargetToBeSliced;

	public int getLineNumberOfTargetToBeSliced() {
		return lineNumberOfTargetToBeSliced;
	}

	public void setLineNumberOfTargetToBeSliced(int lineNumberOfTargetToBeSliced) {
		this.lineNumberOfTargetToBeSliced = lineNumberOfTargetToBeSliced;
	}

	public int[] getLineNumberOfDynamicSlice() {
		return lineNumberOfDynamicSlice;
	}

	public void setLineNumberOfDynamicSlice(int[] lineNumberOfDynamicSlice) {
		this.lineNumberOfDynamicSlice = lineNumberOfDynamicSlice;
	}
	

}
