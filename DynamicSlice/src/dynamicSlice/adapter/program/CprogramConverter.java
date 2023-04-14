package dynamicSlice.adapter.program;
import java.util.List;

import dynamicSlice.entity.CprogramUsedArgvAsInput;

public interface CprogramConverter {
	
	void convert();

	public CprogramUsedArgvAsInput generateCprogramExpertUsedArgvAsInput();
	
	List<Integer> convertChangedLineNumbersToOriginalLineNumbers(List<Integer> list);
}
