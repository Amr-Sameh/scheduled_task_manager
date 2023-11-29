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

    private LocalDateTime nextRunTime;
    private Integer runEveryXSeconds;

    abstract void launch();


    public void run() {
        status = ScheduleTaskStatus.RUNNING;
        launch();
        incrementTimesRun();
        status = ScheduleTaskStatus.FINISHED;

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
        return true;
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

    @Override
    public int compareTo(ScheduledTask task) {
        return this.getNextRunTime().compareTo(task.getNextRunTime());
    }
}
