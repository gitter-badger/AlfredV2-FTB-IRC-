package com.harry2258.Alfred.listeners;

import com.harry2258.Alfred.Main;
import com.harry2258.Alfred.api.Config;
import com.harry2258.Alfred.api.PermissionManager;
import org.pircbotx.hooks.ListenerAdapter;

/**
 * Created by Hardik on 1/19/14.
 */
public class ActionEvent extends ListenerAdapter {

    private Config config;
    private PermissionManager manager;

    public ActionEvent(Config conf, PermissionManager man) {
        this.config = conf;
        this.manager = man;
    }

    public void onNickChange(org.pircbotx.hooks.events.ActionEvent event) throws Exception {
        if (Main.relay.containsKey(event.getChannel())) {
            Main.relay.get(event.getChannel()).send().message("[" + event.getChannel().getName() + "] " + event.getUser().getNick() + " " + event.getAction());
        }
    }

}
