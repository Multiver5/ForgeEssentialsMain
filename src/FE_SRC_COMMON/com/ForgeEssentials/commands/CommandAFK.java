package com.ForgeEssentials.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.Configuration;

import com.ForgeEssentials.commands.util.AFKdata;
import com.ForgeEssentials.commands.util.TickHandlerCommands;
import com.ForgeEssentials.core.commands.ForgeEssentialsCommandBase;
import com.ForgeEssentials.util.Localization;
import com.ForgeEssentials.util.OutputHandler;

public class CommandAFK extends ForgeEssentialsCommandBase
{
	public static List<String>	afkList	= new ArrayList<String>();
	
	//Config
	public static int			warmup	= 5;

	@Override
	public void doConfig(Configuration config, String category)
	{
		warmup = config.get(category, "warmup", 5, "Time in sec. you have to stand still to activate AFK.").getInt();
	}

	@Override
	public String getCommandName()
	{
		return "afk";
	}

	@Override
	public void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		TickHandlerCommands.afkListToAdd.add(new AFKdata((EntityPlayerMP) sender));
		OutputHandler.chatConfirmation(sender, Localization.format("command.afk.warmup", warmup));
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

	public static void abort(AFKdata afkData)
	{
		if (!afkData.player.capabilities.isCreativeMode)
		{
			afkData.player.capabilities.disableDamage = false;
			afkData.player.sendPlayerAbilities();
		}
		OutputHandler.chatError(afkData.player, Localization.get("command.afk.out"));
		afkList.remove(afkData.player.username);
		TickHandlerCommands.afkListToRemove.add(afkData);
	}

	public static void makeAFK(AFKdata afkData)
	{
		afkData.player.capabilities.disableDamage = true;
		afkData.player.sendPlayerAbilities();
		afkList.add(afkData.player.username);
		OutputHandler.chatConfirmation(afkData.player, Localization.get("command.afk.in"));
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		return null;
	}
}
