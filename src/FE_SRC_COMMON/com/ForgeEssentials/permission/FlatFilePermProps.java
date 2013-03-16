package com.ForgeEssentials.permission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import com.ForgeEssentials.api.permissions.PermissionsAPI;
import com.ForgeEssentials.api.permissions.ZoneManager;

public class FlatFilePermProps
{
	File	file;

	public FlatFilePermProps(File file)
	{
		this.file = new File(file, "permissionProps.txt");
	}

	public HashMap<String, ArrayList<PermissionPropHolder>> load()
	{
		ArrayList<PermissionPropHolder> group = new ArrayList<PermissionPropHolder>();
		ArrayList<PermissionPropHolder> player = new ArrayList<PermissionPropHolder>();

		Configuration config = new Configuration(file);

		PermissionPropHolder holder;
		String catName;
		String[] split;
		for (ConfigCategory cat : config.categories.values())
		{
			if (!cat.isChild())
			{
				continue;
			}

			catName = cat.getQualifiedName();

			// ensures that the player and group catNameegories don't get in.
			if (catName.indexOf('.') == catName.lastIndexOf('.'))
			{
				continue;
			}

			split = splitCat(catName);

			if (catName.contains(".player."))
			{
				for (Property prop : cat.getValues().values())
				{
					holder = new PermissionPropHolder(split[1], prop.getName(), prop.value, split[0]);
					player.add(holder);
				}
			}
			else if (catName.contains(".group."))
			{
				for (Property prop : cat.getValues().values())
				{
					holder = new PermissionPropHolder(split[1], prop.getName(), prop.value, split[0]);
					group.add(holder);
				}
			}
		}

		HashMap<String, ArrayList<PermissionPropHolder>> map = new HashMap<String, ArrayList<PermissionPropHolder>>();
		map.put("playerPermProps", player);
		map.put("groupPermProps", group);
		return map;
	}

	public void save(ArrayList<PermissionPropHolder> players, ArrayList<PermissionPropHolder> groups)
	{
		// clear it.
		if (file.exists())
		{
			file.delete();
		}

		Configuration config = new Configuration(file);

		for (PermissionPropHolder holder : players)
		{
			config.get(holder.zone + ".player." + holder.target, holder.getQualifiedName(), holder.value);
		}

		for (PermissionPropHolder holder : groups)
		{
			config.get(holder.zone + ".group." + holder.target, holder.getQualifiedName(), holder.value);
		}

		config.addCustomCategoryComment(ZoneManager.getGLOBAL().getZoneName() + ".group." + PermissionsAPI.getDEFAULT().name, "The group used to as a placeholder for zone flags and such.");

		config.save();
	}

	private String[] splitCat(String qualifiedName)
	{
		String[] names = qualifiedName.split("\\" + Configuration.CATEGORY_SPLITTER, 3);
		return new String[] { names[0], names[2] };
	}

}
