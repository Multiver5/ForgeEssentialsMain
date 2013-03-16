package com.ForgeEssentials.api.permissions;

import java.util.ArrayList;

import net.minecraft.world.World;

import com.ForgeEssentials.util.AreaSelector.AreaBase;
import com.ForgeEssentials.util.AreaSelector.Selection;
import com.ForgeEssentials.util.AreaSelector.WorldArea;
import com.ForgeEssentials.util.AreaSelector.WorldPoint;

public class ZoneManager
{
	public static IZoneManager	manager;

	public static Zone getWorldZone(World world)
	{
		return manager.getWorldZone(world);
	}

	public static void deleteZone(String zoneID)
	{
		manager.deleteZone(zoneID);
	}

	public static boolean doesZoneExist(String zoneID)
	{
		return manager.doesZoneExist(zoneID);
	}

	public static Zone getZone(String zoneID)
	{
		return manager.getZone(zoneID);
	}

	public static boolean createZone(String zoneID, Selection sel, World world)
	{
		return manager.createZone(zoneID, sel, world);
	}

	public static Zone getWhichZoneIn(WorldPoint point)
	{
		return manager.getWhichZoneIn(point);
	}

	@Deprecated
	public static Zone getWhichZoneIn(AreaBase area, World world)
	{
		return manager.getWhichZoneIn(new WorldArea(world, area));
	}

	public static Zone getWhichZoneIn(WorldArea area)
	{
		return manager.getWhichZoneIn(area);
	}

	public static ArrayList<Zone> getZoneList()
	{
		return manager.getZoneList();
	}

	public static Zone getGLOBAL()
	{
		return manager.getGLOBAL();
	}

	public static Zone getSUPER()
	{
		return manager.getSUPER();
	}
}
