package com.valorin.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

public class ViaVersion {
	public static ItemStack getGlassPane(int type) {
		Material material;
		short realType = (short) type;
		if (Material.getMaterial("STAINED_GLASS_PANE") != null) {
			material = Material.getMaterial("STAINED_GLASS_PANE");
		} else {
			realType = 0;
			String materialName = "WHITE_STAINED_GLASS_PANE";
			switch (type) {
			case 1:
				materialName = "ORANGE_STAINED_GLASS_PANE";
			case 2:
				materialName = "MAGENTA_STAINED_GLASS_PANE";
			case 3:
				materialName = "LIGHT_BLUE_STAINED_GLASS_PANE";
			case 4:
				materialName = "YELLOW_STAINED_GLASS_PANE";
			case 5:
				materialName = "LIME_STAINED_GLASS_PANE";
			case 6:
				materialName = "PINK_STAINED_GLASS_PANE";
			case 7:
				materialName = "GRAY_STAINED_GLASS_PANE";
			case 8:
				materialName = "LIGHT_GRAY_STAINED_GLASS_PANE";
			case 9:
				materialName = "CYAN_STAINED_GLASS_PANE";
			case 10:
				materialName = "PURPLE_STAINED_GLASS_PANE";
			case 11:
				materialName = "BLUE_STAINED_GLASS_PANE";
			case 12:
				materialName = "BROWN_STAINED_GLASS_PANE";
			case 13:
				materialName = "GREEN_STAINED_GLASS_PANE";
			case 14:
				materialName = "RED_STAINED_GLASS_PANE";
			case 15:
				materialName = "BLACK_STAINED_GLASS_PANE";
			}
			material = Material.getMaterial(materialName);
		}
		ItemStack itemStack = new ItemStack(material, 1, realType);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(" ");
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static Material getMapMaterial() {
		if (Material.getMaterial("EMPTY_MAP") != null) {
			return Material.getMaterial("EMPTY_MAP");
		} else {
			return Material.getMaterial("MAP");
		}
	}

	public static Material getGoldenAxeMaterial() {
		if (Material.getMaterial("GOLD_AXE") != null) {
			return Material.getMaterial("GOLD_AXE");
		} else {
			return Material.getMaterial("GOLDEN_AXE");
		}
	}

	public static Material getGoldenSwordMaterial() {
		if (Material.getMaterial("GOLD_SWORD") != null) {
			return Material.getMaterial("GOLD_SWORD");
		} else {
			return Material.getMaterial("GOLDEN_SWORD");
		}
	}

	public static Material getWoodenSwordMaterial() {
		if (Material.getMaterial("WOOD_SWORD") != null) {
			return Material.getMaterial("WOOD_SWORD");
		} else {
			return Material.getMaterial("WOODEN_SWORD");
		}
	}

	public static List<Player> getOnlinePlayers() {
		List<Player> players = null;
		try {
			Class<?> clazz = Class.forName("org.bukkit.Bukkit");
			Method method = clazz.getMethod("getOnlinePlayers");
			if (method.getReturnType().equals(Collection.class)) {
				Collection<?> rawPlayers = (Collection<?>) (method
						.invoke(Bukkit.getServer()));
				players = new ArrayList<Player>();
				for (Object o : rawPlayers) {
					if (o instanceof Player) {
						players.add((Player) o);
					}
				}
			} else {
				players = Arrays.asList((Player[]) method.invoke(Bukkit
						.getServer()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return players;
	}

	public static ItemStack getItemInMainHand(Player player) {
		ItemStack item = null;
		try {
			Class<?> clazz = Class.forName("org.bukkit.entity.Player");
			try {
				Method method = clazz.getMethod("getItemInHand");
				item = (ItemStack) method.invoke(player);
			} catch (NoSuchMethodException e) {
				Method method2 = clazz.getMethod("getInventory");
				PlayerInventory inventory = (PlayerInventory) method2.invoke(player);
				item = (ItemStack) method2.invoke(inventory);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	public static void setItemInMainHand(Player player, ItemStack item) {
		try {
			Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
			Class<?> itemStackClass = Class.forName("org.bukkit.inventory.ItemStack");
			Class<?> playerInventoryClass = Class.forName("org.bukkit.inventory.PlayerInventory");
			try {
				Method method2 = playerClass.getMethod("getInventory");
				PlayerInventory inventory = (PlayerInventory) method2.invoke(player);
				Method method3 = playerInventoryClass.getMethod("setItemInMainHand", itemStackClass);
				method3.invoke(inventory, item);
			} catch (NoSuchMethodException e) {
				Method method = playerClass.getMethod("setItemInHand", itemStackClass);
				method.invoke(player, item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ProjectileSource getProjectileSource(Entity entity,
			String entityName) {
		ProjectileSource ps = null;
		try {
			Class<?> clazz = Class.forName("org.bukkit.entity." + entityName);
			Method method = clazz.getMethod("getShooter");
			ps = (ProjectileSource) (method.invoke(entity));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ps;
	}

	public static boolean isHasItemFlagMethod() {
		Class<?> clazz;
		try {
			clazz = Class.forName("org.bukkit.inventory.meta.ItemMeta");
			for (Method method : clazz.getMethods()) {
				if (method.getName().equals("addItemFlags")) {
					return true;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
