public class IntervalJobExample1 extends IntervalScheduledTask{


    public IntervalJobExample1(int interval , String id) {
        super(interval,id);
    }

    @Override
    void launch() {
        System.out.println("Hello from IntervalJobExample1");

    }
}
