package com.ForgeEssentials.core.compat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;

import com.ForgeEssentials.api.json.JSONException;
import com.ForgeEssentials.api.json.JSONObject;
import com.ForgeEssentials.core.ForgeEssentials;
import com.ForgeEssentials.core.moduleLauncher.ModuleLauncher;
import com.ForgeEssentials.lib.mcstats.Metrics;
import com.ForgeEssentials.lib.mcstats.Metrics.Graph;
import com.ForgeEssentials.lib.mcstats.Metrics.Plotter;

// Obfuscated code handler for MCStats
public class CompatMCStats implements IServerStats
{
	public void load()
	{
		registerStats(this);
	}

	private static List<IServerStats>	handlers	= new ArrayList<IServerStats>();
	private static Metrics				metrics;

	public static void registerStats(IServerStats generator)
	{
		if (generator != null)
		{
			handlers.add(generator);
		}
		else
			throw new RuntimeException("Why would you register null?");
	}

	public static void doMCStats()
	{
		try
		{
			metrics = new Metrics("ForgeEssentials", ForgeEssentials.version);

			for (IServerStats obj : handlers)
			{
				obj.makeGraphs(metrics);
			}

			if (ForgeEssentials.mcstats)
			{
				metrics.start();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static JSONObject doSnooperStats() throws JSONException
	{
		JSONObject data = new JSONObject();

		for (IServerStats obj : handlers)
		{
			JSONObject temp = obj.addToServerInfo();
			for (String name : JSONObject.getNames(temp))
			{
				data.put(name, temp.get(name));
			}
		}

		return data;
	}

	@Override
	public void makeGraphs(Metrics metrics)
	{
		Graph graph = metrics.createGraph("Modules used");
		for (String module : ModuleLauncher.getModuleList())
		{
			Plotter plotter = new Plotter(module)
			{
				@Override
				public int getValue()
				{
					return 1;
				}
			};
			graph.addPlotter(plotter);
		}
	}

	@Override
	public JSONObject addToServerInfo() throws JSONException
	{
		return new JSONObject().put("FEmodules", ModuleLauncher.getModuleList());
	}

	// leave this here, it's to remove the need to obf mcstats
	public static boolean isOnlineMode()
	{
		return MinecraftServer.getServer().isServerInOnlineMode();
	}

	public static boolean isDediServer()
	{
		return MinecraftServer.getServer().isDedicatedServer();
	}

	public static int getPlayers()
	{
		return MinecraftServer.getServer().getCurrentPlayerCount();
	}

	public static String getMCVer()
	{
		return MinecraftServer.getServer().getMinecraftVersion();
	}

}
