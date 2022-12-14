package com.valorin.caches;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.valorin.Main;
import com.valorin.api.event.EnergyChangedEvent;
import com.valorin.data.Data;
import com.valorin.util.Debug;
import com.valorin.util.ViaVersion;

public class EnergyCache {
	private Map<String, Double> map = new HashMap<String, Double>();
	private BukkitTask timer;
	private double maxEnergy;
	private double energyNeeded;
	private double energyResumePerSecond;
	private boolean isEnable;

	public EnergyCache() {
		isEnable = Main.getInstance().getConfigManager().isEnergyEnabled();
		if (!isEnable) {
			return;
		}
		try {
			for (Player player : ViaVersion.getOnlinePlayers()) {
				load(player.getName());
			}
			for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
				load(player.getName());
			}
			Debug.send("精力值数据缓存已就绪", "The energy cache has been initialized");
		} catch (Exception e) {
			Debug.send("§c精力值数据缓存未能加载",
					"§cThe energy cache failed to initialize");
			e.printStackTrace();
		}
		maxEnergy = Main.getInstance().getConfigManager().getMaxEnergy();
		if (maxEnergy <= 0) {
			maxEnergy = 300.0;
		}
		energyNeeded = Main.getInstance().getConfigManager().getEnergyNeeded();
		if (energyNeeded <= 0) {
			energyNeeded = 90.0;
		}
		energyResumePerSecond = Main.getInstance().getConfigManager()
				.getEnergyResumePerSecond();
		if (energyResumePerSecond <= 0) {
			energyResumePerSecond = 0.5;
		}
		timer = new BukkitRunnable() {
			@Override
			public void run() {
				for (String name : map.keySet()) {
					double energyNow = map.get(name);
					if (maxEnergy - energyNow <= energyResumePerSecond) {
						map.put(name, maxEnergy);
					} else {
						map.put(name, energyNow + energyResumePerSecond);
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 20, 20);
	}

	public double get(String name) {
		try {
			return map.get(name);
		} catch (Exception e) {
			return 0;
		}
	}

	public void load(String name) {
		if (!map.keySet().contains(name)) {
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
					() -> {
						double energy = Data.getEnergy(name);
						map.put(name, energy);
					});
		}
	}

	public void save(boolean isAsyn) {
		if (!isEnable) {
			return;
		}
		for (String name : map.keySet()) {
			Data.setEnergy(name, map.get(name), isAsyn);
		}
		Debug.send("精力值数据已自动保存", "The energy data saved automatically");
	}

	public void set(String name, double energy) {
		double log = map.get(name);
		map.put(name, energy);
		if (Bukkit.getPlayer(name) != null && Bukkit.getPlayer(name).isOnline()) {
			EnergyChangedEvent event = new EnergyChangedEvent(
					Bukkit.getPlayer(name), log, energy);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	public double getMaxEnergy() { // 获取最大精力值
		return maxEnergy;
	}

	public double getEnergyNeeded() { // 获取每场所需精力值
		return energyNeeded;
	}

	public double getEnergyResumePerSecond() { // 获取每秒恢复的精力值
		return energyResumePerSecond;
	}

	public boolean isEnable() { // 本功能是否开启
		return isEnable;
	}

	public void cancelTimer() {
		if (!isEnable) {
			return;
		}
		timer.cancel();
	}
}
