package com.ForgeEssentials.economy.commands;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.ForgeEssentials.core.commands.ForgeEssentialsCommandBase;
import com.ForgeEssentials.economy.WalletHandler;
import com.ForgeEssentials.util.FunctionHelper;
import com.ForgeEssentials.util.Localization;
import com.ForgeEssentials.util.OutputHandler;

public class CommandPay extends ForgeEssentialsCommandBase
{
	@Override
	public String getCommandName()
	{
		return "pay";
	}

	@Override
	public void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		if (args.length == 2)
		{
			EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(args[0]);
			if (player == null)
			{
				sender.sendChatToPlayer(args[0] + " not found!");
			}
			else
			{
				int amount = parseIntWithMin(sender, args[1], 0);
				if (WalletHandler.getWallet(sender) >= amount)
				{
					WalletHandler.removeFromWallet(amount, sender);
					WalletHandler.addToWallet(amount, player);
					OutputHandler.chatConfirmation(sender, "You have payed " + player.username + " " + amount + " " + WalletHandler.currency(amount));
					OutputHandler.chatConfirmation(player, "You have been payed " + amount + " " + WalletHandler.currency(amount) + " by " + sender.getCommandSenderName());
				}
				else
				{
					OutputHandler.chatError(sender, "You can't afford that!!");
				}
			}
		}
		else
		{
			OutputHandler.chatError(sender, Localization.get(Localization.ERROR_BADSYNTAX) + getSyntaxPlayer(sender));
		}
	}

	@Override
	public void processCommandConsole(ICommandSender sender, String[] args)
	{
		if (args.length == 2)
		{
			EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(args[0]);
			if (PlayerSelector.hasArguments(args[0]))
			{
				player = PlayerSelector.matchOnePlayer(sender, args[0]);
			}
			if (player == null)
			{
				sender.sendChatToPlayer(args[0] + " not found!");
			}
			else
			{
				int amount = parseIntWithMin(sender, args[1], 0);
				WalletHandler.addToWallet(amount, player);
				OutputHandler.chatConfirmation(sender, "You have payed " + player.username + " " + amount + " " + WalletHandler.currency(amount));
				OutputHandler.chatConfirmation(player, "You have been payed " + amount + " " + WalletHandler.currency(amount) + " by " + sender.getCommandSenderName());
			}
		}
		else
		{
			sender.sendChatToPlayer(Localization.get(Localization.ERROR_BADSYNTAX) + getSyntaxConsole());
		}
	}

	@Override
	public boolean canConsoleUseCommand()
	{
		return true;
	}

	@Override
	public String getCommandPerm()
	{
		return "ForgeEssentials.Economy." + getCommandName();
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
