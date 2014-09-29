package com.harry2258.Alfred.commands;

import com.harry2258.Alfred.Main;
import com.harry2258.Alfred.api.*;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.File;
import java.sql.PreparedStatement;

/**
 * Created by Hardik at 11:56 AM on 8/7/2014.
 */
public class Transfer extends Command {

    private Config config;
    private PermissionManager manager;

    public Transfer() {
        super("Transfer", "Using this command to transfer ALL your permissions to a database.", "Transfer");
    }

    @Override
    public boolean execute(MessageEvent event) throws Exception {
        if (event.getMessage().split(" ").length == 2 && event.getMessage().split(" ")[1].equalsIgnoreCase("nuke")) {
            Main.database.prepareStatement("TRUNCATE Channel_Permissions").execute();
        }
        File all = new File("perms/");
        for (final File file : all.listFiles()) {
            try {
                if (file.isDirectory()) {
                    System.out.println("MOVING PERMISSIONS FOR CHANNEL: " + file.getName());
                    for (File tmp : file.listFiles()) {
                    /*
                        String perms = JsonUtils.getStringFromFile(tmp.getPath());
                        Perms P = JsonUtils.getPermsFromString(perms);
                        Permission p = P.getPermission();
                        String Mods = p.getMods().toString().replaceAll("\\]|\\[", "");
                        String Admins = p.getAdmins().toString().replaceAll("\\]|\\[", "");
                        String Everyone = p.getEveryone().toString().replaceAll("\\]|\\[", "");
                        String ModPerms = p.getModPerms().toString().replaceAll("\\]|\\[", "");
                        Main.database.prepareStatement("INSERT IGNORE INTO `Channel_Permissions` (`Channel`, `Admins`, `Mods`, `ModPerms`, `Everyone`, `URL`) VALUES ( '" + file.getName() + "', '" + Admins + "', '" + Mods + "', '" + ModPerms + "', '" + Everyone + "', 'none')").execute();
                    */
                        PreparedStatement stmt = Main.database.prepareStatement("INSERT IGNORE INTO `Channel_Permissions` (`Channel`, `Permission`, `URL`) VALUES (?, ?, 'none')");
                        stmt.setString(1, file.getName());
                        stmt.setString(2, JsonUtils.getStringFromFile(tmp.getPath()));
                        stmt.execute();
                    }
                    //event.getChannel().send().message("Added permissions to database for channel " + file.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Utils.ReloadDatabase()) {
            event.getUser().send().message("Done transferring permissions to database!");
            return true;
        } else {
            return false;
        }
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
