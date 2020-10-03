package com.valorin.dan;

import java.util.ArrayList;
import java.util.List;


public class DefaultDanLoader {
	private List<CustomDan> customDans = new ArrayList<CustomDan>();

	public DefaultDanLoader() {
		customDans.add(new CustomDan("dan0", "&f[&2青铜I&f]&r", 50));
		customDans.add(new CustomDan("dan1", "&f[&2青铜II&f]&r", 150));
		customDans.add(new CustomDan("dan2", "&f[&2青铜III&f]&r", 280));
		customDans.add(new CustomDan("dan3", "&f[&3黑铁I&f]&r", 500));
		customDans.add(new CustomDan("dan4", "&f[&3黑铁II&f]&r", 760));
		customDans.add(new CustomDan("dan5", "&f[&3黑铁III&f]&r", 1200));
		customDans.add(new CustomDan("dan6", "&f[&e黄金I&f]&r", 1550));
		customDans.add(new CustomDan("dan7", "&f[&e黄金II&f]&r", 2100));
		customDans.add(new CustomDan("dan8", "&f[&e黄金III&f]&r", 3500));
		customDans.add(new CustomDan("dan9", "&f[&b钻石I&f]&r", 5000));
		customDans.add(new CustomDan("dan10", "&f[&b钻石II&f]&r", 6666));
		customDans.add(new CustomDan("dan11", "&f[&b钻石III&f]&r", 8888));
		customDans.add(new CustomDan("dan12", "&f[&6&l战斗天尊&f]&r", 10000));
		customDans.add(new CustomDan("dan13", "&f[&d&l荣耀王者&f]&r", 15000));
	}

	public List<CustomDan> get() {
		return customDans;
	}
}
