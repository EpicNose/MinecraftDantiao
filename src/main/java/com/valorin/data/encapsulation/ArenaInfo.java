package com.valorin.data.encapsulation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class ArenaInfo {
	private String editName;
	private String displayName;
	private Location pointA;
	private Location pointB;
	private List<String> commandList;
	private Location watchingPoint;

	public ArenaInfo(String editName, String displayName, Location pointA,
			Location pointB, List<String> commandList, Location watchingPoint) {
		this.editName = editName;
		this.displayName = displayName;
		this.pointA = pointA;
		this.pointB = pointB;
		if (commandList == null) {
			this.commandList = new ArrayList<String>();
		} else {
			this.commandList = commandList;
		}
		this.watchingPoint = watchingPoint;
	}

	public String getEditName() {
		return editName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Location getPointA() {
		return pointA;
	}

	public Location getPointB() {
		return pointB;
	}

	public List<String> getCommandList() {
		return commandList;
	}

	public Location getWatchingPoint() {
		return watchingPoint;
	}

	public void setCommandList(List<String> commandList) {
		if (commandList == null) {
			this.commandList = new ArrayList<String>();
		} else {
			this.commandList = commandList;
		}
	}

	public void setWatchingPoint(Location watchingPoint) {
		this.watchingPoint = watchingPoint;
	}
}
