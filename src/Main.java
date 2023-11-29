public class Main {

    public static void main(String[] args) {

        try {
            ScheduledTaskManager scheduledTaskManager = ScheduledTaskManager.getInstance();
            scheduledTaskManager.addTask(new RepeatableJob(5, 50, "RepeatableJob 1", 10));
            scheduledTaskManager.run();
            System.out.println("scheduledTaskManager started");
            Thread.sleep(3000);
            System.out.println("sleep ended");
            scheduledTaskManager.addTask(new RepeatableJob(5, 5, "RepeatableJob 2", 5));
            System.out.println("RepeatableJob 2 added");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}