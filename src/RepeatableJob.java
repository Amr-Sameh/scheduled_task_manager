import java.time.LocalDateTime;

public  class RepeatableJob extends   RebetableScheduledTask{


    public RepeatableJob(int interval, int timesToRun, String id , Integer runEveryXSeconds) {
        super(interval, timesToRun, id,runEveryXSeconds);
    }

    @Override
    void launch() {
        System.out.println("Hello from "+  getId() + "  ( should run at : "+ getNextRunTime()+" , ran at : "+ LocalDateTime.now() +") : " + getTimesRun());

    }


}
