package dynamicSlice.usecase;
import java.util.List;

import dynamicSlice.entity.CprogramExpertUsedArgvAsInput;
import dynamicSlice.entity.DynamicSlicing;
import dynamicSlice.service.giri.GiriService;

public class DynamicSliceUseCase {
	
	public List<Integer> execute(CprogramExpertUsedArgvAsInput studentProgramDTO) {
		DynamicSlicing dynamicSliceservice = new GiriService();
		
		dynamicSliceservice.setCProgramInfoUsedArgvAsInput(studentProgramDTO);
		dynamicSliceservice.execute();
		
		List<Integer> result = dynamicSliceservice.getDynamicResult();

		return result;		
	}

}
