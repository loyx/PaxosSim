package cn.loyx.paxossim.gui.comm;

import cn.loyx.paxossim.gui.component.PacketComponent;
import cn.loyx.paxossim.gui.component.SiteComponent;

import java.util.LinkedList;
import java.util.List;

public class SimBus {
    private static SimBus instance = null;
    private SimBus(){}

    public static SimBus getInstance() {
        if (instance == null){
            instance = new SimBus();
        }
        return instance;
    }

    private final List<EventListener> eventListeners = new LinkedList<>();

    public void addEventListener(EventListener listener){
        eventListeners.add(listener);
    }

    public void selectPacket(PacketComponent packetComponent){
        eventListeners.forEach(listener -> listener.packetSelected(packetComponent));
    }

    public void selectSite(SiteComponent siteComponent){
        eventListeners.forEach(listener -> listener.siteSelected(siteComponent));
    }

}
