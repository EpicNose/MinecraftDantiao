package com.valorin.caches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.util.Debug;
import com.valorin.util.ViaVersion;

public class LanguageFileCache {
	private Map<String, String> map = new HashMap<String, String>();
	private List<String> changeList = new ArrayList<String>();

	public LanguageFileCache() {
		try {
			for (Player player : ViaVersion.getOnlinePlayers()) {
				load(player.getName());
			}
			Debug.send("语言文件数据缓存已就绪",
					"The language file cache has been initialized");
		} catch (Exception e) {
			Debug.send("§c语言数据数据缓存未能加载",
					"§cThe language file cache failed to initialize");
			e.printStackTrace();
		}
	}

	public String get(String name) {
		return map.get(name);
	}

	public void load(String name) {
		if (!map.keySet().contains(name)) {
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
					() -> {
						String language = Data.getLanguageFile(name);
						map.put(name, language);
					});
		}
	}

	public void unload(String name) {
		if (map.containsKey(name))
			map.remove(name);
	}

	public void save(boolean isAsyn) {
		if (changeList.size() != 0) {
			for (String name : changeList) {
				Data.setLanguageFile(name, map.get(name), isAsyn);
			}
			changeList.clear();
			Debug.send("语言文件数据已自动保存", "Language File data saved automatically");
		}
	}

	public void set(String name, String language) {
		map.put(name, language);
		if (!changeList.contains(name)) {
			changeList.add(name);
		}
	}
}
