package com.valorin.caches;

import java.util.ArrayList;
import java.util.List;

import com.valorin.data.Data;
import com.valorin.util.Debug;

public class BlacklistCache {
	private List<String> blacklist = new ArrayList<String>();

	public List<String> get() {
		return blacklist;
	}

	public BlacklistCache() {
		try {
			blacklist = Data.getBlacklist();
			Debug.send("黑名单缓存已就绪", "The blacklist cache has been initialized");
		} catch (Exception e) {
			Debug.send("§c黑名单缓存加载失败", "§cThe blacklist cache failed to initialize");
			e.printStackTrace();
		}
	}

	public void add(String name) {
		blacklist.add(name);
		Data.setBlacklist(blacklist);
	}

	public void remove(String name) {
		blacklist.remove(name);
		Data.setBlacklist(blacklist);
	}

	public void clear() {
		blacklist.clear();
		Data.setBlacklist(blacklist);
	}
}
