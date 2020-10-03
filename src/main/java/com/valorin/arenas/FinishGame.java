package com.valorin.arenas;

import static com.valorin.Main.getInstance;
import static com.valorin.arenas.ArenaManager.busyArenasName;
import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;
import static com.valorin.util.SyncBroadcast.bc;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.valorin.api.event.ArenaEventAbs;
import com.valorin.api.event.arena.ArenaDrawFinishEvent;
import com.valorin.api.event.arena.ArenaFinishEvent;
import com.valorin.caches.EnergyCache;
import com.valorin.configuration.ConfigManager;
import com.valorin.dan.CustomDan;
import com.valorin.dan.DanHandler;
import com.valorin.effect.WinFirework;
import com.valorin.event.game.CompulsoryTeleport;
import com.valorin.task.SettleEnd;
import com.valorin.teleport.ToLobby;
import com.valorin.teleport.ToLogLocation;

public class FinishGame {
	public static void normalEnd(String name, String winner, String loser,
			boolean isDraw) {// 正常结束
		Arena arena = getInstance().getArenaManager().getArena(name);
		if (arena == null) {
			return;
		}
		if (!arena.getEnable()) {
			return;
		}

		Player w = Bukkit.getPlayerExact(winner);
		Player l = Bukkit.getPlayerExact(loser);

		if (w != null) {
			try {
				if (w.isDead()) {
					w.spigot().respawn();
				}
			} catch (Exception e) {
				CompulsoryTeleport.players.put(winner,
						arena.getLoaction(arena.isp1(winner)));
			}
		}
		if (l != null) {
			try {
				if (l.isDead()) {
					l.spigot().respawn();
				}
			} catch (Exception e) {
				CompulsoryTeleport.players.put(loser,
						arena.getLoaction(arena.isp1(loser)));
			}
		}

		w.setHealth(w.getMaxHealth());
		w.setFoodLevel(20);
		if (l != null && !l.isDead()) {
			l.setHealth(l.getMaxHealth());
			l.setFoodLevel(20);
		}
		EnergyCache energyCache = getInstance().getCacheHandler().getEnergy();
		if (energyCache.isEnable()) {
			double energyNeeded = energyCache.getEnergyNeeded();
			energyCache.set(winner, energyCache.get(winner) - energyNeeded);
			energyCache.set(loser, energyCache.get(loser) - energyNeeded);
		}

		arena.setCanTeleport(true);
		arena.setWatchersTeleport(true);
		List<String> watchers = arena.getWatchers();
		Location lobbyLocation = getInstance().getCacheHandler().getArea()
				.getLobby();
		if (lobbyLocation != null) {
			ToLobby.to(w);
			ToLobby.to(l);
			sm("&b已将你带回单挑大厅！", w);
			if (l != null) {
				sm("&b已将你带回单挑大厅！", l);
			}
			for (String watcher : watchers) {
				if (Bukkit.getPlayerExact(watcher) != null) {
					ToLobby.to(Bukkit.getPlayerExact(watcher));
					sm("&b已将你带回单挑大厅！", l);
				}
			}
		} else {
			Location winnerLocation = arena
					.getLoaction(arena.isp1(w.getName()));
			Location loserLocation = arena.getLoaction(arena.isp1(l.getName()));
			ToLogLocation.to(w, l, winnerLocation, loserLocation);
			for (String watcher : watchers) {
				if (Bukkit.getPlayerExact(watcher) != null) {
					sm("&b[报告] &7你所观战的竞技场上的比赛已结束，请自行传送回家...",
							Bukkit.getPlayerExact(watcher), false);
				}
			}
		}// 回到原处
		ConfigManager configManager = getInstance().getConfigManager();
		boolean isFirework = false;
		int startWay = arena.getStartWay();
		if (startWay == 1) {
			isFirework = configManager.isFireworkAwardedByPanel();
		}
		if (startWay == 2) {
			isFirework = configManager.isFireworkAwardedByInviting();
		}
		if (isFirework) {
			WinFirework.setFirework(w.getLocation());
			sm("&a[v]WOW！服务器专门为你的获胜放了一朵烟花~", w);
		}

		busyArenasName.remove(arena.getName());
		getInstance().getSingleLineChartData().addGameTimes();

		ArenaEventAbs event;
		if (isDraw) {
			event = new ArenaDrawFinishEvent(w, l, arena);
		} else {
			event = new ArenaFinishEvent(w, l, arena);
		}
		Bukkit.getServer().getPluginManager().callEvent(event);

		new BukkitRunnable() {
			public void run() {
				try {
					SettleEnd.settle(arena, w, l, isDraw);
				} catch (Exception e) {
					e.printStackTrace();
				}

				DanHandler dh = getInstance().getDanHandler();
				dh.refreshPlayerDan(winner);
				dh.refreshPlayerDan(loser);
				if (arena.getDan(arena.isp1(winner)) != null) {
					List<CustomDan> danList = dh.getCustomDans();
					for (int i = 0; i < danList.size(); i++) {
						CustomDan danBefore = arena.getDan(arena.isp1(winner));
						CustomDan danNow = dh.getPlayerDan(winner);
						if (!danBefore.equals(danNow)) {
							bc(gm("&a[恭喜]: &7玩家 &e{player} &7的单挑段位成功升到了&r{dan}",
									null, "player dan", new String[] { winner,
											danNow.getDisplayName() }), startWay);
						}
					}
				} else {
					CustomDan danNow = dh.getPlayerDan(winner);
					bc(gm("&a[恭喜]: &7玩家 &e{player} &7突破了无段位的身份，首次获得了段位：&r{dan}&7！祝TA在单挑战斗的路上越走越远！",
							null, "player dan",
							new String[] { winner, danNow.getDisplayName() }), startWay);
				}
				if (isDraw) {
					if (arena.getDan(arena.isp1(loser)) != null) {
						List<CustomDan> danList = dh.getCustomDans();
						for (int i = 0; i < danList.size(); i++) {
							CustomDan danBefore = arena.getDan(arena
									.isp1(loser));
							CustomDan danNow = dh.getPlayerDan(loser);
							if (!danBefore.equals(danNow)) {
								bc(gm("&a[恭喜]: &7玩家 &e{player} &7的单挑段位成功升到了&r{dan}",
										null,
										"player dan",
										new String[] { loser,
												danNow.getDisplayName() }), startWay);
							}
						}
					} else {
						CustomDan danNow = dh.getPlayerDan(loser);
						bc(gm("&a[恭喜]: &7玩家 &e{player} &7突破了无段位的身份，首次获得了段位：&r{dan}&7！祝TA在单挑战斗的路上越走越远！",
								null, "player dan", new String[] { loser,
										danNow.getDisplayName() }), startWay);
					}
				}
				arena.finish();
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void compulsoryEnd(String name, Player finisher) {// 强制结束，不予记录
		Arena arena = getInstance().getArenaManager().getArena(name);
		if (arena == null) {
			sm("&c[x]不存在的竞技场，请检查输入", finisher);
			return;
		}
		if (!arena.getEnable()) {
			sm("&c[x]这个竞技场还没有比赛呢！", finisher);
			return;
		}

		Player p1 = Bukkit.getPlayerExact(arena.getp1());
		Player p2 = Bukkit.getPlayerExact(arena.getp2());

		arena.setWatchersTeleport(true);
		List<String> watchers = arena.getWatchers();
		Location lobbyLocation = getInstance().getCacheHandler().getArea()
				.getLobby();
		if (lobbyLocation != null) {
			if (p1 != null) {
				ToLobby.to(p1);
				sm("&b已将你带回单挑大厅！", p1);
			}
			if (p2 != null) {
				ToLobby.to(p2);
				sm("&b已将你带回单挑大厅！", p2);
			}
			for (String watcher : watchers) {
				if (Bukkit.getPlayerExact(watcher) != null) {
					ToLobby.to(Bukkit.getPlayerExact(watcher));
					sm("&b已将你带回单挑大厅！", p2);
				}
			}
		} else {
			Location winnerLocation = arena
					.getLoaction(arena.isp1(p1.getName()));
			Location loserLocation = arena
					.getLoaction(arena.isp1(p2.getName()));
			ToLogLocation.to(p1, p2, winnerLocation, loserLocation);
			for (String watcher : watchers) {
				if (Bukkit.getPlayerExact(watcher) != null) {
					sm("&b[报告] &7你所观战的竞技场上的比赛已结束，请自行传送回家...",
							Bukkit.getPlayerExact(watcher));
				}
			}
		}// 回到原处

		arena.finish();
		sm("&b&l比赛被管理员强制结束！本局比赛不会被记录！", p1, p2);
		sm("&a[v]已强制停止", finisher);
		busyArenasName.remove(arena.getName());
	}
}
