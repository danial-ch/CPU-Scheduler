import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler implements Runnable{

    private static Scheduler instance = null;
    private static int M;
    private CPU cpu;
    private Boolean fullCPU = false;
    private long currentMillis;
    public static Lock cpuLock;

    MyPriorityQueue processQueue;

    private Scheduler() {
        M = Main.M;
        cpuLock = new ReentrantLock();
        processQueue = new MyPriorityQueue();
    }

    public static Scheduler getInstance(){
        if(instance == null){
            instance = new Scheduler();
        }
        return instance;
    }

    public void addProcess(Process p){
        synchronized (processQueue){
            processQueue.addToQueue(p);
        }
    }

    public Process peek(){
        synchronized (processQueue){
            return processQueue.peek();
        }
    }

    public Process poll(){
        synchronized (processQueue){
            return processQueue.poll();
        }
    }

    public int size(){
        synchronized (processQueue){
            return processQueue.size();
        }
    }

    public Boolean getFull(){
        synchronized (fullCPU){
            return fullCPU;
        }
    }

    public void setFull(Boolean full){
        synchronized (fullCPU){
            fullCPU = full;
        }
    }

    public void finishProcess(Process p){
        currentMillis = System.currentTimeMillis();
        long burstTime = (new Random().nextInt(10000));
        long arrivalTime = (new Random().nextInt(1000));
        p.setState(State.READY);
        p.setBurstTime(burstTime);
        p.setArrivalTime(currentMillis + arrivalTime);
        addProcess(p);
        cpuLock.lock();
        List<Core> coresUsed = p.getCoresUsed();
        for (Core core : coresUsed) {
            core.setBusy(false);
            core.setCurrentProcess(null);
        }
        coresUsed.clear();
        cpu.removeProcessFromList(p);
        setFull(false);
        cpu.setEmptyCores(cpu.getEmptyCores() + p.getCoresNeeded());
        cpu.setBusyCores(cpu.getBusyCores() - p.getCoresNeeded());
        cpuLock.unlock();
        Main.appendText("Process " + p.getId() + " finished and reassigned to queue with arrival time : " + arrivalTime + " and burst time : " + burstTime);
    }

    public void startQueuing(){
        while (true){
            if(!getFull()){
                int index = 0;
                if(size() != 0){
                    if(cpu.getEmptyCores() >= peek().getCoresNeeded()){
                        Process p = poll();
                        cpuLock.lock();
                        List<Core> cores = cpu.getCores();
                        for (Core core : cores ) {
                            if(!core.isBusy()){
                                core.setBusy(true);
                                core.setCurrentProcess(p);
                                p.coresUsed.add(core);
                                index++;
                                if(index == p.getCoresNeeded()){
                                    Main.appendText(index + " cores assigned to process " + p.getId());
                                    break;
                                }
                            }
                        }
                        cpu.setEmptyCores(cpu.getEmptyCores() - p.getCoresNeeded());
                        cpu.setBusyCores(cpu.getBusyCores() + p.getCoresNeeded());
                        p.setState(State.RUNNING);
                        p.startTimer();
                        cpu.addProcessToList(p);
                        cpuLock.unlock();
                    }
                    else{
                        Main.appendText("Exception in assigning the process");
                        setFull(true);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        startQueuing();
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public boolean isFullCPU() {
        return fullCPU;
    }

    public void setFullCPU(boolean fullCPU) {
        this.fullCPU = fullCPU;
    }
}
