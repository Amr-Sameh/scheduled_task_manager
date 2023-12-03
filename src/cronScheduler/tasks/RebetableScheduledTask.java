package cronScheduler.tasks;

public  abstract class RebetableScheduledTask extends   ScheduledTask{


    public RebetableScheduledTask(int interval, int timesToRun, String id , Integer runEveryXSeconds) {
        super(interval, timesToRun, id,runEveryXSeconds);
    }


}
