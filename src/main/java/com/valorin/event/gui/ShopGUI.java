package com.valorin.event.gui;

import static com.valorin.configuration.DataFile.savepd;
import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;
import static com.valorin.configuration.languagefile.MessageSender.sml;
import static com.valorin.inventory.INVShop.pages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.valorin.Main;
import com.valorin.api.event.ShopEvent;
import com.valorin.caches.PointCache;
import com.valorin.caches.ShopCache;
import com.valorin.commands.sub.CMDShop;
import com.valorin.data.encapsulation.Good;
import com.valorin.inventory.INVShop;
import com.valorin.util.ItemGiver;

public class ShopGUI implements Listener {
	@EventHandler
	public void page(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		String pn = p.getName();
		Inventory inv = e.getInventory();
		if (inv == null) {
			return;
		}
		if (e.getView().getTitle().equals(gm("&0&l积分商城 &9&l[right]", p))) {
			e.setCancelled(true);
			if (e.getRawSlot() == 49) {
				int page = INVShop.pages.get(pn);
				int maxpage = INVShop.getMaxPage();
				if (e.getClick().equals(ClickType.LEFT)) {
					if (page != maxpage) {
						INVShop.loadItem(inv, pn, page + 1);
						pages.put(pn, page + 1);
						INVShop.loadPageItem(inv, pn, page + 1);
					}
				}
				if (e.getClick().equals(ClickType.RIGHT)) {
					if (page != 1) {
						INVShop.loadItem(inv, pn, page - 1);
						pages.put(pn, page - 1);
						INVShop.loadPageItem(inv, pn, page - 1);
					}
				}
			}
			if (e.getRawSlot() > 8 && e.getRawSlot() < 45) // 买东西
			{
				if (inv.getItem(e.getRawSlot()) == null) {
					return;
				}
				int page = INVShop.pages.get(pn);
				int row;
				if ((e.getRawSlot() + 1) % 9 == 0) {
					row = ((e.getRawSlot() + 1) - 9) / 9;
				} else {
					row = ((e.getRawSlot() + 1) - 9) / 9 + 1;
				}
				int column;
				if ((e.getRawSlot() + 1) % 9 == 0) {
					column = 9;
				} else {
					column = (e.getRawSlot() - 9) - (row - 1) * 9 + 1;
				}
				int index = CMDShop.getNum(page, row, column);
				ShopCache shopCache = Main.getInstance().getCacheHandler()
						.getShop();
				PointCache pointCache = Main.getInstance().getCacheHandler()
						.getPoint();
				Good good = shopCache.getList().get(index);
				double price = good.getPrice();
				double points = pointCache.get(pn);
				if (points < price) {
					sm("&c[x]积分余额不足！该商品需要&e{price}&c积分，而你只有&e{points}&c积分", p,
							"price points", new String[] { "" + price,
									"" + points });
					return;
				}
				ItemStack item = good.getItemStack();

				ShopEvent event = new ShopEvent(p, page, row, column, item);
				Bukkit.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled()) { // 购买事件被取消
					return;
				}

				ItemGiver ig = new ItemGiver(p, item);
				if (ig.getIsReceive()) {
					pointCache.set(pn, points - price);
					savepd();
					sml("&7========================================|&a[v]恭喜购买成功，现在你获得了这个道具|&7========================================",
							p);
					if (good.getBroadcast() != null) {
						Bukkit.broadcastMessage(good.getBroadcast()
								.replace("&", "§").replace("_", " ")
								.replace("{player}", p.getName()));
					}
					int num = shopCache.getNumByIndex(index);
					shopCache.updateSalesVolumn(num);
					INVShop.loadInv(pn, inv);
				}
			}
		}
	}
}
