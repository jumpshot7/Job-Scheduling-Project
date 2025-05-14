 public class main {
 public static void main(String[] args) {

        JobScheduler scheduler = new JobScheduler(); 

        //int quantum = 5; //uncomment this line, and line 15 if you choose to run Round Robin with context switch
        //int contextSwitchTime = 0; //uncomment this line, and line 16 if you choose to run Round Robin without context switch
        scheduler.printJobs();
        scheduler.fifo();
        scheduler.sjfNonPreemptive();
        scheduler.srtf();
        scheduler.highestPriorityFirstNonPreemptive();
        //scheduler.roundRobin(quantum, contextSwitchTime); 
        //scheduler.roundRobin(quantum, 0); 
    }
 }