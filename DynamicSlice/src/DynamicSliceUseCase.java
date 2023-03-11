import java.util.List;

public class DynamicSliceUseCase {
	
	public List<Integer> execute(CprogramExpertUsedArgvAsInput studentProgramDTO) {
		DynamicSlicing dynamicSlicing = new DynamicSlicingUsedGiri();
		
		dynamicSlicing.setCProgramInfoUsedArgvAsInput(studentProgramDTO);
		dynamicSlicing.execute();
		
		List<Integer> result = dynamicSlicing.getDynamicResult();

		return result;		
	}

}
