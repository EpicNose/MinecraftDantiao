package com.valorin.event.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import com.valorin.Main;
import com.valorin.arenas.StartGame;
import com.valorin.inventory.special.INVStart;
import com.valorin.inventory.special.StartInvHolder;
import com.valorin.queue.SearchingQueue;

public class StartGUI implements Listener {
	@EventHandler
	public void page(InventoryClickEvent e) {
		Player opener = (Player) e.getWhoClicked();
		String opener_name = opener.getName();
		Inventory inventory = e.getInventory();
		if (inventory == null) {
			return;
		}
		if (inventory.getHolder() == null) {
			return;
		}
		if (!(inventory.getHolder() instanceof StartInvHolder)) {
			return;
		}
		StartInvHolder holder = (StartInvHolder) inventory.getHolder();
		e.setCancelled(true);
		if (e.getRawSlot() != 13) {
			return;
		}
		if (holder.getStatus() == 0) {
			SearchingQueue queue = Main.getInstance().getSearchingQueue();
			String waiter_name = queue.getWaiter();
			if (waiter_name == null) {
				queue.setWaiter(opener_name);
				INVStart.startSearch(holder);
			} else {
				queue.setWaiter(null);
				Player waiter = Bukkit.getPlayerExact(waiter_name);
				new StartGame(opener, waiter, null, null, 1);
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Inventory inventory = e.getInventory();
		if (inventory == null) {
			return;
		}
		if (inventory.getHolder() == null) {
			return;
		}
		if (!(inventory.getHolder() instanceof StartInvHolder)) {
			return;
		}
		StartInvHolder holder = (StartInvHolder) inventory.getHolder();
		SearchingQueue queue = Main.getInstance().getSearchingQueue();
		if (holder.getStatus() == 1) {
			queue.setWaiter(null);
			INVStart.finishSearch(holder, true);
		}
	}
}
