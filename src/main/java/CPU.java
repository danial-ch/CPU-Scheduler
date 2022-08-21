import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CPU {

    List<Core> cores;
    List<Process> runningProcess;
    private int busyCores = 0;
    private int emptyCores;
    private long startTime;

    public CPU(int N) {
        emptyCores = N;
        startTime = System.currentTimeMillis();
        cores = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            Core core = new Core();
            cores.add(core);
        }
        runningProcess = new ArrayList<>(N);
    }

    public void startReport(){
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Scheduler.cpuLock.lock();
                        statusReport();
                        Scheduler.cpuLock.unlock();
                    }
                },
                5000,5000
        );
    }

    public void statusReport(){
        Main.appendText("Busy cores = " + busyCores);
        Main.appendText("Empty cores = " + emptyCores);
        for (Process process : runningProcess) {
            Main.appendText("Process " +process.getId() + " running");
        }
        Main.appendText("Total run time = " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime));
        Main.appendText("--------------------------------------------------------");
    }

    public void addProcessToList(Process p){
        this.runningProcess.add(p);
    }

    public void removeProcessFromList(Process p){
        this.runningProcess.remove(p);
    }

    public List<Core> getCores() {
        return cores;
    }

    public void setCores(List<Core> cores) {
        this.cores = cores;
    }

    public int getBusyCores() {
        return busyCores;
    }

    public void setBusyCores(int busyCores) {
        this.busyCores = busyCores;
    }

    public int getEmptyCores() {
        return emptyCores;
    }

    public void setEmptyCores(int emptyCores) {
        this.emptyCores = emptyCores;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
