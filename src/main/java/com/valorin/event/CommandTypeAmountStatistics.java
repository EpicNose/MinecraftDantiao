package com.valorin.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.valorin.Main;

public class CommandTypeAmountStatistics implements Listener {
	@EventHandler
	public void onCommandTypeAmountStatistics(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().startsWith("/dt ") || e.getMessage().equals("/dt")) {
			Main.getInstance().getSingleLineChartData().addTypeCommandTimes();
		}
	}
}
