public abstract class IntervalScheduledTask  extends   ScheduledTask{


    public IntervalScheduledTask(int interval, String id) {
        super(interval, 1, id,0);
    }
}
