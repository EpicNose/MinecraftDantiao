package com.valorin.api;

import static com.valorin.Main.getInstance;

import java.math.BigDecimal;

import org.bukkit.entity.Player;

import com.valorin.caches.EnergyCache;
import com.valorin.dan.CustomDan;
import com.valorin.dan.DanHandler;
import com.valorin.ranking.Ranking;

/**
 * 单挑插件新版API
 * 
 * @version 2.0正式版
 * @author Valorin
 * @since 2020/2/23
 */
public class DantiaoAPI {
	/**
	 * 获取某玩家的单挑积分
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的单挑积分
	 */
	public static double getPlayerPoints(Player p) {
		return getInstance().getCacheHandler().getPoint().get(p.getName());
	}

	/**
	 * 设置某玩家的单挑积分
	 * 
	 * @param p
	 *            玩家
	 * @param value
	 *            值
	 * @return 玩家的单挑积分
	 */
	public static void setPlayerPoints(Player p, double value) {
		getInstance().getCacheHandler().getPoint().set(p.getName(), value);
	}
	
	/**
	 * 获取某玩家的段位经验
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的段位经验
	 */
	public static int getPlayerExp(Player p) {
		return getInstance().getCacheHandler().getDan().get(p.getName());
	}

	/**
	 * 设置某玩家的段位经验
	 * 
	 * @param p
	 *            玩家
	 * @param value
	 *            值
	 * @return 玩家的段位经验
	 */
	public static void setPlayerExp(Player p, int value) {
		getInstance().getCacheHandler().getDan().set(p.getName(), value);
	}

	/**
	 * 获取某玩家的胜利场数
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的胜利场数
	 */
	public static int getPlayerWin(Player p) {
		return getInstance().getCacheHandler().getRecord().getWins(p.getName());
	}

	/**
	 * 获取某玩家的战败场数
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的战败场数
	 */
	public static int getPlayerLose(Player p) {
		return getInstance().getCacheHandler().getRecord()
				.getLoses(p.getName());
	}

	/**
	 * 获取某玩家的平局场数
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的平局场数
	 */
	public static int getPlayerDraw(Player p) {
		return getInstance().getCacheHandler().getRecord()
				.getDraws(p.getName());
	}

	/**
	 * 获取某玩家的精力值
	 * 
	 * @param p
	 *            玩家
	 * @return >=0,实际的精力值
	 * @return -1,无限能量（精力值系统被禁用时）
	 */
	public static double getPlayerEnergy(Player p) {
		EnergyCache energyCache = getInstance().getCacheHandler().getEnergy();
		if (energyCache.isEnable()) {
			BigDecimal bg = new BigDecimal(energyCache.get(p.getName()));
			double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			return value;
		} else {
			return -1;
		}
	}

	/**
	 * 获取设置的最大精力值
	 * 
	 * @param p
	 *            玩家
	 * @return >=0,实际设置的最大精力值
	 * @return -1,无限能量（精力值系统被禁用时）
	 */
	public static double getMaxEnergy() {
		EnergyCache energyCache = getInstance().getCacheHandler().getEnergy();
		if (energyCache.isEnable() == true) {
			return energyCache.getMaxEnergy();
		} else {
			return -1;
		}
	}

	/**
	 * 获取某玩家的KD值（胜场数/败场数的值）
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的KD值
	 */
	public static double getPlayerKD(Player p) {
		double kd = 0;
		int wins = getInstance().getCacheHandler().getRecord()
				.getWins(p.getName());
		int loses = getInstance().getCacheHandler().getRecord()
				.getLoses(p.getName());
		if ((double) loses != 0) {
			kd = (double) wins / (double) loses;
		} else {
			kd = (double) wins;
		}
		BigDecimal bg = new BigDecimal(kd);
		double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return value;
	}

	/**
	 * 获取某玩家的胜场数排名
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的胜场数排名
	 */
	public static int getPlayerWinRank(Player p) {
		Ranking ranking = getInstance().getRanking();
		return ranking.getWin(p);
	}

	/**
	 * 获取某玩家的KD值排名
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的KD值排名
	 */
	public static int getPlayerKDRank(Player p) {
		Ranking ranking = getInstance().getRanking();
		return ranking.getKD(p);
	}

	/**
	 * 获取某玩家的段位名称
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的胜场数排名
	 */
	public static String getPlayerDanName(Player p) {
		DanHandler dan = getInstance().getDanHandler();
		if (dan != null) {
			return dan.getPlayerDan(p.getName()).getDisplayName();
		} else {
			return getInstance().getConfigManager().getInitialDanName();
		}
	}

	/**
	 * 获取某玩家的段位
	 * 
	 * @param p
	 *            玩家
	 * @return 玩家的胜场数排名
	 */
	public static CustomDan getPlayerDan(Player p) {
		DanHandler dan = getInstance().getDanHandler();
		return dan.getPlayerDan(p.getName());
	}
	
	/**
	 * 获取DanHandler
	 * 
	 * @return DanHandler
	 */
	public static DanHandler getDanHandler() {
		return getInstance().getDanHandler();
	}
}