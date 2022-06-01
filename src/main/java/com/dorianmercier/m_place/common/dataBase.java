/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dorianmercier.m_place.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Stack;

/**
 *
 * @author doria
 */
public class dataBase {
    
    private static Connection con() {
        try {
            Class.forName("org.postgresql.Driver");
            log.info("Driver postgresql jdbc chargé avec succès");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/m_place", "m_place", "m_place");
        }
        catch(ClassNotFoundException | SQLException e) {
            log.error("Cannot connect to the database : " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public static void disconnect() {
        try {
            if(con != null) con.close();
        } catch (SQLException ex) {
            log.error("Database : Cannot close the connexion to the database");
        }
    }
    
    private static Connection con = con();
    
    private static void addLog(placeEvent place_event) {
        try {
            PreparedStatement st = con.prepareStatement("insert into logs values(DEFAULT, ?, ?, ?, ?, ?, ?)");
            st.setTimestamp(1, place_event.timestamp);
            st.setString(2, place_event.material.toString());
            st.setString(3, place_event.player_uuid.toString());
            st.setInt(4, place_event.location.getBlockX());
            st.setInt(5, place_event.location.getBlockY());
            st.setInt(6, place_event.location.getBlockZ());
            st.execute();
        }
        catch(SQLException e) {
            log.error("Cannot add a log to the database");
            e.printStackTrace();
        }
    }
    
    public static void update_database(boolean final_save) {
        log.info("Updating database...");
        Stack<placeEvent> to_remove_buffer_placements = new Stack<>();
        Stack<placeEvent> copy_buffer = (Stack<placeEvent>) gameConfig.buffer_placements.clone();
        for(placeEvent place_event : copy_buffer) {
            long limit = System.currentTimeMillis() - gameConfig.time_between_pixels;
            if(final_save || place_event.timestamp.getTime() < limit) {
                addLog(place_event);
                to_remove_buffer_placements.push(place_event);
            }
        }
        for(placeEvent place_event : to_remove_buffer_placements) {
            gameConfig.buffer_placements.remove(place_event);
        }
        log.info("...database updated");
        if(final_save) dataBase.disconnect();
    }
}