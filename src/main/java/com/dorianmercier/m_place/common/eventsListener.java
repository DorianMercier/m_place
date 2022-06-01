/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dorianmercier.m_place.common;

import java.sql.Timestamp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author doria
 */
public class eventsListener implements Listener {
    
    private static final World world = Bukkit.getWorld("world");
    
    public eventsListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public static void onPlaceBlock(final BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if(!gameConfig.admins.contains(player.getUniqueId())) {
            Block block = e.getBlockPlaced();
            Location location = block.getLocation();
            int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
            if(z > 63 || x > 63 || z < -64 || x < -64 || y < 1) {
                e.setCancelled(true);
            }
            else {
                //Check if the placed block is an authorized material
                Material material = block.getType();
                if(!gameConfig.LIST_BLOCKS.contains(material)) {
                    e.setCancelled(true);
                    return;
                }
                //Check if the block placed is not floating
                Block block_under = world.getBlockAt(x, y-1, z);
                Material material_under = block_under.getType();
                if(!material_under.equals(Material.SNOW_BLOCK) && !gameConfig.LIST_BLOCKS.contains(material_under)) {
                    e.setCancelled(true);
                    return;
                }
                //Check if the block under the block placed is not too young (5 minutes)
                if(check_block_buffer(block_under.getLocation())) {
                    player.sendMessage("You cannot cover this block yet. Blocks can be covered only after 5 minutes");
                    e.setCancelled(true);
                    return;
                }
                placeEvent place_event = new placeEvent(player.getUniqueId(), material, location, new Timestamp(System.currentTimeMillis()));
                gameConfig.buffer_placements.push(place_event);
            }
        }
    }
    
    @EventHandler
    public static void onBreakBlock(final BlockBreakEvent e) {
        Player player = e.getPlayer();
        if(!gameConfig.admins.contains(player.getUniqueId())) {
            Location location = e.getBlock().getLocation();
            int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
            
            //Prevent block breaking outside the zone
            if(z > 63 || x > 63 || z < -64 || x < -64 || y < 1) {
                e.setCancelled(true);
                return;
            }
            
            //A player can only break blocks placed by himself less than 5 minutes ago            
            long limit = System.currentTimeMillis() - gameConfig.time_between_pixels;
            for(placeEvent place_event : gameConfig.buffer_placements) {
                if(place_event.location.equals(location)) {
                    if(!(place_event.timestamp.getTime() > limit && place_event.player_uuid.equals(player.getUniqueId()))) {
                        e.setCancelled(true);
                        player.sendMessage("You cannot break any block");
                    }
                    else {
                        gameConfig.buffer_placements.remove(place_event);
                    }
                    return;
                }
            }
            e.setCancelled(true);
            player.sendMessage("You cannot break any block");
        }
    }
    
    @EventHandler
    public static void onEmptyBucket(PlayerBucketEmptyEvent e) {
        Player player = e.getPlayer();
        if(!gameConfig.admins.contains(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onFillBucket(PlayerBucketFillEvent e) {
        Player player = e.getPlayer();
        if(!gameConfig.admins.contains(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onClickEntity(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        if(!gameConfig.admins.contains(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onEntityDamagingEntity(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if(!gameConfig.admins.contains(player.getUniqueId())) {
            e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public static void onPlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(!gameConfig.admins.contains(player.getUniqueId())) {
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Material holding = player.getInventory().getItemInMainHand().getType();
                if(!gameConfig.LIST_BLOCKS.contains(holding)) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    private static boolean check_block_buffer(Location location) {
        long limit = System.currentTimeMillis() - gameConfig.time_between_pixels;
        for(placeEvent place_event : gameConfig.buffer_placements) {
            if(place_event.location.equals(location)) {
                return(place_event.timestamp.getTime() > limit);
            }
        }
        return false;
    }
}
