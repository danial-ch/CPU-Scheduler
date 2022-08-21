public class Core {

    private Process currentProcess;
    private boolean busy;

    public Core() {
        currentProcess = null;
        busy = false;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(Process currentProcess) {
        this.currentProcess = currentProcess;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
