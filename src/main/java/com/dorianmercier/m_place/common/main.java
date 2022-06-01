/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dorianmercier.m_place.common;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author doria
 */
public class main extends JavaPlugin {
    private eventsListener event_listener;
    dataBase database;
    @Override
    public void onEnable() {
        event_listener = new eventsListener(this);
        database = new dataBase();
        
        this.getCommand("admin").setExecutor(new mPlaceCommandExecutor(this));
        
        gameConfig.LIST_BLOCKS.add(Material.WHITE_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.ORANGE_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.MAGENTA_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.LIGHT_BLUE_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.YELLOW_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.LIME_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.PINK_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.GRAY_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.LIGHT_GRAY_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.CYAN_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.PURPLE_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.BLUE_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.BROWN_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.GREEN_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.RED_WOOL);
        gameConfig.LIST_BLOCKS.add(Material.BLACK_WOOL);
        
        threads.initiate_tasks(this);
        
        
        
    }
    @Override
    public void onDisable() {
        dataBase.update_database(true);
    }
}
