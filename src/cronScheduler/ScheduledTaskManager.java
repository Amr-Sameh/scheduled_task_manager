package cronScheduler;

import cronScheduler.tasks.ScheduledTask;

import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class ScheduledTaskManager implements Closeable {
    private static ScheduledTaskManager instance;

    private final PriorityQueue<ScheduledTask> executionQueue;
    private final Thread executionCheckerThread;
    private volatile boolean isRunning;

    private ScheduledTaskManager() {
        executionQueue = new PriorityQueue<>();
        isRunning = false;

        executionCheckerThread = new Thread(() -> {
            while (isRunning) {
                checkForTasks();
            }
        });
    }

    public static ScheduledTaskManager getInstance() {
        if (instance == null) {
            instance = new ScheduledTaskManager();
        }
        return instance;
    }

    public void run() {
        isRunning = true;
        executionCheckerThread.start();
    }

    private void checkForTasks() {
        synchronized (executionQueue) {
            try {
                if (!executionQueue.isEmpty()) {
                    ScheduledTask task = executionQueue.poll();
                    if (task.getNextRunTime().isEqual(LocalDateTime.now()) || task.getNextRunTime().isBefore(LocalDateTime.now())) {
                        executeTask(task);
                    } else {
                        executionQueue.add(task);
                        long waitTime = task.getNextRunTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli() - LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
                        if (waitTime > 0) {
                            executionQueue.wait(waitTime);
                        }
                    }
                } else {
                    executionQueue.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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
            executionQueue.notify(); // Notify the waiting thread
        }
    }

    public void removeTask(ScheduledTask task) {
        synchronized (executionQueue) {
            executionQueue.remove(task);
        }
    }

    public void clearTasks() {
        synchronized (executionQueue) {
            executionQueue.clear();
        }
    }

    public ArrayList<ScheduledTask> getTasks() {
        synchronized (executionQueue) {
            return new ArrayList<>(executionQueue);
        }
    }

    @Override
    public void close() {
        isRunning = false;
        executionCheckerThread.interrupt();
    }
}
