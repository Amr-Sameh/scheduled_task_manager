package cronScheduler.tasks;

import cronScheduler.enums.ScheduleTaskStatus;

import java.time.LocalDateTime;

public abstract class ScheduledTask implements Comparable<ScheduledTask> {

    public ScheduledTask(int interval, int timesToRun, String id , Integer runEveryXSeconds) {
        this.interval = interval;
        this.timesToRun = timesToRun;
        this.id = id;
        this.runEveryXSeconds = runEveryXSeconds;
        lastRunTime = LocalDateTime.now().plusSeconds(interval);
        calculateNextRunTime();
    }

    private int interval;
    private String id;
    private int timesRun = 0;
    private ScheduleTaskStatus status = ScheduleTaskStatus.PENDING;
    private int timesToRun;
    private LocalDateTime addedTime;
    private LocalDateTime lastRunTime;
    private LocalDateTime firstRunTime;

    private LocalDateTime nextRunTime;
    private Integer runEveryXSeconds;

    protected abstract void launch();


    public void run() {
        if (timesRun == 0) {
            firstRunTime = LocalDateTime.now();
        }
        System.out.println("Running " + getId() + " at " + LocalDateTime.now());

        setLastRunTime(LocalDateTime.now());
        status = ScheduleTaskStatus.RUNNING;
        long startTime = System.currentTimeMillis();
        launch();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        incrementTimesRun();
        status = ScheduleTaskStatus.FINISHED;
        System.out.println("Finished " + getId() + " at " + LocalDateTime.now() + "  execution time : ("+elapsedTime+" ms) , next run at " + getNextRunTime());

    }


    public int getInterval() {
        return interval;
    }

    public int getTimesRun() {
        return timesRun;
    }

    public int getTimesToRun() {
        return timesToRun;
    }

    public void incrementTimesRun() {
        timesRun++;
    }

    public boolean shouldRunAgain() {
        return getTimesRun() < getTimesToRun();
    }

    public String getId() {
        return id;
    }

    public ScheduleTaskStatus getStatus() {
        return status;
    }

    public void stop() {
        status = ScheduleTaskStatus.FINISHED;
    }

    public LocalDateTime getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(LocalDateTime addedTime) {
        this.addedTime = addedTime;
    }

    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(LocalDateTime lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public LocalDateTime getNextRunTime() {
        return nextRunTime;
    }

    public void calculateNextRunTime() {
        this.nextRunTime = lastRunTime.plusSeconds(runEveryXSeconds);
    }

    public LocalDateTime getFirstRunTime() {
        return firstRunTime;
    }

    @Override
    public int compareTo(ScheduledTask task) {
        return this.getNextRunTime().compareTo(task.getNextRunTime());
    }
}
