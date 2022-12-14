package com.valorin.data;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.DataFile.areas;
import static com.valorin.configuration.DataFile.areasFile;
import static com.valorin.configuration.DataFile.blacklist;
import static com.valorin.configuration.DataFile.pd;
import static com.valorin.configuration.DataFile.ranking;
import static com.valorin.configuration.DataFile.records;
import static com.valorin.configuration.DataFile.saveAreas;
import static com.valorin.configuration.DataFile.saveBlackList;
import static com.valorin.configuration.DataFile.saveRanking;
import static com.valorin.configuration.DataFile.saveRecords;
import static com.valorin.configuration.DataFile.saveShop;
import static com.valorin.configuration.DataFile.savepd;
import static com.valorin.configuration.DataFile.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.valorin.configuration.ConfigManager;
import com.valorin.data.encapsulation.Good;
import com.valorin.data.encapsulation.Record;

public class Data {
	public static boolean useDatabase;

	public static boolean areaB = false, blacklistB = false, danB = false,
			energyB = false, languageFileB = false, pointB = false,
			pointShopB = false, recordB = false;

	public static void initalB(ConfigManager configManager) {
		if (configManager.isUseMySQL()) {
			areaB = configManager.isAreaUseMySQL();
			blacklistB = configManager.isBlacklistUseMySQL();
			danB = configManager.isDanUseMySQL();
			energyB = configManager.isEnergyUseMySQL();
			languageFileB = configManager.isLanguageFileUseMySQL();
			pointB = configManager.isPointUseMySQL();
			pointShopB = configManager.isPointShopUseMySQL();
			recordB = configManager.isRecordUseMySQL();
		}
	}

	public static interface Action {
		public void run();
	}

	public static List<String> getArenas() { // ?????????????????????????????????
		useDatabase = areaB;
		if (useDatabase) {
			return getInstance().getMySQL().getArenas();
		} else {
			List<String> arenaList = new ArrayList<String>();
			if (!areasFile.exists()) {
				return arenaList;
			}
			ConfigurationSection section = areas
					.getConfigurationSection("Arenas");
			if (section == null) {
				return arenaList;
			}
			section.getKeys(false).forEach(key -> {
				arenaList.add(key);
			});
			return arenaList;
		}
	}

	public static String getArenaDisplayName(String editName) { // ?????????????????????????????????
		useDatabase = areaB;
		if (useDatabase) {
			return getInstance().getMySQL().getArenaDisplayName(editName);
		} else {
			return areas.getString("Arenas." + editName + ".Name").replace("&",
					"??");
		}
	}

	public static Location getArenaPointA(String editName) { // ????????????????????????A???
		useDatabase = areaB;
		if (useDatabase) {
			return getInstance().getMySQL().getArenaPointA(editName);
		} else {
			World world = Bukkit.getWorld(areas.getString("Arenas." + editName
					+ ".A.World"));
			double x = areas.getDouble("Arenas." + editName + ".A.X"), y = areas
					.getDouble("Arenas." + editName + ".A.Y"), z = areas
					.getDouble("Arenas." + editName + ".A.Z");
			float yaw = (float) areas
					.getDouble("Arenas." + editName + ".A.YAW"), pitch = (float) areas
					.getDouble("Arenas." + editName + ".A.PITCH");
			return new Location(world, x, y, z, yaw, pitch);
		}
	}

	public static Location getArenaPointB(String editName) { // ????????????????????????B???
		useDatabase = areaB;
		if (useDatabase) {
			return getInstance().getMySQL().getArenaPointB(editName);
		} else {
			World world = Bukkit.getWorld(areas.getString("Arenas." + editName
					+ ".B.World"));
			double x = areas.getDouble("Arenas." + editName + ".B.X"), y = areas
					.getDouble("Arenas." + editName + ".B.Y"), z = areas
					.getDouble("Arenas." + editName + ".B.Z");
			float yaw = (float) areas
					.getDouble("Arenas." + editName + ".B.YAW"), pitch = (float) areas
					.getDouble("Arenas." + editName + ".B.PITCH");
			return new Location(world, x, y, z, yaw, pitch);
		}
	}

	public static List<String> getArenaCommands(String editName) { // ?????????????????????????????????
		useDatabase = areaB;
		if (useDatabase) {
			return getInstance().getMySQL().getArenaCommands(editName);
		} else {
			return areas.getStringList("Arenas." + editName + ".Commands");
		}
	}

	public static Location getArenaWatchingPoint(String editName) { // ?????????????????????????????????
		useDatabase = areaB;
		if (useDatabase) {
			return getInstance().getMySQL().getArenaWatchingPoint(editName);
		} else {
			String world = areas.getString("Arenas." + editName
					+ ".WatchingPoint.World");
			if (world == null) {
				return null;
			}
			double x = areas.getDouble("Arenas." + editName
					+ ".WatchingPoint.X"), y = areas.getDouble("Arenas."
					+ editName + ".WatchingPoint.Y"), z = areas
					.getDouble("Arenas." + editName + ".WatchingPoint.Z");
			float yaw = (float) areas.getDouble("Arenas." + editName
					+ ".WatchingPoint.YAW"), pitch = (float) areas
					.getDouble("Arenas." + editName + ".WatchingPoint.PITCH");
			return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
		}
	}

	public static void setArenaCommands(String editName, List<String> list) { // ??????????????????????????????
		useDatabase = areaB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setArenaCommands(editName, list);
				} else {
					areas.set("Arenas." + editName + ".Commands", list);
					saveAreas();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void setArenaWatchingPoint(String editName, Location location) { // ??????????????????????????????
		useDatabase = areaB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setArenaWatchingPoint(editName,
							location);
				} else {
					if (location == null) {
						areas.set("Arenas." + editName + ".WatchingPoint", null);
					} else {
						areas.set(
								"Arenas." + editName + ".WatchingPoint.World",
								location.getWorld().getName());
						areas.set("Arenas." + editName + ".WatchingPoint.X",
								location.getX());
						areas.set("Arenas." + editName + ".WatchingPoint.Y",
								location.getY());
						areas.set("Arenas." + editName + ".WatchingPoint.Z",
								location.getZ());
						areas.set("Arenas." + editName + ".WatchingPoint.YAW",
								(float) location.getYaw());
						areas.set(
								"Arenas." + editName + ".WatchingPoint.PITCH",
								(float) location.getPitch());
					}
					saveAreas();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void saveArena(String editName, String displayName,
			Location pointA, Location pointB) { // ????????????????????????????????????
		useDatabase = areaB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().saveArena(editName, displayName,
							pointA, pointB);
				} else {
					areas.set("Arenas." + editName + ".A.World", pointA
							.getWorld().getName());
					areas.set("Arenas." + editName + ".A.X", pointA.getX());
					areas.set("Arenas." + editName + ".A.Y", pointA.getY());
					areas.set("Arenas." + editName + ".A.Z", pointA.getZ());
					areas.set("Arenas." + editName + ".A.YAW",
							(float) pointA.getYaw());
					areas.set("Arenas." + editName + ".A.PITCH",
							(float) pointA.getPitch());
					areas.set("Arenas." + editName + ".B.World", pointB
							.getWorld().getName());
					areas.set("Arenas." + editName + ".B.X", pointB.getX());
					areas.set("Arenas." + editName + ".B.Y", pointB.getY());
					areas.set("Arenas." + editName + ".B.Z", pointB.getZ());
					areas.set("Arenas." + editName + ".B.YAW",
							(float) pointB.getYaw());
					areas.set("Arenas." + editName + ".B.PITCH",
							(float) pointB.getPitch());
					areas.set("Arenas." + editName + ".Name", displayName);
					saveAreas();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void deleteArena(String editName) { // ?????????????????????
		useDatabase = areaB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().deleteArena(editName);
				} else {
					areas.set("Arenas." + editName, null);
					saveAreas();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static Location getHologramLocation(int type) { // ???????????????????????????????????????
		useDatabase = areaB;
		if (useDatabase) {
			return getInstance().getMySQL().getHologramLocation(type);
		} else {
			String prefix;
			if (type == 0) {
				prefix = "Dantiao-HD-Win.";
			} else {
				prefix = "Dantiao-HD-KD.";
			}
			String world = areas.getString(prefix + "World");
			if (world == null) {
				return null;
			}
			double x = areas.getInt(prefix + "X");
			double y = areas.getInt(prefix + "Y");
			double z = areas.getInt(prefix + "Z");
			Location location = new Location(Bukkit.getWorld(world), x, y, z);
			return location;
		}
	}

	public static void setHologramLocation(int type, Location location) { // ??????????????????????????????????????????
		useDatabase = areaB;
		if (useDatabase) {
			getInstance().getMySQL().setHologramLocation(type, location);
		} else {
			String prefix;
			if (type == 0) {
				prefix = "Dantiao-HD-Win.";
			} else {
				prefix = "Dantiao-HD-KD.";
			}
			if (location == null) {
				areas.set(prefix.replace(".", ""), null);
			} else {
				areas.set(prefix + "World", location.getWorld().getName());
				areas.set(prefix + "X", location.getX());
				areas.set(prefix + "Y", location.getY());
				areas.set(prefix + "Z", location.getZ());
			}
			saveAreas();
		}
	}

	public static Location getLobbyLocation() { // ????????????????????????????????????
		useDatabase = areaB;
		if (useDatabase) {
			return getInstance().getMySQL().getLobbyLocation();
		} else {
			String prefix = "Dantiao-LobbyPoint.";
			String world = areas.getString(prefix + "World");
			if (world == null) {
				return null;
			}
			double x = areas.getInt(prefix + "X");
			double y = areas.getInt(prefix + "Y");
			double z = areas.getInt(prefix + "Z");
			Location location = new Location(Bukkit.getWorld(world), x, y, z);
			return location;
		}
	}

	public static void setLobbyLocation(Location location) { // ????????????????????????????????????
		useDatabase = areaB;
		if (useDatabase) {
			getInstance().getMySQL().setLobbyLocation(location);
		} else {
			if (location == null) {
				areas.set("Dantiao-LobbyPoint", null);
			} else {
				String prefix = "Dantiao-LobbyPoint.";
				areas.set(prefix + "World", location.getWorld().getName());
				areas.set(prefix + "X", location.getX());
				areas.set(prefix + "Y", location.getY());
				areas.set(prefix + "Z", location.getZ());
			}
			saveAreas();
		}
	}

	public static List<String> getBlacklist() { // ???????????????
		useDatabase = blacklistB;
		if (useDatabase) {
			return getInstance().getMySQL().getBlacklist();
		} else {
			return blacklist.getStringList("BlackList");
		}
	}

	public static void setBlacklist(List<String> list) { // ???????????????
		useDatabase = blacklistB;
		if (useDatabase) {
			getInstance().getMySQL().setBlacklist(list);
		} else {
			blacklist.set("BlackList", list);
			saveBlackList();
		}
	}

	public static int getDanExp(String name) { // ??????????????????????????????
		useDatabase = danB;
		if (useDatabase) {
			return getInstance().getMySQL().getDanExp(name);
		} else {
			return pd.getInt(name + ".Exp");
		}
	}

	public static void setDanExp(String name, int exp, boolean isAsyn) { // ??????????????????????????????
		useDatabase = danB;
		Action action = new Action() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setDanExp(name, exp);
				} else {
					pd.set(name + ".Exp", exp);
					savepd();
				}
			}
		};
		if (isAsyn) {
			new BukkitRunnable() {
				public void run() {
					action.run();
				}
			}.runTaskAsynchronously(getInstance());
		} else {
			action.run();
		}
	}

	public static String getLanguageFile(String name) { // ??????????????????????????????
		useDatabase = languageFileB;
		if (useDatabase) {
			return getInstance().getMySQL().getLanguageFile(name);
		} else {
			return pd.getString(name + ".Language");
		}
	}

	public static void setLanguageFile(String name, String language,
			boolean isAsyn) { // ????????????????????????????????????
		useDatabase = languageFileB;
		Action action = new Action() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setLanguageFile(name, language);
				} else {
					pd.set(name + ".Language", language);
					savepd();
				}
			}
		};
		if (isAsyn) {
			new BukkitRunnable() {
				public void run() {
					action.run();
				}
			}.runTaskAsynchronously(getInstance());
		} else {
			action.run();
		}
	}

	public static double getPoint(String name) { // ????????????????????????????????????
		useDatabase = pointB;
		if (useDatabase) {
			return getInstance().getMySQL().getPoint(name);
		} else {
			return pd.getDouble(name + ".Points");
		}
	}

	public static void setPoint(String name, double point, boolean isAsyn) { // ????????????????????????????????????
		useDatabase = pointB;

		Action action = new Action() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setPoint(name, point);
				} else {
					pd.set(name + ".Points", point);
					savepd();
				}
			}
		};
		if (isAsyn) {
			new BukkitRunnable() {
				public void run() {
					action.run();
				}
			}.runTaskAsynchronously(getInstance());
		} else {
			action.run();
		}
	}

	public static int getHistoryGood() { // ????????????????????????
		useDatabase = pointShopB;
		if (useDatabase) {
			return getInstance().getMySQL().getHistoryGood();
		} else {
			return shop.getInt("Num");
		}
	}

	public static List<Good> getGoodList() { // ??????????????????
		useDatabase = pointShopB;
		if (useDatabase) {
			return getInstance().getMySQL().getGoodList();
		} else {
			List<Good> list = new ArrayList<Good>();
			Set<String> set = shop.getKeys(false);
			set.remove("Num");
			for (String key : set) {
				ItemStack item = shop.getItemStack(key + ".Item");
				double price = shop.getDouble(key + ".Price");
				String broadcast = shop.getString(key + ".Broadcast");
				String description = shop.getString(key + ".Description");
				int salesVolumn = shop.getInt(key + ".SalesVolume");
				int num = Integer.parseInt(key.replace("n", ""));
				Good good = new Good(num, item, price, broadcast, description,
						salesVolumn);
				list.add(good);
			}
			return list;
		}
	}

	public static void setBroadcastForGood(int num, String broadcast) { // ?????????????????????????????????
		useDatabase = pointShopB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL()
							.setBroadcastForGood(num, broadcast);
				} else {
					shop.set("n" + num + ".Broadcast", broadcast);
					saveShop();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void setDescriptionForGood(int num, String description) { // ?????????????????????????????????
		useDatabase = pointShopB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setDescriptionForGood(num,
							description);
				} else {
					shop.set("n" + num + ".Description", description);
					saveShop();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void updateSalesVolumn(int num) { // ????????????
		useDatabase = pointShopB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().updateSalesVolumn(num);
				} else {
					int now = shop.getInt("n" + num + ".SalesVolume");
					shop.set("n" + num + ".SalesVolume", now + 1);
					saveShop();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void updateHistoryGood() { // ????????????????????????
		useDatabase = pointShopB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().updateHistoryGood();
				} else {
					shop.set("Num", shop.getInt("Num") + 1);
					saveShop();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void addGood(int num, ItemStack item, double price) { // ??????????????????
		useDatabase = pointShopB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().addGood(num, item, price);
				} else {
					shop.set("n" + num + ".Item", item);
					shop.set("n" + num + ".Price", price);
					saveShop();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void removeGood(int num) { // ??????????????????
		useDatabase = pointShopB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().removeGood(num);
				} else {
					shop.set("n" + num, null);
					saveShop();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static List<String> getWinRanking() { // ?????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getRanking(0);
		} else {
			return ranking.getStringList("Win");
		}
	}

	public static List<String> getKDRanking() { // ??????KD????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getRanking(1);
		} else {
			return ranking.getStringList("KD");
		}
	}

	public static void setRanking(int type, List<String> list) { // ???????????????
		useDatabase = recordB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setRanking(type, list);
				} else {
					if (type == 0) {
						ranking.set("Win", list);
					} else {
						ranking.set("KD", list);
					}
					saveRanking();
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static int getWins(String name) { // ??????????????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getWins(name);
		} else {
			return records.getInt(name + ".Win");
		}
	}

	public static int getLoses(String name) { // ??????????????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getLoses(name);
		} else {
			return records.getInt(name + ".Lose");
		}
	}

	public static int getDraws(String name) { // ??????????????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getDraws(name);
		} else {
			return records.getInt(name + ".Draw");
		}
	}

	public static int getWinningStreakTimes(String name) { // ???????????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getWinningStreakTimes(name);
		} else {
			return pd.getInt(name + ".Winning-Streak-Times");
		}
	}

	public static int getMaxWinningStreakTimes(String name) { // ?????????????????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getMaxWinningStreakTimes(name);
		} else {
			return pd.getInt(name + ".Max-Winning-Streak-Times");
		}
	}

	public static void setWins(String name, int value) { // ??????????????????????????????
		useDatabase = recordB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setWins(name, value);
				} else {
					records.set(name + ".Win", value);
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void setLoses(String name, int value) { // ??????????????????????????????
		useDatabase = recordB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setLoses(name, value);
				} else {
					records.set(name + ".Lose", value);
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void setDraws(String name, int value) { // ??????????????????????????????
		useDatabase = recordB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setDraws(name, value);
				} else {
					records.set(name + ".Draw", value);
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void setWinningStreakTimes(String name, int value) { // ??????????????????????????????
		useDatabase = recordB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setWinningStreakTimes(name, value);
				} else {
					records.set(name + ".Winning-Streak-Times", value);
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void setMaxWinningStreakTimes(String name, int value) { // ????????????????????????????????????
		useDatabase = recordB;
		new BukkitRunnable() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setMaxWinningStreakTimes(name,
							value);
				} else {
					records.set(name + ".Max-Winning-Streak-Times", value);
				}
			}
		}.runTaskAsynchronously(getInstance());
	}

	public static void addRecord(String name, String date, String opponent,
			String server, int time, double damage, double maxDamage,
			int result, int startWay, int expChange, String arenaEditName) { // ??????????????????
		useDatabase = recordB;
		if (useDatabase) {
			getInstance().getMySQL().addRecord(name, date, opponent, server,
					time, damage, maxDamage, result, startWay, expChange,
					arenaEditName);
		} else {
			int logWins = records.getInt(name + ".Win");
			int logLoses = records.getInt(name + ".Lose");
			int logDraws = records.getInt(name + ".Draw");
			int logGameTimes = logWins + logLoses + logDraws;
			records.set(name + ".Record." + logGameTimes + ".player", opponent);
			records.set(name + ".Record." + logGameTimes + ".time", time);
			records.set(name + ".Record." + logGameTimes + ".date", date);
			records.set(name + ".Record." + logGameTimes + ".damage", damage);
			records.set(name + ".Record." + logGameTimes + ".maxdamage",
					maxDamage);
			if (result == 0) { // ??????
				records.set(name + ".Record." + logGameTimes + ".isWin", true);
				records.set(name + ".Record." + logGameTimes + ".isDraw", false);
			}
			if (result == 1) { // ??????
				records.set(name + ".Record." + logGameTimes + ".isWin", false);
				records.set(name + ".Record." + logGameTimes + ".isDraw", false);
			}
			if (result == 2) {
				records.set(name + ".Record." + logGameTimes + ".isWin", false);
				records.set(name + ".Record." + logGameTimes + ".isDraw", true);
			}
			records.set(name + ".Record." + logGameTimes + ".startWay",
					startWay);
			records.set(name + ".Record." + logGameTimes + ".expChange",
					expChange);
			records.set(name + ".Record." + logGameTimes + ".arenaEditName",
					arenaEditName);
			records.set(name + ".Record." + logGameTimes + ".server",
					server);
			saveRecords();
		}
	}

	public static void initialRecordData(String name) { // ??????????????????????????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			Bukkit.getScheduler().runTaskAsynchronously(getInstance(),
					() -> getInstance().getMySQL().initialRecordData(name));
		}
	}

	public static int getRecordNumber(String name) { // ???????????????????????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getRecordNumber(name);
		} else {
			int wins = records.getInt(name + ".Win");
			int loses = records.getInt(name + ".Lose");
			int draws = records.getInt(name + ".Draw");
			int totalGameTimes = wins + loses + draws;
			return totalGameTimes;
		}
	}

	public static List<Record> getRecordList(String name) { // ????????????????????????
		useDatabase = recordB;
		if (useDatabase) {
			return getInstance().getMySQL().getRecordList(name);
		} else {
			List<Record> recordList = new ArrayList<Record>();
			ConfigurationSection section = records.getConfigurationSection(name
					+ ".Record");
			if (section != null) {
				section.getKeys(false).forEach(subKey -> {
					String prefix = name + ".Record." + subKey;

					String date = records.getString(prefix + ".date");
					String opponent = records.getString(prefix + ".player");
					String server = records.getString(prefix + ".server");
					int time = records.getInt(prefix + ".time");
					int damage = records.getInt(prefix + ".damage");
					int maxDamage = records.getInt(prefix + ".maxdamage");
					int result;
					if (records.getBoolean(prefix + ".isWin")) {
						result = 0; // ??????
					} else {
						if (records.getBoolean(prefix + ".isDraw")) {
							result = 2; // ??????
						} else {
							result = 1; // ??????
						}
					}
					int startWay = records.getInt(prefix + ".startWay");
					int expChange = records.getInt(prefix + ".expChange");
					String arenaEditName = records.getString(prefix
							+ ".arenaEditName");

					Record record = new Record(name, date, opponent, server,
							time, damage, maxDamage, result, startWay,
							expChange, arenaEditName);
					recordList.add(record);
				});
			}
			return recordList;
		}
	}

	public static double getEnergy(String name) { // ???????????????????????????
		useDatabase = energyB;
		if (useDatabase) {
			return getInstance().getMySQL().getEnergy(name);
		} else {
			return pd.getDouble(name + ".Energy");
		}
	}

	public static void setEnergy(String name, double energy, boolean isAsyn) { // ???????????????????????????
		useDatabase = energyB;

		Action action = new Action() {
			public void run() {
				if (useDatabase) {
					getInstance().getMySQL().setEnergy(name, energy);
				} else {
					pd.set(name + ".Energy", energy);
					savepd();
				}
			}
		};
		if (isAsyn) {
			new BukkitRunnable() {
				public void run() {
					action.run();
				}
			}.runTaskAsynchronously(getInstance());
		} else {
			action.run();
		}
	}
}
