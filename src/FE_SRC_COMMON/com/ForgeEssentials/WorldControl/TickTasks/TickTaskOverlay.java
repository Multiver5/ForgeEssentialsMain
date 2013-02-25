package com.ForgeEssentials.WorldControl.TickTasks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.ForgeEssentials.WorldControl.ConfigWorldControl;
import com.ForgeEssentials.core.PlayerInfo;
import com.ForgeEssentials.util.BlockArrayBackup;
import com.ForgeEssentials.util.BlockInfo;
import com.ForgeEssentials.util.Localization;
import com.ForgeEssentials.util.OutputHandler;
import com.ForgeEssentials.util.AreaSelector.AreaBase;
import com.ForgeEssentials.util.AreaSelector.Point;
import com.ForgeEssentials.util.AreaSelector.Selection;

public class TickTaskOverlay extends TickTaskLoadBlocks
{
	private ArrayList<AreaBase>	applicable;

	BlockInfo to;

	public TickTaskOverlay(EntityPlayer player, AreaBase sel, BlockInfo to)
	{
		super(player, sel);
		this.to=to;
	}

	public TickTaskOverlay(EntityPlayer player, AreaBase sel, BlockInfo to, ArrayList<AreaBase> applicable)
	{
		this(player, sel, to);
		this.applicable = applicable;
	}
	
	protected boolean placeBlock() {
		if(((Block.blocksList[world.getBlockId(x, y, z)]==null||Block.blocksList[world.getBlockId(x, y, z)].isBlockReplaceable(world, x, y, z)) && isApplicable(x, y, z)) && world.getBlockId(x, y-1, z)>0) {
			return place(x, y, z, to.randomBlock());
		}
		return true;
	}

	private boolean isApplicable(int x, int y, int z)
	{
		Point p = new Point(x, y, z);
		if (applicable == null)
		{
			return true;
		}

		boolean contains = false;

		for (AreaBase area : applicable)
		{
			contains = area.contains(p);
			if (contains)
			{
				return true;
			}
		}

		return contains;
	}

}