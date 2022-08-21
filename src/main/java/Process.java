import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Process implements Runnable {

    Process p;
    long arrivalTime;
    long burstTime;
    int coresNeeded;
    State state;
    int id;
    List<Core> coresUsed;

    public Process(long arrivalTime, long burstTime, int coresNeeded, int id) {
        p = this;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.coresNeeded = coresNeeded;
        this.id = id;
        this.state = State.READY;
        coresUsed = new ArrayList<>();
    }

    @Override
    public void run() {
        Main.schedulerLock.lock();
        Scheduler.getInstance().addProcess(this);
        Main.schedulerLock.unlock();
    }

    public void startTimer(){
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Main.schedulerLock.lock();
                        Scheduler.getInstance().finishProcess(p);
                        Main.schedulerLock.unlock();
                    }
                },
                burstTime
        );
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(long burstTime) {
        this.burstTime = burstTime;
    }

    public int getCoresNeeded() {
        return coresNeeded;
    }

    public void setCoresNeeded(int coresNeeded) {
        this.coresNeeded = coresNeeded;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Core> getCoresUsed() {
        return coresUsed;
    }

    public void setCoresUsed(List<Core> coresUsed) {
        this.coresUsed = coresUsed;
    }
}
