package cn.loyx.paxossim.simluator.util;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Log4j
class ChangeableDelayQueueTest {

    static class ChangeableDelayInt implements ChangeableDelayed {
        private long delay;
        private final int value;

        public ChangeableDelayInt(long delay, int value) {
            this.delay = TimeUnit.MILLISECONDS.convert(delay, TimeUnit.SECONDS)+System.currentTimeMillis();
            this.value = value;
        }

        @Override
        public long getDelay() {
//            System.out.println("getDelay: " + value + ":"+TimeUnit.SECONDS.convert(delay- System.currentTimeMillis(), TimeUnit.MILLISECONDS));
            return delay;
        }

        @Override
        public void changeDelay(long offset) {
            delay += (offset);
//            System.out.println(value + ":"+TimeUnit.SECONDS.convert(delay- System.currentTimeMillis(), TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "changeableDelayInt{" +
                    "delay=" + (delay-System.currentTimeMillis()) +
                    "ms, value=" + value +
                    '}';
        }
    }

    ChangeableDelayQueue<ChangeableDelayInt> queue;

    @BeforeEach
    void setUp() {
        BasicConfigurator.configure();
        queue = new ChangeableDelayQueue<>();
    }


    @Test
    void orderPutAndTake() {
        for (int i = 0; i < 5; i++) {
            ChangeableDelayInt changeableDelayInt = new ChangeableDelayInt(i, i);
            queue.put(changeableDelayInt);
        }
        take();
    }

    @Test
    void reversOrderPutAndTask() {
        for (int i = 0; i < 5; i++) {
            ChangeableDelayInt changeableDelayInt = new ChangeableDelayInt(5-i, i);
            queue.put(changeableDelayInt);
        }
        take();
    }

    @Test
    void randomOrderPutAndTask() {
        List<Integer> ints = Arrays.asList(3,2,4,1,5);
        Collections.shuffle(ints);
        System.out.println(ints);
        for (Integer i : ints) {
            ChangeableDelayInt changeableDelayInt = new ChangeableDelayInt(i, i);
            queue.put(changeableDelayInt);
        }
        take();
    }

    void take() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            ChangeableDelayInt take;
            try {
                take = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(take + " " + (System.currentTimeMillis()-start) + "ms");
        }
    }

    @Test
    void testChangeDelay() {
        List<ChangeableDelayInt> ints = new ArrayList<>();
        for (Integer integer : Arrays.asList(1, 5, 10, 15, 20)) {
            ChangeableDelayInt changeableDelayInt = new ChangeableDelayInt(integer, integer);
            ints.add(changeableDelayInt);
            queue.put(changeableDelayInt);
        }
        new Thread(()->{
            queue.changeDelay(ints.get(0), +21_000);
            queue.changeDelay(ints.get(3), -10_000); // 15 -10 = 5
            queue.changeDelay(ints.get(1),+20_000);  // 5 +20 = 25
            queue.changeDelay(ints.get(4), -19_000); // 20 -19 = 1
        }).start();
        take();
    }

    @Test
    void testDelayQueue() throws InterruptedException {
        long delay = System.currentTimeMillis() + (10 * 1000);
        ChangeableDelayQueue.PollTask pollTask = new ChangeableDelayQueue.PollTask(delay, 1);
        DelayQueue<ChangeableDelayQueue.PollTask> pollTasks = new DelayQueue<>();
        pollTasks.add(pollTask);
        new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            pollTask.delay += 5000;
        }).start();
        System.out.println(pollTasks.take());
    }
}