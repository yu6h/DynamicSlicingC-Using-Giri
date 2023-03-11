
public interface CprogramAdapter {
	
	public void setOriginalCProgramDTO(StudentProgramDTO studentProgramDTO);

	public CprogramExpertUsedArgvAsInput generateCprogramExpertUsedArgvAsInput();
	
	public int getNumbersOfDummyLines();

}
