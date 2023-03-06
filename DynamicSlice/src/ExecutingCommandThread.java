
public class ExecutingCommandThread extends Thread{
	private Process process;
    
    public ExecutingCommandThread(Process process) {
    	this.process = process;
    }
    
    @Override
    public void run() {
    	
    	try {
			this.process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.process.destroy();
			Thread.currentThread().interrupt();
		}

    }
	public Process getProcess() {
		return process;
	}
}
