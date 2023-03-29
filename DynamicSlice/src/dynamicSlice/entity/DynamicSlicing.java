package dynamicSlice.entity;
import java.util.List;

public interface DynamicSlicing {
	
	public void setCProgramInfoUsedArgvAsInput(CprogramExpertUsedArgvAsInput programDTO);

	public void execute();
	
	public List<Integer> getDynamicResult();
	
}
