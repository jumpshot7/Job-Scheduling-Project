/* Zackaria Mamdouh
 * CSCI 340
 * Professor Anne Smith-Thompson
 * Fall 2023
 * Job Scheduling Project
 * 
 * Data Structures Used: ArrayList, PriorityQueue
 * Big O Runtimes:
 * FIFO: O(n): Each job is processed in the order it arrives. Appropriate usage: When the order of execution is essential and where task duration are uniform
 * SJF:O(nlogn):Due to sorting jobs based on CPU burst time. Appropriate usage: Ideal for batch processing envrionments where job lengths are known. Good at minimizing arrival time, but may cause "starvation"
 * SRTF: O(n^2): Involves sorting jobs by priority. Appropriate usage: Ideal for when responsiveness is important, and jobs may arrive at different times.
 * Highest Priority: O(nlogn): Involves sorting by priortiy. Appropriate usage: Where tasks must be being priority due to their importance
 * Round-Roubin: O(n): Each job gets an equal time slice, overhead might increase due to context switiching. Appropriate usage: Effective time-sharing system where fairness and responsiveness are more crucial than turnaround time.
 */
 

import java.util.*;

public class Job implements Comparable<Job> {
    protected int arrTime; // Arrival time of job
    protected int cpuBurst; // CPU burst time required for job
    protected int priority; // Priority of job
    protected int exitTime; // Time at which job exits or completes
    protected int turnaroundTime; // Turnaround time of job, from arrival to completion
    protected int remainingTime; // Remaining time needed for job to complete
    private static final Random random = new Random(); // Random generator for attributes

    // Constructor initializes all attributes
    public Job() {
        this.arrTime = 1 + random.nextInt(250); // Arrival time between 1 and 250
        this.cpuBurst = 2 + random.nextInt(14); // CPU burst time between 2 and 15
        this.priority = 1 + random.nextInt(5); // Priority between 1 and 5
        this.remainingTime = this.cpuBurst; // Initially, remaining time is equal to cpuBurst
    }

    // Getters and Setters for all attributed to preserve encapsulation.
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

    // toString method to display initial jobs and algorithm results
    @Override
    public String toString() {
        return String.format(Locale.US, "Job{arrTime=%d, cpuBurst=%d, priority=%d, exitTime=%d, turnaroundTime=%d, remainingTime=%d}",
                arrTime, cpuBurst, priority, exitTime, turnaroundTime, remainingTime);
    }    

    // compareTo method to sort jobs by arrival time
    @Override
    public int compareTo(Job other) {
        return Integer.compare(this.arrTime, other.arrTime);
    }
}
