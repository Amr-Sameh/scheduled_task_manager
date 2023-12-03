import cronScheduler.ScheduledTaskManager;
import jobs.IntervalJobExample1;
import jobs.RepeatableJob;

public class Main {

    public static void main(String[] args) {

        try {
            ScheduledTaskManager scheduledTaskManager = ScheduledTaskManager.getInstance();
            scheduledTaskManager.addTask(new RepeatableJob(5, 50, "jobs.RepeatableJob 1", 10));
            scheduledTaskManager.addTask(new IntervalJobExample1(15, "jobs.IntervalJobExample1 1"));
            scheduledTaskManager.run();
            Thread.sleep(3000);
            scheduledTaskManager.addTask(new RepeatableJob(5, 5, "jobs.RepeatableJob 2", 5));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}