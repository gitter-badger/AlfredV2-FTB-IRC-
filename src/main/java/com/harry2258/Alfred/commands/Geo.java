package com.harry2258.Alfred.commands;

import com.google.gson.JsonObject;
import com.harry2258.Alfred.api.*;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.WhoisEvent;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Geo extends Command {

    public Geo() {
        super("Geo", "Geolocate an IP address!");
    }

    @Override
    public boolean execute(MessageEvent event) throws Exception {
        String[] args = event.getMessage().split(" ");
        String info = "";
        String ip = args[1];

        if (event.getChannel().isChannelPrivate() || event.getChannel().isSecret() || event.getChannel().isInviteOnly()) {
            event.getChannel().send().message("No no no! Not in here!");
            return true;
        }

        if (args.length == 3 && PermissionManager.hasExec(event.getUser().getNick()) && args[1].equals("exec")) {
            if (event.getChannel().getUsers().toString().contains(args[2])) {
                User u = event.getBot().getUserChannelDao().getUser(args[2]);
                event.getBot().sendRaw().rawLineNow("WHOIS " + u.getNick());
                WaitForQueue waitForQueue = new WaitForQueue(event.getBot());
                WhoisEvent test;
                try {
                    test = waitForQueue.waitFor(WhoisEvent.class);
                    waitForQueue.close();
                    if (Utils.ValidIP(test.getHostname()))
                        ip = test.getHostname();
                    else
                        ip = java.net.InetAddress.getByName(test.getHostname()).getHostAddress();
                } catch (InterruptedException ex) {
                    Logger.getLogger(com.harry2258.Alfred.listeners.MessageEvent.class.getName()).log(Level.SEVERE, null, ex);

                }
            } else {
                if (Utils.ValidIP(args[2]))
                    ip = args[2];
                else
                    ip = java.net.InetAddress.getByName(args[2]).getHostAddress();
            }

            String geo = "http://freegeoip.net/json/" + ip;
            try {
                URL url;
                url = new URL(geo);
                BufferedReader re = new BufferedReader(new InputStreamReader(url.openStream()));
                String jsonstring = re.readLine();
                JsonObject jsonObj = JsonUtils.getJsonObject(jsonstring);
                info += Colors.BOLD + "City: " + Colors.NORMAL + jsonObj.get("city").getAsString() + " | \t";
                info += Colors.BOLD + "Zip: " + Colors.NORMAL + jsonObj.get("zipcode").getAsString() + " | \t";
                info += Colors.BOLD + "State: " + Colors.NORMAL + jsonObj.get("region_name").getAsString() + " | \t";
                info += Colors.BOLD + "Country: " + Colors.NORMAL + jsonObj.get("country_name").getAsString() + " | \t";
                info += Colors.BOLD + "Coords: " + Colors.NORMAL + jsonObj.get("latitude").getAsString() + " " + jsonObj.get("longitude").getAsString();

                event.getUser().send().message(info);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                if (e.getMessage().contains("400")) {
                    event.getChannel().send().message("IPV6 aren't supported yet!");
                }
                return false;
            }
        }
        if (event.getChannel().getUsers().toString().contains(args[1])) {
            User u = event.getBot().getUserChannelDao().getUser(args[1]);
            event.getBot().sendRaw().rawLineNow("WHOIS " + u.getNick());
            WaitForQueue waitForQueue = new WaitForQueue(event.getBot());
            WhoisEvent test;
            try {
                test = waitForQueue.waitFor(WhoisEvent.class);
                waitForQueue.close();
                if (Utils.ValidIP(test.getHostname()))
                    ip = test.getHostname();
                else
                    ip = java.net.InetAddress.getByName(test.getHostname()).getHostAddress();
            } catch (InterruptedException ex) {
                Logger.getLogger(com.harry2258.Alfred.listeners.MessageEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (Utils.ValidIP(args[2]))
                ip = args[2];
            else
                ip = java.net.InetAddress.getByName(args[2]).getHostAddress();
        }

        String geo = "http://freegeoip.net/json/" + ip;
        String jsonstring = "";
        try {
            URL url;
            url = new URL(geo);
            BufferedReader re = new BufferedReader(new InputStreamReader(url.openStream()));
            jsonstring = re.readLine();
            JSONObject jsonObj = new JSONObject(jsonstring);
            info += Colors.BOLD + "State: " + Colors.NORMAL + jsonObj.getString("region_name") + " | \t";
            info += Colors.BOLD + "Country: " + Colors.NORMAL + jsonObj.getString("country_name") + " | \t";
            info += Colors.BOLD + "Coords: " + Colors.NORMAL + jsonObj.getString("latitude").replaceAll("(?:\\.).*", "") + " " + jsonObj.getString("longitude").replaceAll("(?:\\.).*", "");

            event.getChannel().send().message(info);
        } catch (Exception e) {
            e.printStackTrace();
            if (jsonstring.contains("Not Found")) {
                event.getChannel().send().message("IPV6 aren't supported yet!");
            }
            return false;
        }
        return true;
    }

    @Override
    public void setConfig(Config config) {

    }

    @Override
    public void setManager(PermissionManager manager) {

    }
}
