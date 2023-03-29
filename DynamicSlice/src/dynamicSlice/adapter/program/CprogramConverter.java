package dynamicSlice.adapter.program;
import java.util.List;

import dynamicSlice.entity.CprogramExpertUsedArgvAsInput;

public interface CprogramConverter {
	
	void convert();

	public CprogramExpertUsedArgvAsInput generateCprogramExpertUsedArgvAsInput();
	
	List<Integer> convertChangedLineNumbersToOriginalLineNumbers(List<Integer> list);
}
