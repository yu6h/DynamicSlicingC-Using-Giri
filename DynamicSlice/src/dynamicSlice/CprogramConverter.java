package dynamicSlice;
import java.util.List;

public interface CprogramConverter {
	
	void convert();

	public CprogramExpertUsedArgvAsInput generateCprogramExpertUsedArgvAsInput();
	
	List<Integer> convertChangedLineNumbersToOriginalLineNumbers(List<Integer> list);
}
