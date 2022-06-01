/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dorianmercier.m_place.common;

import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author doria
 */
public class mPlaceCommandExecutor implements CommandExecutor {
    private final main plugin;

    public mPlaceCommandExecutor(main plugin) {
            this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        Player player = (Player) cs;
        if(cmnd.getName().equalsIgnoreCase("admin")) {
            String value = strings[0];
            UUID uuid = player.getUniqueId();
            if(value.equalsIgnoreCase("on")) {
                if(gameConfig.admins.contains(uuid)) {
                    player.sendMessage("You already are in admin mode");
                    return true;
                }
                else {
                    gameConfig.admins.add(uuid);
                    player.sendMessage("You are now in admin mode. You can place and break blocks wherever you want");
                    return true;
                }
            }
            if(value.equalsIgnoreCase("off")) {
                if(gameConfig.admins.contains(uuid)) {
                    gameConfig.admins.remove(uuid);
                    player.sendMessage("You are not in admin mode anymore");
                    return true;
                }
                else {
                    player.sendMessage("You are not in admin mode");
                    return true;
                }
            }
        }
        return false;
    }
}
