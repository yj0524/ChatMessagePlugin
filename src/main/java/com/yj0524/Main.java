package com.yj0524;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin implements Listener {

    public String chatMessage;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin Enabled");

        // Event Listener 등록
        getServer().getPluginManager().registerEvents(this, this);

        // Config.yml 파일 생성
        loadConfig();
        File cfile = new File(getDataFolder(), "config.yml");
        if (cfile.length() == 0) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin Disabled");
    }

    private void loadConfig() {
        // Load chest size from config
        FileConfiguration config = getConfig();
        chatMessage = config.getString("chatMessage", "&a%player%&r : &b%message%&r");
        // Save config
        config.set("chatMessage", chatMessage);
        saveConfig();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String msg = event.getMessage();
        msg = msg.replace("&", "§");
        String formattedMessage = chatMessage.replace("%player%", p.getName()).replace("%message%", msg).replace("&", "§");
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            event.setCancelled(true);
            allPlayers.sendMessage(formattedMessage);
            Bukkit.getConsoleSender().sendMessage(formattedMessage);
        }
    }
}
