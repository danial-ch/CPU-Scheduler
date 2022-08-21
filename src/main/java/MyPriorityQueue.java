import java.util.Comparator;
import java.util.LinkedList;

public class MyPriorityQueue extends LinkedList<Process> {

    public boolean addToQueue(Process process) {
        add(process);
        this.sort(Comparator.comparing(Process::getArrivalTime));
        return true;
    }

}
