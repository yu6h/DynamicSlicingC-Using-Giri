package dynamicSlice.usecase.in;
import java.util.List;

import dynamicSlice.entity.CprogramUsedArgvAsInput;

public interface DynamicSlicingUseCase {

	public List<Integer> execute(CprogramUsedArgvAsInput input);
	
}
