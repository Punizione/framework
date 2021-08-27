package com.delitto.izumo.framework.base.plugin;



import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;

public interface PluginBase<T>  {
    String getInfo();
    boolean execute(T commands, Contact eventSubject, User sender);
    void execute(@NotNull MessageEvent event);
}
