package com.ForgeEssentials.WorldControl;

import java.util.ArrayList;
import java.util.List;

import com.ForgeEssentials.AreaSelector.Point;

import net.minecraft.src.World;

/**
 * @author UnknownCoder : Max Bruce Defines an area to be backed up including before/after snapshots
 */

public class BackupArea
{
	private List<BackupObject> before = new ArrayList<BackupObject>();
	private List<BackupObject> after = new ArrayList<BackupObject>();
	public String username;
	public int worldEdit;
	public Point start;
	public Point end;
	public boolean hasUndone = false;

	public BackupArea(String user, int worldEdit)
	{
		username = user;
		this.worldEdit = worldEdit;
	}

	public void addBlockBefore(int x, int y, int z, int blockID, int metadata)
	{
		before.add(new BackupObject(x, y, z, blockID, metadata));
	}

	public void addBlockAfter(int x, int y, int z, int blockID, int metadata)
	{
		after.add(new BackupObject(x, y, z, blockID, metadata));
	}

	public void loadAreaBefore(World worldObj)
	{
		for (int i = 0; i < before.size(); i++)
		{
			BackupObject obj = before.get(i);
			worldObj.setBlockAndMetadataWithNotify(obj.X, obj.Y, obj.Z, obj.blockID, obj.metadata);
		}
		hasUndone = true;
	}

	public void loadAreaAfter(World worldObj)
	{
		for (int i = 0; i < after.size(); i++)
		{
			BackupObject obj = after.get(i);
			worldObj.setBlockAndMetadataWithNotify(obj.X, obj.Y, obj.Z, obj.blockID, obj.metadata);
		}
		hasUndone = false;
	}
}