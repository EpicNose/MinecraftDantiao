package com.valorin.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerSet {
     public static List<String> get() {
    	 List<String> nameList = new ArrayList<String>();
    	 for (Player onlinePlayer : ViaVersion.getOnlinePlayers()) {
    		 nameList.add(onlinePlayer.getName());
    	 }
    	 for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
    		 nameList.add(offlinePlayer.getName());
    	 }
    	 return nameList;
     }
}
