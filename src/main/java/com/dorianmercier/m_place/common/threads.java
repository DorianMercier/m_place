/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dorianmercier.m_place.common;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author doria
 */
public class threads {
    private static final World world = Bukkit.getWorld("world");
    
    private static void player_manager(main plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                       
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(!gameConfig.admins.contains(player.getUniqueId())) {
                            Location location = player.getLocation();
                            if(Math.abs(location.getBlockX()) > 100 || Math.abs(location.getBlockZ()) > 100 || location.getBlockY() > 100 || location.getBlockY() < -30) {
                                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                       player.teleport(new Location(world, 0, 2, 66));
                                    }
                                });
                                player.sendTitle("Please stay close to the central zone", "There is nothing else to see", 10, 70, 20);
                            }
                        }
                    }
                }
        });
    }
    
    private static void database_manager(main plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    dataBase.update_database(false);
                }
        });
    }
    
    public static void initiate_tasks(main plugin) {
        log.info("Launching background tasks...");
        new BukkitRunnable() {
            @Override
            public void run() {
                database_manager(plugin);
            }
        }.runTaskTimer(plugin, 20L * 5L * 60L /*<-- the initial delay */, 20L * 5L * 60L /*<-- the interval */);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                player_manager(plugin);
            }
        }.runTaskTimer(plugin, 20L * 60L /*<-- the initial delay */, 20L /*<-- the interval */);
        log.info("background tasks launched");
    }
    
}
