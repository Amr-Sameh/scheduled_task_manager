package jobs;

import cronScheduler.tasks.IntervalScheduledTask;

public class IntervalJobExample1 extends IntervalScheduledTask {


    public IntervalJobExample1(int interval , String id) {
        super(interval,id);
    }

    @Override
    protected void launch() {
        System.out.println("Hello from jobs.IntervalJobExample1");

    }
}
