package com.ForgeEssentials.permission.autoPromote;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.ForgeEssentials.core.commands.ForgeEssentialsCommandBase;

public class CommandAutoPromote extends ForgeEssentialsCommandBase
{
	@Override
	public String getCommandName()
	{
		return "autopromote";
	}

	@Override
	public void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		
	}

	@Override
	public void processCommandConsole(ICommandSender sender, String[] args)
	{
		
	}

	@Override
	public boolean canConsoleUseCommand()
	{
		return true;
	}

	@Override
	public String getCommandPerm()
	{
		return "ForgeEssentials.autoPromote";
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
