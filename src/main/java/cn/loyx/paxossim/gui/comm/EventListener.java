package cn.loyx.paxossim.gui.comm;

import cn.loyx.paxossim.gui.component.PacketComponent;
import cn.loyx.paxossim.gui.component.SiteComponent;

public interface EventListener {
    default void packetSelected(PacketComponent packetComponent){}
    default void siteSelected(SiteComponent siteComponent){}
}
