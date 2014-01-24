package com.harry2258.Alfred.commands;

import com.harry2258.Alfred.api.Command;
import com.harry2258.Alfred.api.Config;
import com.harry2258.Alfred.api.PermissionManager;
import com.harry2258.Alfred.api.Utils;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by Hardik on 1/21/14.
 */
public class Fishbans extends Command {

    private Config config;
    private PermissionManager manager;

    public Fishbans() {
        super("Fishbans", "Check if the user has any McBans.", "Fishbans [user]");
    }

    @Override
    public boolean execute(MessageEvent event) throws Exception {
        String[] args = event.getMessage().split(" ");
        event.getChannel().send().message(Utils.McBans(args[1]));
        return true;
    }

    @Override
    public void setConfig(Config config) {
this.config = config;
    }

    @Override
    public void setManager(PermissionManager manager) {
this.manager = manager;
    }
}
