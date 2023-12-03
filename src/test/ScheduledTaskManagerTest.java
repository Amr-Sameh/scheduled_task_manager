package test;

import cronScheduler.ScheduledTaskManager;
import cronScheduler.tasks.ScheduledTask;
import jobs.IntervalJobExample1;
import jobs.RepeatableJob;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScheduledTaskManagerTest {

    private ScheduledTaskManager scheduledTaskManager;

    @BeforeEach
    public void setUp() {
        scheduledTaskManager = ScheduledTaskManager.getInstance();
        scheduledTaskManager.run();
    }

    @AfterEach
    public void tearDown() {
        scheduledTaskManager.close();
    }

    @Test
    public void testScheduledTaskExecution() throws InterruptedException {
        ScheduledTask task = new RepeatableJob(2, 2, "TestRepeatableJob", 2);

        scheduledTaskManager.addTask(task);

        Thread.sleep(10000); // To allow time for the task to execute


        assertTrue(task.getTimesRun() > 1, "Task should have executed more than once");


    }


    @Test
    public void testRunningAddedTaskAndRemovingTask() throws InterruptedException {
        ScheduledTask task = new RepeatableJob(5, 20, "TestRepeatableJob", 2);

        scheduledTaskManager.addTask(task);
        Thread.sleep(10000); // To allow time for the task to execute

        assertTrue(task.getTimesRun() > 0, "Task should have executed at least once");
        Integer timesRun = task.getTimesRun();
        scheduledTaskManager.removeTask(task);

        Thread.sleep(5000);  // To allow time for the task to potentially execute (should not execute)

        assertEquals(timesRun, task.getTimesRun(), "Task should not execute after removal");
    }

    @Test
    public void testBatchRunningAddedTasksAndRemovingTasks() throws InterruptedException {
        ScheduledTask task1 = new RepeatableJob(5, 10, "TestRepeatableJob1", 2);
        ScheduledTask task2 = new IntervalJobExample1(5, "TestIntervalJob");

        scheduledTaskManager.addTask(task1);
        scheduledTaskManager.addTask(task2);

        Thread.sleep(10000); // To allow time for the task to execute

        assertTrue(task1.getTimesRun() > 0, "Task1 should have executed at least once");
        assertTrue(task2.getTimesRun() > 0, "Task2 should have executed at least once");

        scheduledTaskManager.clearTasks();
        Integer task1TimesRun = task1.getTimesRun();
        Integer task2TimesRun = task2.getTimesRun();

        Thread.sleep(5000);  // To allow time for the task to potentially execute (should not execute)

        assertEquals(task1TimesRun, task1.getTimesRun(), "Task1 should not execute after clearing tasks");
        assertEquals(task2TimesRun, task2.getTimesRun(), "Task2 should not execute after clearing tasks");
    }


}