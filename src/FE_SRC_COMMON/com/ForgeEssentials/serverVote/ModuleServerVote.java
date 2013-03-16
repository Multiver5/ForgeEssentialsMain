package com.ForgeEssentials.serverVote;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import com.ForgeEssentials.api.modules.FEModule;
import com.ForgeEssentials.api.modules.event.FEModuleServerInitEvent;
import com.ForgeEssentials.api.modules.event.FEModuleServerStopEvent;
import com.ForgeEssentials.api.snooper.VoteEvent;
import com.ForgeEssentials.api.snooper.snooperAPI;
import com.ForgeEssentials.core.ForgeEssentials;
import com.ForgeEssentials.serverVote.Votifier.VoteReceiver;
import com.ForgeEssentials.util.FunctionHelper;
import com.ForgeEssentials.util.OutputHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

@FEModule(name = "ServerVoteModule", parentMod = ForgeEssentials.class, configClass = ConfigServerVote.class)
public class ModuleServerVote
{
	@FEModule.Config
	public static ConfigServerVote	config;

	public static VoteReceiver		votifier;

	public ModuleServerVote()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@FEModule.ServerInit
	public void serverStarting(FEModuleServerInitEvent e)
	{
		try
		{
			votifier = new VoteReceiver(config.hostname, config.port);
			votifier.start();
		}
		catch (Exception e1)
		{
			FMLLog.severe("Error initializing Votifier compat.");
			FMLLog.severe(e.toString());
			e1.printStackTrace();
		}
	}

	@FEModule.ServerStop
	public void serverStopping(FEModuleServerStopEvent e)
	{
		try
		{
			votifier.shutdown();
		}
		catch (Exception e1)
		{
			FMLLog.severe("Error closing Votifier compat thread.");
			FMLLog.severe(e.toString());
			e1.printStackTrace();
		}
	}

	@ForgeSubscribe(priority = EventPriority.HIGHEST)
	public void defVoteResponces(VoteEvent vote)
	{
		OutputHandler.finer("Got Vote!");

		/*
		 * Offline check.
		 */

		EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(vote.player);
		if (player == null)
		{
			if (!config.allowOfflineVotes)
			{
				OutputHandler.info("Player for vote not online, vote canceled.");
				vote.setFeedback("notOnline");
				vote.setCanceled(true);
				return;
			}
			return;
		}

		/*
		 * do sh*t!
		 */

		if (!config.msgAll.equals(""))
		{
			FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager()
					.sendPacketToAllPlayers(new Packet3Chat(FunctionHelper.formatColors(config.msgAll.replaceAll("%service", vote.serviceName).replaceAll("%player", vote.player))));
		}

		if (!config.msgVoter.equals(""))
		{
			player.sendChatToPlayer(FunctionHelper.formatColors(config.msgAll.replaceAll("%service", vote.serviceName).replaceAll("%player", vote.player)));
		}

		if (!config.freeStuff.isEmpty())
		{
			for (ItemStack stack : config.freeStuff)
			{
				OutputHandler.finer(stack);
				player.inventory.addItemStackToInventory(stack.copy());
			}
		}
		else
		{
			OutputHandler.finer("noFreeStuff :(");
		}
	}
}
