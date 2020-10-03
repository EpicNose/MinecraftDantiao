package com.valorin.task;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;
import static com.valorin.configuration.languagefile.MessageSender.sml;
import static com.valorin.dan.ExpChange.changeExp;
import static com.valorin.util.SyncBroadcast.bc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.entity.Player;

import com.valorin.arenas.Arena;
import com.valorin.caches.ArenaInfoCache;
import com.valorin.caches.DanCache;
import com.valorin.caches.PointCache;
import com.valorin.caches.RankingCache;
import com.valorin.caches.RecordCache;
import com.valorin.configuration.ConfigManager;
import com.valorin.dan.DanHandler;
import com.valorin.ranking.Ranking;

public class SettleEnd {
	public static void settle(Arena arena, Player w, Player l, boolean isDraw) {
		int time = arena.getTime();
		int startWay = arena.getStartWay();
		String winner = w.getName();
		String loser = l.getName();

		double player1Damage = arena.getDamage(true);
		double player1MaxDamage = arena.getMaxDamage(true);
		double player2Damage = arena.getDamage(false);
		double player2MaxDamage = arena.getMaxDamage(false);

		double winnerDamage, winnerMaxDamage, loserDamage, loserMaxDamage;
		if (arena.isp1(winner)) {
			winnerDamage = player1Damage;
			winnerMaxDamage = player1MaxDamage;
			loserDamage = player2Damage;
			loserMaxDamage = player2MaxDamage;
		} else {
			winnerDamage = player2Damage;
			winnerMaxDamage = player2MaxDamage;
			loserDamage = player1Damage;
			loserMaxDamage = player1MaxDamage;
		}

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = formatter.format(cal.getTime());
		ConfigManager configManager = getInstance().getConfigManager();
		DanCache danCache = getInstance().getCacheHandler().getDan();
		PointCache pointCache = getInstance().getCacheHandler().getPoint();
		RecordCache recordCache = getInstance().getCacheHandler().getRecord();

		String server = getInstance().getConfigManager().getServerName();
		int loserResult = 0, winnerResult = 0;
		
		double pointsAwarded = 0;
		int winExpAwarded = 0,drawExpAwarded = 0,maxExpAwarded = 0;
		// 匹配赛
		if (startWay == 1) {
			pointsAwarded = configManager.getPointsAwardedByPanel();
			winExpAwarded = configManager.getExpAwardedByPanel();
			drawExpAwarded = configManager.getDrawExpAwardedByPanel();
			maxExpAwarded = configManager.getMaxExpAwardedByPanel();
		}
		// 邀请赛
		if (startWay == 2) {
			pointsAwarded = configManager.getPointsAwardedByInviting();
			winExpAwarded = configManager.getExpAwardedByInviting();
			drawExpAwarded = configManager.getDrawExpAwardedByInviting();
			maxExpAwarded = configManager.getMaxExpAwardedByInviting();
		}
		// 强制赛
		if (startWay == 3) {
			pointsAwarded = configManager.getPointsAwardedByCompulsion();
			winExpAwarded = configManager.getExpAwardedByCompulsion();
			drawExpAwarded = configManager.getDrawExpAwardedByCompulsion();
			maxExpAwarded = configManager.getMaxExpAwardedByCompulsion();
		}
		
		if (!isDraw) {
			loserResult = 1;
			winnerResult = 0;
			
			if (pointsAwarded != 0) {
				double now = pointCache.get(winner);
				pointCache.set(winner, now + pointsAwarded);
				sm("&b做的不错！奖励你 &d{points} &b点单挑积分", w, "points",
						new String[] { "" + pointsAwarded });
			}

			recordCache.addWins(winner);
			recordCache.addLoses(loser);
		} else {
			recordCache.addDraws(winner);
			recordCache.addDraws(loser);
		}

		ArenaInfoCache arenaInfoCache = getInstance().getCacheHandler()
				.getArenaInfo();
		String displayName = arenaInfoCache.get(arena.getName())
				.getDisplayName();
		String editName = arena.getName();
	    int winnerExpChange = 0;
	    int loserExpChange = 0;
		//有赢家产生，刷新排名，给予经验
		if (!isDraw) {
			RankingCache rankingCache = getInstance().getCacheHandler()
					.getRanking();
			Ranking ranking = getInstance().getRanking();

			int winnerRank = ranking.getWin(w);
			int loserRank = ranking.getWin(l);
			int winnerRank2 = ranking.getKD(w);
			int loserRank2 = ranking.getKD(l);

			int winnerOrder = 0;
			if (rankingCache.getWin() != null) {
				for (int i = 0; i < rankingCache.getWin().size(); i++) {
					if (rankingCache.getWin().get(i).split("\\|")[0].equals(winner)) {
						winnerOrder = i;
					}
				}
			}
			int loserOrder = 0;
			if (rankingCache.getWin() != null) {
				for (int i = 0; i < rankingCache.getWin().size(); i++) {
					if (rankingCache.getWin().get(i).split("\\|")[0].equals(loser)) {
						loserOrder = i;
					}
				}
			}

			if (winnerOrder <= loserOrder) {// winner本来就强过loser 等号则代表没有排行数据，winner优先
				ranking.rank(winner + "|" + recordCache.getWins(winner), true);
				ranking.rank(loser + "|" + recordCache.getWins(loser), true);
			} else {
				ranking.rank(loser + "|" + recordCache.getWins(loser), true);
				ranking.rank(winner + "|" + recordCache.getWins(winner), true);
			}

			int winnerOrder2 = 0;
			if (rankingCache.getKD() != null) {
				for (int i = 0; i < rankingCache.getKD().size(); i++) {
					if (rankingCache.getKD().get(i).split("\\|")[0].equals(winner)) {
						winnerOrder2 = i;
					}
				}
			}
			int loserOrder2 = 0;
			if (rankingCache.getKD() != null) {
				for (int i = 0; i < rankingCache.getKD().size(); i++) {
					if (rankingCache.getKD().get(i).split("\\|")[0].equals(loser)) {
						loserOrder2 = i;
					}
				}
			}
			if (winnerOrder2 <= loserOrder2) {// winner本来就强过loser
												// 等号则代表没有排行数据，winner优先
				if (recordCache.getLoses(winner) != 0) {
					ranking.rank(
							winner
									+ "|"
									+ ((double) recordCache.getWins(winner) / (double) recordCache
											.getLoses(winner)), false);
				} else {
					ranking.rank(
							winner + "|" + ((double) recordCache.getWins(winner)),
							false);
				}

				if (recordCache.getLoses(loser) != 0) {
					ranking.rank(
							loser
									+ "|"
									+ ((double) recordCache.getWins(loser) / (double) recordCache
											.getLoses(loser)), false);
				} else {
					ranking.rank(loser + "|"
							+ ((double) recordCache.getWins(loser)), false);
				}
			} else {
				if (recordCache.getLoses(loser) != 0) {
					ranking.rank(
							loser
									+ "|"
									+ ((double) recordCache.getWins(loser) / (double) recordCache
											.getLoses(loser)), false);
				} else {
					ranking.rank(loser + "|"
							+ ((double) recordCache.getWins(loser)), false);
				}
				if (recordCache.getLoses(winner) != 0) {
					ranking.rank(
							winner
									+ "|"
									+ ((double) recordCache.getWins(winner) / (double) recordCache
											.getLoses(winner)), false);
				} else {
					ranking.rank(
							winner + "|" + ((double) recordCache.getWins(winner)),
							false);
				}
			}

			if (winnerRank != ranking.getWin(w) && ranking.getWin(w) != 0) {
				sm("&b胜场排名发生变更！&e{before}->{now}", w, "before now", new String[] {
						"" + winnerRank, "" + ranking.getWin(w) });
			}
			if (loserRank != ranking.getWin(l) && ranking.getWin(l) != 0) {
				sm("&b胜场排名发生变更！&e{before}->{now}", l, "before now", new String[] {
						"" + loserRank, "" + ranking.getWin(l) });
			}
			if (winnerRank2 != ranking.getKD(w) && ranking.getWin(w) != 0) {
				sm("&bKD排名发生变更！&e{before}->{now}", w, "before now", new String[] {
						"" + winnerRank2, "" + ranking.getKD(w) });
			}
			if (loserRank2 != ranking.getKD(l) && ranking.getWin(l) != 0) {
				sm("&bKD排名发生变更！&e{before}->{now}", l, "before now", new String[] {
						"" + loserRank2, "" + ranking.getKD(l) });
			}
			
			boolean b1 = arena.isp1(winner);
			int winnerExp;
			int loserExp;
			if (arena.getExp(b1) > maxExpAwarded) {
				winnerExp = maxExpAwarded;
			} else {
				winnerExp = arena.getExp(b1);
			}
			winnerExp = winnerExp + winExpAwarded;
			loserExp = (int) winnerExp / 3;
			
			DanHandler dh = getInstance().getDanHandler();

			boolean protection;
			int protectionExp = configManager.getProtectionExp();
			int winnerExpNow = danCache.get(winner);
			int loserExpNow = danCache.get(loser);
			if (protectionExp == 0) { // 有保护措施
				protection = false;
			} else {
				if (winnerExpNow - loserExpNow >= protectionExp
						|| loserExpNow - winnerExpNow >= protectionExp) {
					protection = true;
				} else {
					protection = false;
				}
			}

			int loserExpShow;
			int winnerExpShow;
			if (protection) {
				loserExpShow = 0;
				winnerExpShow = 0;
				sm("&c双方段位差距过大，段位经验不会变更", w, l);
			} else {
				changeExp(w, winnerExpNow + winnerExp);
				winnerExpChange = winnerExp;
				winnerExpShow = winnerExp;
				loserExpShow = loserExp;
				if (loserExpNow - loserExp > dh.getThreshold()) {
					changeExp(l, loserExpNow - loserExp); // 正常扣除经验
					loserExpChange = 0 - loserExp;
				} else {
					if (loserExpNow > dh.getThreshold()) {
						loserExpShow = loserExpNow - dh.getThreshold();
						changeExp(l, dh.getThreshold()); // 设置为保底经验
						loserExpChange = 0 - (loserExpNow - dh.getThreshold());
					} else {
						loserExpShow = 0; // 不扣经验
					}
				}
			}

			sml("&7============================================================| |                       &b比赛结束！|        &7恭喜获得了胜利，期待你下一次更加精彩得表现！|                  &7同时获得了 &a{exp} &7经验| |&7============================================================",
					w, "exp", new String[] { "" + winnerExpShow });
			if (l != null) {
				sml("&7============================================================| |                     &b比赛结束！|           &7你没有获胜，不要灰心，再接再厉！|                &7同时损失了 &c{exp} &7经验| |&7============================================================",
						l, "exp", new String[] { "" + loserExpShow });
			}
			if (displayName == null) {
				displayName = arena.getName();
			} else {
				displayName = displayName.replace("&", "§");
			}
			bc(gm("&b[战报]: &7玩家 &e{winner} &7在单挑赛场&r{arenaname}&r&7上以 &6{time}秒 &7击败玩家 &e{loser}",
					null, "winner arenaname time loser", new String[] { winner,
							displayName, arena.getTime() + "", loser }), startWay);

			//连胜
			int winTime = recordCache.getWinningStreakTimes(winner);
			int maxWinTime = recordCache.getMaxWinningStreakTimes(winner);
			recordCache.addWinningStreakTimes(winner);
			if (winTime + 1 > maxWinTime) {
				recordCache.addMaxWinningStreakTimes(winner);
			}
			recordCache.clearWinningStreakTimes(loser);
			int reportTimes = configManager.getBroadcastWinningStreakTimes();
			if (reportTimes == 0) {
				reportTimes = 3;
			}
			if (winTime + 1 >= reportTimes) {
				bc(gm("&a[恭喜]: &7玩家 &e{player} &7在竞技场上完成了 &b{times} &7连胜！", null,
						"player times", new String[] { winner, (winTime + 1) + "" }), startWay);
			}
		} else { // 平局
			changeExp(w, danCache.get(winner) + drawExpAwarded);
			changeExp(l, danCache.get(loser) + drawExpAwarded);
			winnerExpChange = drawExpAwarded;
			loserExpChange = winnerExpChange;
			sml("&7============================================================| |                    &b比赛结束！|          &7比赛超时！未决出胜负，判定为平局！|          &7同时获得了 &a{exp} &7经验| |&7============================================================",
					w, "exp", new String[] { "" + drawExpAwarded });
			if (l != null) {
				sml("&7============================================================| |                    &b比赛结束！|          &7比赛超时！未决出胜负，判定为平局！|          &7同时获得了 &a{exp} &7经验| |&7============================================================",
						l, "exp", new String[] { "" + drawExpAwarded });
			}
			if (displayName == null) {
				displayName = editName;
			} else {
				displayName = displayName.replace("&", "§");
			}
			bc(gm("&b[战报]: &7玩家 &e{p1} &7与 &e{p2} &7在单挑赛场&r{arenaname}&r&7上打成平手，实为精妙！",
					null, "p1 arenaname p2", new String[] { winner,
							displayName, loser }), startWay);
		}

		getInstance().getHD().setIsNeedRefresh(true);
		recordCache.add(loser, date, winner, server, time, loserDamage,
				loserMaxDamage, loserResult, startWay, loserExpChange, editName);
		recordCache.add(winner, date, loser, server, time, winnerDamage,
				winnerMaxDamage, winnerResult, startWay, winnerExpChange, editName);
		recordCache.refreshServerTotalGameTimes();
	}
}
