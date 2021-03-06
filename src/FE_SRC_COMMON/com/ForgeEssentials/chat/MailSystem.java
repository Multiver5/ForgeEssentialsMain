package com.ForgeEssentials.chat;

import net.minecraft.entity.player.EntityPlayer;

import com.ForgeEssentials.api.data.ClassContainer;
import com.ForgeEssentials.api.data.DataStorageManager;
import com.ForgeEssentials.util.FEChatFormatCodes;
import com.ForgeEssentials.util.FunctionHelper;
import com.ForgeEssentials.util.Localization;
import com.google.common.collect.HashMultimap;

import cpw.mods.fml.common.IPlayerTracker;

public class MailSystem implements IPlayerTracker
{
	private static HashMultimap<String, Mail>	map	= HashMultimap.create();

	public static void AddMail(Mail mail)
	{
		map.put(mail.getReceiver(), mail);
		DataStorageManager.getReccomendedDriver().saveObject(new ClassContainer(Mail.class), mail);

		if (FunctionHelper.getPlayerFromPartialName(mail.getReceiver()) != null)
		{
			receiveMail(FunctionHelper.getPlayerFromPartialName(mail.getReceiver()));
		}
	}

	public static void LoadAll()
	{
		for (Object obj : DataStorageManager.getReccomendedDriver().loadAllObjects(new ClassContainer(Mail.class)))
		{
			Mail mail = (Mail) obj;
			map.put(mail.getReceiver(), mail);
		}
	}

	public static void SaveAll()
	{
		for (Mail mail : map.values())
		{
			DataStorageManager.getReccomendedDriver().saveObject(new ClassContainer(Mail.class), mail);
		}
	}

	public static void receiveMail(EntityPlayer receiver)
	{
		if (map.containsKey(receiver.username))
		{
			receiver.sendChatToPlayer(FEChatFormatCodes.GREEN + Localization.get("message.mail.header"));
			for (Mail mail : map.get(receiver.username))
			{
				receiver.sendChatToPlayer(FEChatFormatCodes.GREEN + "{" + mail.getSender() + "} " + FEChatFormatCodes.WHITE + mail.getMessage());
				DataStorageManager.getReccomendedDriver().deleteObject(new ClassContainer(Mail.class), mail.getKey());
			}
			receiver.sendChatToPlayer(FEChatFormatCodes.GREEN + Localization.get("message.mail.footer"));
		}
	}

	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		receiveMail(player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{
	}
}
