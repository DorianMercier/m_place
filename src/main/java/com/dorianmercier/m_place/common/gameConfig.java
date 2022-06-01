/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.m_place.common;

import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;
import org.bukkit.Material;



/**
 *
 * @author doria
 */
public class gameConfig {
    
    public static final String VERSION = "v1.0";
    //Only blocks allowed to color the map
    public static ArrayList<Material> LIST_BLOCKS = new ArrayList();
    public static Stack<placeEvent> buffer_placements = new Stack<>();
    public static ArrayList<UUID> admins = new ArrayList<>();
    public static long time_between_pixels = 5*60*1000;
    
}
