package com.valorin.itemstack;

import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.gml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.valorin.Main;
import com.valorin.caches.ArenaInfoCache;
import com.valorin.caches.PointCache;
import com.valorin.caches.RecordCache;
import com.valorin.data.encapsulation.Good;
import com.valorin.data.encapsulation.Record;
import com.valorin.inventory.special.StartInvHolder;
import com.valorin.specialtext.Dec;
import com.valorin.util.ItemCreator;
import com.valorin.util.ViaVersion;

public class GUIItems {

	public static ItemStack getPage(String pn, int page, int maxpage) {// records和shop通用
		Player p = Bukkit.getPlayerExact(pn);
		return new ItemCreator(ViaVersion.getMapMaterial(), gm(
				"&f当前页码：{page}/{maxpage}", p, "page maxpage", new String[] {
						"" + page, "" + maxpage }),
				gml(" |&9<左键下一页/右键上一页| ", p), 0, true).get();
	}

	public static ItemStack getStart(String pn) {
		Player p = Bukkit.getPlayerExact(pn);
		String displayname = gm("&f<[ 全服匹配 &f]>", p);
		List<String> lore = new ArrayList<String>();
		lore.addAll(gml(" |&e在线寻找对手|&f&l>> &a点击开始", p));
		return new ItemCreator(ViaVersion.getGoldenAxeMaterial(), displayname, lore,
				0, false).get();
	}

	public static ItemStack updataStart(StartInvHolder holder) {
		String opener_name = holder.getOpenerName();
		Player opener = Bukkit.getPlayerExact(opener_name);
		String displayname = gm("&f<[ &7全服匹配 &f]>", opener);
		List<String> lore = new ArrayList<String>();
		int second = holder.getSec();
		lore.addAll(gml(" |&7在线寻找对手|&f&l>> &6搜寻中..{second}s", opener, "second",
				new String[] { second + "" }));

		return new ItemCreator(ViaVersion.getGoldenAxeMaterial(), displayname, lore,
				0, true).get();
	}

	public static ItemStack getRecords(String pn, int num) {
		Player p = Bukkit.getPlayerExact(pn);
		String displayname = gm("&a作战编号 &2#{num}", p, "num", new String[] { ""
				+ num });
		List<String> lore = new ArrayList<String>();

		RecordCache recordCache = Main.getInstance().getCacheHandler()
				.getRecord();
		ArenaInfoCache arenaInfoCache = Main.getInstance().getCacheHandler()
				.getArenaInfo();
		Record record = recordCache.getRecords(pn).get(num);

		String result = null;
		if (record.getResult() == 0) {
			result = "&a[v]胜利";
		}
		if (record.getResult() == 1) {
			result = "&c[x]败北";
		}
		if (record.getResult() == 2) {
			result = "&6[=]平局";
		}

		String server = gm("&7&m未记录&r", p);;
		if (record.getServerName() != null) {
			server = record.getServerName().replace("&", "§");
		}

		String damage = gm("&7&m未记录&r", p);
		if (record.getDamage() != 0) {
			BigDecimal bg = new BigDecimal(record.getDamage());
			damage = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
					+ "";
		}

		String maxDamage = gm("&7&m未记录&r", p);
		if (record.getMaxDamage() != 0) {
			BigDecimal bg = new BigDecimal(record.getMaxDamage());
			maxDamage = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
					+ "";
		}

		String arenaName = gm("&7&m未记录&r", p);
		if (record.getArenaEditName() != null) {
			String editName = record.getArenaEditName();
			if (arenaInfoCache.get(editName) != null) {
				if (arenaInfoCache.get(editName).getDisplayName() != null) {
					arenaName = arenaInfoCache.get(editName).getDisplayName()
							.replace("&", "§");
				} else {
					arenaName = editName;
				}
			}
		}

		String expChangeStr = gm("&7&m未记录&r", p);
		if (record.getExpChange() != 0) {
			int expChange = record.getExpChange();
			if (expChange > 0) {
				expChangeStr = gm("获得", p) + expChange;
			} else {
				expChangeStr = gm("损失", p) + (0 - expChange);
			}
		}


		String startWayStr = gm("&7&m未记录&r", p);
		int startWay = record.getStartWay();
		if (startWay == 1) {
			startWayStr = gm("匹配赛", p);
		}
		if (startWay == 2) {
			startWayStr = gm("邀请赛", p);
		}
		if (startWay == 3) {
			startWayStr = gm("强制赛", p);
		}

		String result_string = gm(result, p);
		try {
			lore = gml(
					" |&b结果 &f[right] {result}|&b对手 &f[right] &e{opponent}|&b地图 &f[right] &e{arena}|&b日期 &f[right] &e{date}|&b耗时 &f[right] &e{time}秒|&b经验 &f[right] &e{exp}|&b类型 &f[right] &e{type}|&b伤害输出  &f[right] &e{damage}|&b最大输出  &f[right] &e{maxdamage}|&b对战区服  &f[right] &e{server}| ",
					p,
					"result opponent arena date time exp type damage maxdamage server",
					new String[] { result_string, record.getOpponentName(),
							arenaName, record.getDate(), record.getTime() + "",
							expChangeStr, startWayStr, damage, maxDamage,
							server });
			lore.add("");
			lore.add(gm("&f[right] &b左键&7将本战绩分享到聊天框", p));
			lore.add(gm("&f[right] &d右键&7将本战绩打印到纸上", p));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ItemCreator(Material.PAPER, displayname, lore, 0, false)
				.get();
	}

	public static ItemStack getGoodShowed(String pn, Good good) {
		Player p = Bukkit.getPlayerExact(pn);

		ItemStack item = good.getItemStack();
		double price = good.getPrice();
		String description = good.getDescription();
		int salesVolumn = good.getSalesVolumn();

		List<String> lore = new ArrayList<String>();
		String dec = Dec.getStr(5);
		lore.add(dec);
		lore.add(gm("&b商品Lore &f&l>>", p));
		boolean haslore = true;
		if (item.hasItemMeta()) {
			if (item.getItemMeta().getLore() != null) {
				lore.addAll(item.getItemMeta().getLore());
			} else {
				haslore = false;
			}
		} else {
			haslore = false;
		}
		if (!haslore) {
			lore.add(gm("&7无", p));
		}
		lore.add(dec);
		lore.add(gm("&b商品信息 &f&l>>", p));

		if (description != null) {
			lore.add(gm("&6备注： ", p)
					+ description.replace("&", "§").replace("{player}", pn)
							.replace("_", " "));
		} else {
			lore.add(gm("&6备注： &7无", p));
		}

		lore.add(gm("&6销量： &e{volumn}", p, "volumn", new String[] { ""
				+ salesVolumn }));

		lore.add(gm("&6价格： &e{price}", p, "price", new String[] { "" + price }));
		PointCache pointCache = Main.getInstance().getCacheHandler().getPoint();
		double points = pointCache.get(pn);

		lore.add(gm("&6余额： &e{points}", p, "points",
				new String[] { "" + points }));

		lore.add(dec);
		lore.add("");

		if (points >= price) {
			lore.add(gm("&a[v]点击购买", p));
		} else {
			lore.add(gm("&c[x]余额不足", p));
		}

		ItemStack itemShowed = new ItemStack(item);
		ItemMeta im = itemShowed.getItemMeta();
		im.setLore(lore);
		itemShowed.setItemMeta(im);

		return itemShowed;
	}
}
