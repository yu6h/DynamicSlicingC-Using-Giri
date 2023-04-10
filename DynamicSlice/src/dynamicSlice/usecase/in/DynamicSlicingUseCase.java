package dynamicSlice.usecase.in;
import java.util.List;

import dynamicSlice.entity.CprogramExpertUsedArgvAsInput;

public interface DynamicSlicingUseCase {

	public List<Integer> execute(CprogramExpertUsedArgvAsInput input);
	
}
