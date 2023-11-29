import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class ScheduledTaskManager implements Closeable {
    private static ScheduledTaskManager instance;

    static {
        try {
            instance = new ScheduledTaskManager();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final PriorityQueue<ScheduledTask> executionQueue;
    private final Thread executionCheckerThread;
    private static final Object lock = new Object();

    private ScheduledTaskManager() throws InterruptedException {
        executionQueue = new PriorityQueue<>();
        executionCheckerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    synchronized (lock) {
                        try {
                            checkForTasks();
                        } catch (InterruptedException e) {
                            try {
                                checkForTasks();
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }

                        }

                    }
                }
            }
        });
    }

    public static ScheduledTaskManager getInstance() throws InterruptedException {
        if (instance == null) {
            instance = new ScheduledTaskManager();
        }
        return instance;
    }


    public void run() {
        executionCheckerThread.start();
    }

    private void checkForTasks() throws InterruptedException {
        while (true) {
            synchronized (executionQueue){
                if (!executionQueue.isEmpty()) {
                    ScheduledTask task = executionQueue.poll();
                    if (task.getNextRunTime().isEqual(java.time.LocalDateTime.now()) || task.getNextRunTime().isBefore(java.time.LocalDateTime.now())) {
                        executeTask(task);
                    } else {
                        executionQueue.add(task);
                        long waitTime = task.getNextRunTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli() - LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
                        if (waitTime > 0) {
                            lock.wait(waitTime);
                        }
                        break;
                    }
                } else {
                    lock.wait();
                    System.out.println("No tasks to run");
                    break;
                }
            }
        }
    }

    private void executeTask(ScheduledTask task) {
        Thread executeTaskThread = new Thread(() -> {
            task.run();
            if (task.shouldRunAgain()) {
                task.calculateNextRunTime();
                addTask(task);
            }
        });
        executeTaskThread.start();
    }


    public void addTask(ScheduledTask task) {
        synchronized (executionQueue) {

            executionQueue.add(task);
            executionCheckerThread.interrupt();
        }
    }

    public void removeTask(ScheduledTask task) {
        executionQueue.remove(task);

    }


    public void clearTasks() {
        executionQueue.clear();
    }

    public ArrayList<ScheduledTask> getTasks() {

        return new ArrayList<>(executionQueue);
    }

    public void setTasks(ArrayList<ScheduledTask> tasks) {
        executionQueue.clear();
        for (ScheduledTask scheduledTask : tasks) {
            addTask(scheduledTask);
        }
    }


    @Override
    public void close() throws IOException {
        executionCheckerThread.interrupt();
    }
}