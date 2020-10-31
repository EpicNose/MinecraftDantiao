package com.valorin.papi;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.gm;

import java.math.BigDecimal;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.caches.ArenaInfoCache;
import com.valorin.caches.CacheHandler;
import com.valorin.caches.EnergyCache;
import com.valorin.caches.PointCache;
import com.valorin.caches.RecordCache;
import com.valorin.dan.DanHandler;
import com.valorin.data.encapsulation.Record;
import com.valorin.ranking.Ranking;
import com.valorin.request.RequestsHandler;

public class RegPAPI extends PlaceholderHook {
	private static final String HOOK_NAME = "dantiao";

	@Override
	public String onPlaceholderRequest(Player p, String i) {
		if (p == null) {
			return null;
		}
		CacheHandler cacheHandler = Main.getInstance().getCacheHandler();
		Ranking ranking = Main.getInstance().getRanking();
		ArenaInfoCache arenaInfoCache = cacheHandler.getArenaInfo();
		EnergyCache energyCache = cacheHandler.getEnergy();
		PointCache pointCache = cacheHandler.getPoint();
		RecordCache recordCache = cacheHandler.getRecord();
		String name = p.getName();
		if (i.equalsIgnoreCase("points")) {
			return "" + pointCache.get(name);
		}
		if (i.equalsIgnoreCase("win")) {
			return "" + recordCache.getWins(name);
		}
		if (i.equalsIgnoreCase("lose")) {
			return "" + recordCache.getLoses(name);
		}
		if (i.equalsIgnoreCase("draw")) {
			return "" + recordCache.getDraws(name);
		}
		if (i.equalsIgnoreCase("total")) {
			int winTime = recordCache.getWins(name);
			int loseTime = recordCache.getLoses(name);
			int drawTime = recordCache.getDraws(name);
			int totalTime = winTime + loseTime + drawTime;
			return "" + totalTime;
		}
		if (i.equalsIgnoreCase("isWin")) {
			int times = recordCache.getGameTimes(name);
			if (times == 0) {
				return gm("&7无数据", p);
			}
			try {
				Record record = recordCache.getRecords(name).get(times - 1);
				if (record.getResult() == 0) {
					return gm("&a[v]胜利", p);
				}
				if (record.getResult() == 1) {
					return gm("&c[x]败北", p);
				}
				if (record.getResult() == 2) {
					return gm("&6[=]平局", p);
				}
			} catch (Exception e) {
				return "Loading...";
			}
		}
		if (i.equalsIgnoreCase("energy")) {
			if (energyCache.isEnable()) {
				BigDecimal bg = new BigDecimal(energyCache.get(name));
				double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				return "" + value;
			}
		}
		if (i.equalsIgnoreCase("maxenergy")) {
			if (energyCache.isEnable()) {
				return "" + energyCache.getMaxEnergy();
			}
		}
		if (i.equalsIgnoreCase("kd")) {
			int wins = recordCache.getWins(name);
			int loses = recordCache.getLoses(name);
			if (loses == 0) {
				return "" + wins;
			} else {
				BigDecimal bg = new BigDecimal((double) wins / (double) loses);
				double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				return "" + value;
			}
		}
		if (i.equalsIgnoreCase("winrank")) {
			return "" + ranking.getWin(p);
		}
		if (i.equalsIgnoreCase("kdrank")) {
			return "" + ranking.getKD(p);
		}
		if (i.equalsIgnoreCase("isInvited")) {
			RequestsHandler request = getInstance().getRequestsHandler();
			if (request.getSenders(p.getName()).size() != 0) {
				return gm("&a{amount}条", p, "amount", new String[] { request
						.getSenders(p.getName()).size() + "" });
			} else {
				return gm("&7暂无", p);
			}
		}
		DanHandler dh = getInstance().getDanHandler();
		if (i.equalsIgnoreCase("duanwei")) {
			String danDisplayName;
			if (dh.getPlayerDan(p.getName()) == null) {
				danDisplayName = getInstance().getConfigManager()
						.getInitialDanName();
			} else {
				danDisplayName = dh.getPlayerDan(p.getName()).getDisplayName();
			}
			return danDisplayName;
		}
		if (i.equalsIgnoreCase("exptolevelup")) {
			return "" + dh.getNeedExpToLevelUp(p.getName());
		}
		Record lastRecord = recordCache.getLast(name);
		if (i.equalsIgnoreCase("lastdamage")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			if (lastRecord.getDamage() == 0) {
				return gm("&7无数据", p);
			}
			return "" + lastRecord.getDamage();
		}
		if (i.equalsIgnoreCase("lastmaxdamage")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			if (lastRecord.getMaxDamage() == 0) {
				return gm("&7无数据", p);
			}
			return "" + lastRecord.getMaxDamage();
		}
		if (i.equalsIgnoreCase("lastopponent")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			if (lastRecord.getOpponentName() == null) {
				return gm("&7无数据", p);
			}
			return lastRecord.getOpponentName();
		}
		if (i.equalsIgnoreCase("lasttime")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			return "" + lastRecord.getTime();
		}
		if (i.equalsIgnoreCase("lastdate")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			if (lastRecord.getDate() == null) {
				return gm("&7无数据", p);
			}
			return lastRecord.getDate();
		}
		if (i.equalsIgnoreCase("lastserver")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			if (lastRecord.getServerName() == null) {
				return gm("&7无数据", p);
			}
			return lastRecord.getServerName();
		}
		if (i.equalsIgnoreCase("lastexpchange")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			String expChangeStr = gm("&7无数据", p);
			if (lastRecord.getExpChange() != 0) {
				int expChange = lastRecord.getExpChange();
				if (expChange > 0) {
					expChangeStr = gm("获得", p) + expChange;
				} else {
					expChangeStr = gm("损失", p) + (0 - expChange);
				}
			}
			return expChangeStr;
		}
		if (i.equalsIgnoreCase("lastarenaname")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			String arenaName = gm("&7无数据", p);
			if (lastRecord.getArenaEditName() != null) {
				String editName = lastRecord.getArenaEditName();
				if (arenaInfoCache.get(editName).getDisplayName() != null) {
					arenaName = arenaInfoCache.get(editName).getDisplayName()
							.replace("&", "§");
				} else {
					arenaName = editName;
				}
			}
			return arenaName;
		}
		if (i.equalsIgnoreCase("laststartway")) {
			if (lastRecord == null) {
				return gm("&7无数据", p);
			}
			String startWayStr = gm("&7无数据", p);
			int startWay = lastRecord.getStartWay();
			if (startWay == 1) {
				startWayStr = gm("匹配赛", p);
			}
			if (startWay == 2) {
				startWayStr = gm("邀请赛", p);
			}
			if (startWay == 3) {
				startWayStr = gm("强制赛", p);
			}
			return startWayStr;
		}
		if (i.equalsIgnoreCase("winning-streak")) {
			return "" + recordCache.getWinningStreakTimes(name);
		}
		if (i.equalsIgnoreCase("max-winning-streak")) {
			return "" + recordCache.getMaxWinningStreakTimes(name);
		}
		if (i.startsWith("winrank")) {
			String numberString = i.replace("winrank", "");
			int rank = -1;
			try {
				rank = Integer.parseInt(numberString);
			} catch (Exception e) {
				rank = -1;
			}
			if (rank == -1) {
				return gm("&7无数据", p);
			} else {
				String playerName = ranking.getPlayerByWin(rank - 1);
				if (playerName == null) {
					return gm("&7无数据", p);
				} else {
					return playerName;
				}
			}
		}
		if (i.startsWith("kdrank")) {
			String numberString = i.replace("kdrank", "");
			int rank = -1;
			try {
				rank = Integer.parseInt(numberString);
			} catch (Exception e) {
				rank = -1;
			}
			if (rank == -1) {
				return gm("&7无数据", p);
			} else {
				String playerName = ranking.getPlayerByKD(rank - 1);
				if (playerName == null) {
					return gm("&7无数据", p);
				} else {
					return playerName;
				}
			}
		}
		if (i.equalsIgnoreCase("servertotalgametimes")) {
			int value = recordCache.getServerTotalGameTimes();
			if (value == -1) {
				return "Loading...";
			}
			return "" + value;
		}
		if (i.equalsIgnoreCase("globaltotalgametimes")) {
			int value = Main.getInstance().getGlobalGameTimes().getValue();
			if (value == -1) {
				return "Loading...";
			}
			return "" + value;
		}
		if (i.equalsIgnoreCase("gametimesproportion")) {
			int serverValue = recordCache.getServerTotalGameTimes();
			int globalValue = Main.getInstance().getGlobalGameTimes().getValue();
			if (serverValue != -1 && globalValue != -1) {
				BigDecimal bg = new BigDecimal(((double)serverValue/(double)globalValue)*100);
				double value = bg.setScale(4, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				return "" + value +"%";
			} else {
				return "Loading...";
			}
		}
		return "?";
	}

	public static void hook() {
		PlaceholderAPI.registerPlaceholderHook(HOOK_NAME, new RegPAPI());
	}

	public static void unhook() {
		PlaceholderAPI.unregisterPlaceholderHook(HOOK_NAME);
	}
}