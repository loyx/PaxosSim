package cn.loyx.paxossim.sim.util;

public interface ChangeableDelayed {
    long getDelay();
    void changeDelay(long offset);
}
