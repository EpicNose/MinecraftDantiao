package com.valorin.teleport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.valorin.Main;

public class ToLobby {
	public static void to(Player p) {
		if (Main.getInstance().getCacheHandler().getArea().getLobby() == null) {
			return;
		}
		if (p == null) {
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(p, "dt lobby");
			}
		}.runTask(Main.getInstance());
	}
}
