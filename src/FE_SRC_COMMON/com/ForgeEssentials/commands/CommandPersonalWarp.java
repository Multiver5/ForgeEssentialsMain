package com.ForgeEssentials.commands;

import java.util.HashMap;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.ForgeEssentials.commands.util.CommandDataManager;
import com.ForgeEssentials.commands.util.PWarp;
import com.ForgeEssentials.core.PlayerInfo;
import com.ForgeEssentials.core.commands.ForgeEssentialsCommandBase;
import com.ForgeEssentials.util.TeleportCenter;
import com.ForgeEssentials.util.AreaSelector.WarpPoint;

public class CommandPersonalWarp extends ForgeEssentialsCommandBase
{
	@Override
	public String getCommandName()
	{
		return "personalwarp";
	}

	@Override
	public String[] getDefaultAliases()
	{
		return new String[]
		{ "pw" };
	}

	@Override
	public void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		HashMap<String, PWarp> map = CommandDataManager.pwMap.get(sender.username);

		if (args.length != 2)
		{
			String msg = "Warp list: ";
			for (String name : map.keySet())
			{
				msg = msg + ", " + name;
			}
			sender.sendChatToPlayer(msg);
		}
		else
		{
			if (args[0].equalsIgnoreCase("goto"))
			{
				if (map.containsKey(args[1]))
				{
					PWarp warp = map.get(args[1]);
					PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(sender.username);
					playerInfo.back = new WarpPoint(sender);
					TeleportCenter.addToTpQue(warp.getPoint(), sender);
				}
				else
				{
					sender.sendChatToPlayer("PW does not exist.");
				}
			}
			else if (args[0].equalsIgnoreCase("add"))
			{
				if (!map.containsKey(args[1]))
				{
					map.put(args[1], new PWarp(sender.username, args[1], new WarpPoint(sender)));
					sender.sendChatToPlayer("PW added.");
				}
				else
				{
					sender.sendChatToPlayer("PW already exists.");
				}
			}
			else if (args[0].equalsIgnoreCase("remove"))
			{
				if (map.containsKey(args[1]))
				{
					CommandDataManager.removePWarp(map.get(args[1]));
					map.remove(args[1]);
					sender.sendChatToPlayer("PW removed.");
				}
				else
				{
					sender.sendChatToPlayer("PW does not exist.");
				}
			}
		}
		CommandDataManager.pwMap.put(sender.username, map);
		CommandDataManager.savePWarps(sender.username);
	}

	@Override
	public void processCommandConsole(ICommandSender sender, String[] args)
	{
	}

	@Override
	public boolean canConsoleUseCommand()
	{
		return false;
	}

	@Override
	public String getCommandPerm()
	{
		return "ForgeEssentials.BasicCommands." + getCommandName();
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, "goto", "add", "remove");
		if (args.length == 2)
		{
			if (CommandDataManager.pwMap.get(sender.getCommandSenderName()) == null)
			{
				CommandDataManager.pwMap.put(sender.getCommandSenderName(), new HashMap<String, PWarp>());
			}
			return getListOfStringsFromIterableMatchingLastWord(args, CommandDataManager.pwMap.get(sender.getCommandSenderName()).keySet());
		}
		return null;
	}
}
