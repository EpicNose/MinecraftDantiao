package com.valorin.configuration;

import static com.valorin.Main.getInstance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;
import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.util.ViaVersion;

public class ConfigManager {
	public FileConfiguration config;

	public ConfigManager() {
		reload();
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void reload() {
		getInstance().reloadConfig();
		try {
			FileConfiguration config = new YamlConfiguration();
			File file = new File(getInstance().getDataFolder(), "config.yml");
			if (!file.exists()) {
				Main.getInstance().saveResource("config.yml", false);
			}
			try {
				config.load(new BufferedReader(new InputStreamReader(
						new FileInputStream(file), Charsets.UTF_8)));
			} catch (Exception e) {
				config.load(new BufferedReader(new InputStreamReader(
						new FileInputStream(file))));
			}
			this.config = config;

			FileConfiguration symbols = new YamlConfiguration();
			File file2 = new File(getInstance().getDataFolder(), "symbols.yml");
			if (!file2.exists()) {
				Main.getInstance().saveResource("symbols.yml", false);
			}
			if (ViaVersion.isHasItemFlagMethod()) {
				symbols.load(new BufferedReader(new InputStreamReader(
						new FileInputStream(file2), Charsets.UTF_8)));
			} else {
				symbols.load(new BufferedReader(new InputStreamReader(
						new FileInputStream(file2))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Data.initalB(this);
	}

	public int getConfigVersion() {
		return config.getInt("ConfigVersion");
	}

	public boolean isCheckVersion() {
		return config.getBoolean("CheckVersion");
	}

	public String getServerName() {
		String s = config.getString("ServerName");
		if (s != null) {
			s = s.replace("&", "§");
		}
		return s;
	}

	public boolean isDebug() {
		return config.getBoolean("Debug");
	}

	public int getAutoSaveCachesTime() {
		return config.getInt("AutoSaveCachesTime");
	}

	public String getPrefix() {
		return config.getString("Message.Prefix").replace("&", "§");
	}

	public String getDefaultLanguage() {
		return config.getString("Message.Lang");
	}

	public boolean isUseMySQL() {
		return config.getBoolean("MySQL.Enable");
	}

	public String getMySQLURL() {
		return config.getString("MySQL.URL");
	}

	public String getMySQLUser() {
		return config.getString("MySQL.User");
	}

	public String getMySQLPassword() {
		return config.getString("MySQL.Password");
	}

	public boolean isAreaUseMySQL() {
		return config.getBoolean("MySQL.Function.Area");
	}

	public boolean isBlacklistUseMySQL() {
		return config.getBoolean("MySQL.Function.Blacklist");
	}

	public boolean isDanUseMySQL() {
		return config.getBoolean("MySQL.Function.Dan");
	}
	
	public boolean isEnergyUseMySQL() {
		return config.getBoolean("MySQL.Function.Energy");
	}

	public boolean isLanguageFileUseMySQL() {
		return config.getBoolean("MySQL.Function.LanguageFile");
	}

	public boolean isPointUseMySQL() {
		return config.getBoolean("MySQL.Function.Point");
	}

	public boolean isPointShopUseMySQL() {
		return config.getBoolean("MySQL.Function.PointShop");
	}

	public boolean isRecordUseMySQL() {
		return config.getBoolean("MySQL.Function.Record");
	}

	public boolean isEnergyEnabled() {
		return config.getBoolean("Energy.Enable");
	}

	public double getEnergyNeeded() {
		return config.getDouble("Energy.Need");
	}

	public double getMaxEnergy() {
		return config.getDouble("Energy.Max");
	}

	public double getEnergyResumePerSecond() {
		return config.getDouble("Energy.PerSecond");
	}

	public boolean isWorldWhitelistEnabled() {
		return config.getBoolean("WorldWhitelist.Enable");
	}

	public List<String> getWorldWhitelist() {
		return config.getStringList("WorldWhitelist.Worlds");
	}

	public String getHologramPluginUsed() {
		return config.getString("Hologram.Plugin");
	}

	public int getHologramRefreshInterval() {
		return config.getInt("Hologram.RefreshInterval");
	}

	public String getArenaCreatingToolMaterial() {
		return config.getString("Game.ArenaCreating.CreatingToolMaterial");
	}

	public List<String> getSearchingTimeTable() {
		return config.getStringList("Game.Start.Timetable.Searching");
	}

	public List<String> getInviteTimeTable() {
		return config.getStringList("Game.Start.Timetable.Invite");
	}

	public boolean isClickPlayerToSendRequestAllowed() {
		return config.getBoolean("Game.Start.ClickPlayerToSendRequest");
	}

	public List<String> getItemLimitByMaterial() {
		return config.getStringList("Game.Start.ItemLimit.Material");
	}

	public List<String> getItemLimitByLore() {
		return config.getStringList("Game.Start.ItemLimit.Lore");
	}

	public int getBroadcastWinningStreakTimes() {
		return config.getInt("Game.Finish.BroadcastWinningStreakTimes");
	}

	public int getProtectionExp() {
		return config.getInt("Game.Finish.ProtectionExp");
	}

	public double getPointsAwardedByPanel() {
		return config.getDouble("Game.Rewards.Panel.Points");
	}

	public int getExpAwardedByPanel() {
		return config.getInt("Game.Rewards.Panel.WinExp");
	}

	public int getMaxExpAwardedByPanel() {
		return config.getInt("Game.Rewards.Panel.MaxExp");
	}

	public int getDrawExpAwardedByPanel() {
		return config.getInt("Game.Rewards.Panel.DrawExp");
	}

	public boolean isFireworkAwardedByPanel() {
		return config.getBoolean("Game.Rewards.Panel.Firework");
	}
	
	public boolean isBroadcastByPanel() {
		return config.getBoolean("Game.Rewards.Panel.Broadcast");
	}

	public double getPointsAwardedByInviting() {
		return config.getDouble("Game.Rewards.Inviting.Points");
	}

	public int getExpAwardedByInviting() {
		return config.getInt("Game.Rewards.Inviting.WinExp");
	}

	public int getMaxExpAwardedByInviting() {
		return config.getInt("Game.Rewards.Inviting.MaxExp");
	}

	public int getDrawExpAwardedByInviting() {
		return config.getInt("Game.Rewards.Inviting.DrawExp");
	}

	public boolean isFireworkAwardedByInviting() {
		return config.getBoolean("Game.Rewards.Inviting.Firework");
	}
	
	public boolean isBroadcastByInviting() {
		return config.getBoolean("Game.Rewards.Inviting.Broadcast");
	}

	public double getPointsAwardedByCompulsion() {
		return config.getDouble("Game.Rewards.Compulsion.Points");
	}

	public int getExpAwardedByCompulsion() {
		return config.getInt("Game.Rewards.Compulsion.WinExp");
	}

	public int getMaxExpAwardedByCompulsion() {
		return config.getInt("Game.Rewards.Compulsion.MaxExp");
	}

	public int getDrawExpAwardedByCompulsion() {
		return config.getInt("Game.Rewards.Compulsion.DrawExp");
	}

	public boolean isFireworkAwardedByCompulsion() {
		return config.getBoolean("Game.Rewards.Compulsion.Firework");
	}
	
	public boolean isBroadcastByCompulsion() {
		return config.getBoolean("Game.Rewards.Compulsion.Broadcast");
	}

	public boolean isDanShowed() {
		return config.getBoolean("Dan.Settings.Show");
	}

	public String getInitialDanName() {
		String str = config.getString("Dan.Settings.InitialDanName");
		if (str != null) {
			str = str.replace("&", "§");
		}
		return str;

	}
}
