package com.valorin.data.encapsulation;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DataMedium implements Serializable {
	private static final long serialVersionUID = 8667303456576664751L;
	public String location_worldName;
    public double location_x;
    public double location_y;
    public double location_z;
    public float location_pitch;
    public float location_yaw;
    
    public void setLocation(Location location) {
    	location_worldName = location.getWorld().getName();
    	location_x = location.getX();
    	location_y = location.getY();
    	location_z = location.getZ();
    	location_yaw = location.getYaw();
    	location_pitch = location.getPitch();
    }
    
    public Location getSimpleLocation() {
    	return new Location(
    			Bukkit.getWorld(location_worldName),
    			location_x,location_y,location_z);
    }
    
    public Location getLocation() {
    	return new Location(
    			Bukkit.getWorld(location_worldName),
    			location_x,location_y,location_z,location_yaw,location_pitch);
    }
}
