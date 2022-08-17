package cn.loyx.paxossim.simluator.util;

public interface ChangeableDelayed {
    long getDelay();
    void changeDelay(long offset);
}
