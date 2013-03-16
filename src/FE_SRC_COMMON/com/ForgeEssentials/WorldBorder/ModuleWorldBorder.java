package com.ForgeEssentials.WorldBorder;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import com.ForgeEssentials.WorldBorder.Effects.IEffect;
import com.ForgeEssentials.api.modules.FEModule;
import com.ForgeEssentials.api.modules.event.FEModuleServerInitEvent;
import com.ForgeEssentials.core.ForgeEssentials;
import com.ForgeEssentials.util.OutputHandler;
import com.ForgeEssentials.util.events.PlayerMoveEvent;
import com.ForgeEssentials.util.vector.Vector2;

/**
 * Bounces players back into the border if they pass it. No bypass permissions
 * available, If needed, tell me on github.
 * @author Dries007
 */
@FEModule(name = "WorldBorder", parentMod = ForgeEssentials.class, configClass = ConfigWorldBorder.class)
public class ModuleWorldBorder
{
	public static boolean						WBenabled		= false;
	public static boolean						logToConsole	= true;

	@FEModule.Config
	public static ConfigWorldBorder				config;

	public static BorderShape					shape;
	public static HashMap<Integer, IEffect[]>	effectsList		= new HashMap<Integer, IEffect[]>();
	public static int							overGenerate	= 345;
	public static boolean						set				= false;

	public static int							X;
	public static int							Z;
	public static int							rad;

	public static int							maxX;
	public static int							maxZ;
	public static int							minX;
	public static int							minZ;

	public ModuleWorldBorder()
	{
		WBenabled = true;

	}

	@FEModule.ServerInit
	public void serverStarting(FEModuleServerInitEvent e)
	{
		e.registerServerCommand(new CommandWB());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@ForgeSubscribe
	public void playerMove(PlayerMoveEvent e)
	{
		if (WBenabled && set)
		{
			shape.doCheck((EntityPlayerMP) e.entityPlayer);
		}
	}

	/*
	 * Used to get determen shapes & execute the actual check.
	 */

	public enum BorderShape
	{
		round, square;

		public byte getByte()
		{
			if (equals(round))
				return 1;
			if (equals(square))
				return 2;
			return 0;
		}

		public static BorderShape getFromByte(byte byte1)
		{
			if (byte1 == 1)
				return BorderShape.round;
			else if (byte1 == 2)
				return BorderShape.square;
			return null;
		}

		public void doCheck(EntityPlayerMP player)
		{
			if (equals(round))
			{
				int dist = (int) getDistanceRound(X, Z, (int) player.posX, (int) player.posZ);
				if (dist > rad)
				{
					executeClosestEffects(dist - ModuleWorldBorder.rad, player);
				}
			}
			if (equals(square))
			{
				if (player.posX < minX)
				{
					executeClosestEffects((int) player.posX - minX, player);
				}
				if (player.posX > maxX)
				{
					executeClosestEffects((int) player.posX - maxX, player);
				}
				if (player.posZ < minZ)
				{
					executeClosestEffects((int) player.posZ - minZ, player);
				}
				if (player.posZ > maxZ)
				{
					executeClosestEffects((int) player.posZ - maxZ, player);
				}
			}
		}

		public int getETA()
		{
			if (equals(square))
			{
				int var = maxX - minX;
				var = var * var;
				return var;
			}
			if (equals(round))
			{
				int var = (int) (rad * rad * Math.PI);
				return var;
			}
			return 0;
		}
	}

	/*
	 * Penalty part
	 */

	public static void registerEffects(int dist, IEffect[] effects)
	{
		effectsList.put(dist, effects);
	}

	public static void executeClosestEffects(int dist, EntityPlayerMP player)
	{
		dist = Math.abs(dist);
		log(player, dist);
		for (int i = dist; i >= 0; i--)
		{
			if (effectsList.containsKey(i))
			{
				for (IEffect effect : effectsList.get(i))
				{
					effect.execute(player);
				}
			}
		}
	}

	/*
	 * Static Helper Methods
	 */

	public static double getDistanceRound(int centerX, int centerZ, int X, int Z)
	{
		int difX = centerX - X;
		int difZ = centerZ - Z;

		return Math.sqrt(difX * difX + difZ * difZ);
	}

	public static Vector2 getDirectionVector(EntityPlayerMP player)
	{
		Vector2 vecp = new Vector2(X - player.posX, Z - player.posZ);
		vecp.normalize();
		vecp.multiply(-1);
		return vecp;
	}

	public static void log(EntityPlayerMP player, int dist)
	{
		if (logToConsole)
		{
			OutputHandler.info(player.username + " passed the worldborder by " + dist + " blocks.");
		}
	}

	public static void setCenter(int rad, int posX, int posZ, BorderShape shapeToSet, boolean set)
	{
		shape = shapeToSet;
		ModuleWorldBorder.set = set;

		X = posX;
		Z = posZ;
		ModuleWorldBorder.rad = rad;

		maxX = posX + rad;
		maxZ = posZ + rad;

		minX = posX - rad;
		minZ = posZ - rad;

		config.forceSave();
	}
}
