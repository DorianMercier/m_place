/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dorianmercier.m_place.common;

import java.sql.Timestamp;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author doria
 */
public class placeEvent {
    public Material material;
    public UUID player_uuid;
    public Location location;
    public Timestamp timestamp;
    
    public placeEvent(UUID player_uuid, Material material, Location location, Timestamp timestamp) {
        this.player_uuid = player_uuid;
        this.material = material;
        this.location = location;
        this.timestamp = timestamp;
    }
}
