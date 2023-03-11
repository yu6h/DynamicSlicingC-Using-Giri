import java.util.ArrayList;
import java.util.List;

public class DynamicResultAdapter {
	
	public List<Integer> adapteToResultUsedScanfAsInput(List<Integer> lineNumbers, int dummyLines) {
		List<Integer> result = new ArrayList<Integer>();
		for(Integer lineNumber:lineNumbers) {
			result.add(lineNumber-dummyLines);
		}
		return result;
	}

}
