/** 
 * Represents a job in a CPU scheduling system. This class encapsulates all properties and behaviors of a schedulable job
 * @ author Zackaria Mamdouh
 * @ version 2.0
 */
import java.util.*;

public class Job implements Comparable<Job> {
    protected int arrTime;
    protected int cpuBurst;
    protected int priority;
    protected int exitTime;
    protected int turnaroundTime;
    protected int remainingTime;
    private static final Random random = new Random(); 

    public Job() {
        this.arrTime = 1 + random.nextInt(250); 
        this.cpuBurst = 2 + random.nextInt(14);
        this.priority = 1 + random.nextInt(5); 
        this.remainingTime = this.cpuBurst;
    }

    public int getArrTime() {
        return arrTime;
    }

    public int getCpuBurst() {
        return cpuBurst;
    }

    public int getPriority() {
        return priority;
    }

    public int getExitTime() {
        return exitTime;
    }

    public void setExitTime(int exitTime) {
        this.exitTime = exitTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Job{arrTime=%d, cpuBurst=%d, priority=%d, exitTime=%d, turnaroundTime=%d, remainingTime=%d}",
                arrTime, cpuBurst, priority, exitTime, turnaroundTime, remainingTime);
    }    

    @Override
    public int compareTo(Job other) {
        return Integer.compare(this.arrTime, other.arrTime);
    }
}
