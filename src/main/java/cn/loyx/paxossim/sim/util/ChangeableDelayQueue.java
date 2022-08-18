package cn.loyx.paxossim.sim.util;

import lombok.extern.log4j.Log4j;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Log4j
public class ChangeableDelayQueue<E extends ChangeableDelayed> {

    static class PollTask implements Delayed {
        public long delay;
        private final int taskHashCode;

        PollTask(long delay, int taskHashCode) {
            this.delay = delay;
            this.taskHashCode = taskHashCode;
        }

        public int getTaskHashCode() {
            return taskHashCode;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return delay - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "pollTask{" +
                    "delay=" + getDelay(TimeUnit.MILLISECONDS) +
                    "ms, taskHashCode=" + taskHashCode +
                    '}';
        }
    }
    private final Map<Integer,E> elements = new HashMap<>();
    private final DelayQueue<PollTask> pollTaskQueue = new DelayQueue<>();


    public void put(E e) {
        elements.put(e.hashCode(), e);
        PollTask task = new PollTask(e.getDelay(), e.hashCode());
//        log.debug("queue put a task: " + task);
        pollTaskQueue.put(task);
    }

    public E take() throws InterruptedException {
        while(true){
            PollTask task = pollTaskQueue.take();
//            log.debug("queue take a task: " + task);
            E e = elements.getOrDefault(task.getTaskHashCode(), null);
            if (e!=null && e.getDelay() <= System.currentTimeMillis()){
                elements.remove(e.hashCode());
                return e;
            }
        }
    }

    public void changeDelay(int hashCode, long offset){
        E e = elements.getOrDefault(hashCode, null);
        if (e != null){
            e.changeDelay(offset);
            pollTaskQueue.put(new PollTask(e.getDelay(), e.hashCode()));
        }
    }

    public void changeDelay(E element, long offset){
        this.changeDelay(element.hashCode(), offset);
    }

    public void cancelTask(E element){
        elements.remove(element.hashCode());
    }


}
