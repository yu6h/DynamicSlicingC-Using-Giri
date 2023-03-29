import java.util.List;

public class DynamicSliceUseCase {
	
	public List<Integer> execute(CprogramExpertUsedArgvAsInput studentProgramDTO) {
		DynamicSlicing dynamicSliceservice = new GiriService();
		
		dynamicSliceservice.setCProgramInfoUsedArgvAsInput(studentProgramDTO);
		dynamicSliceservice.execute();
		
		List<Integer> result = dynamicSliceservice.getDynamicResult();

		return result;		
	}

}
