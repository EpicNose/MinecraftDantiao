package com.valorin.event;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.valorin.Main;
import com.valorin.arenas.ArenaCreator;
import com.valorin.itemstack.PlayerItems;
import com.valorin.util.ViaVersion;

public class ArenaCreate implements Listener {
	@EventHandler
	public void selectPoint(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		String creatorname = p.getName();
		ItemStack item = ViaVersion.getItemInMainHand(p);
		if (item == null) {
			return;
		}
		if (item.getType().equals(Material.AIR)) {
			return;
		}
		if (!item.hasItemMeta()) {
			return;
		}
		if (item.getItemMeta().getLore() != null) {
			if (item.getItemMeta().getLore().get(0)
					.equals(PlayerItems.mark1)) {
				if (!p.hasPermission("dt.admin")) {
					sm("&c[x]无权限！", p);
					return;
				}
				if (!Main.getInstance().getACS().getCreators()
						.contains(creatorname)) {
					sm("&c[x]请输入/dt a mode进入竞技场创建模式后再使用这个快捷创建工具！", p);
					return;
				}
				e.setCancelled(true);
				ArenaCreator ac = Main.getInstance().getACS()
						.getAC(creatorname);
				Action action = e.getAction();
				if (action.equals(Action.LEFT_CLICK_AIR)) {
					if (ac.getPointB() != null) {
						if (!ac.getPointB().getWorld().equals(p.getWorld())) {
							sm("&c[x]两点必须处于同一世界！", p);
							return;
						}
						ac.setPointA(p.getLocation());
						sm("&a[v]A点设定完毕", p);
						return;
					}
					ac.setPointA(p.getLocation());
					sm("&a[v]A点设定完毕", p);
				}
				if (action.equals(Action.RIGHT_CLICK_AIR)) {
					if (ac.getPointA() != null) {
						if (!ac.getPointA().getWorld().equals(p.getWorld())) {
							sm("&c[x]两点必须处于同一世界！", p);
							return;
						}
						ac.setPointB(p.getLocation());
						sm("&a[v]B点设定完毕", p);
						return;
					}
					ac.setPointB(p.getLocation());
					sm("&a[v]B点设定完毕", p);
				}
				if (action.equals(Action.LEFT_CLICK_BLOCK)
						|| action.equals(Action.RIGHT_CLICK_BLOCK)) {
					sm("&c[x]竞技场创建方式错误！请将创建工具拿在手上后点击空气，以将你当前所处的位置作为传送点！", p);
				}
			}
		}
	}
}
