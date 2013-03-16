package com.ForgeEssentials.snooper;

import java.util.Collection;
import java.util.HashMap;

import com.ForgeEssentials.api.snooper.Response;
import com.ForgeEssentials.util.OutputHandler;

public class ResponseRegistry
{
	private static HashMap<Integer, Response>	map	= new HashMap<Integer, Response>();

	/**
	 * Register a response for an ID. Use the API!
	 * @param ID
	 * @param response
	 */
	public static void registerResponse(Integer ID, Response response)
	{
		if (map.containsKey(ID))
			throw new RuntimeException("You are attempting to register a response on an used ID: " + ID);
		else
		{
			OutputHandler.finer("Response " + response.getName() + " ID: " + ID + " registered!");
			response.id = ID;
			map.put(ID, response);
		}
	}

	/**
	 * Used to build the response.
	 * @param ID
	 * @return
	 */
	public static Response getResponse(byte ID)
	{
		if (map.containsKey((int) ID))
			return map.get((int) ID);
		else
			return map.get(0);
	}

	/**
	 * Used by config
	 * @return
	 */
	public static Collection<Response> getAllresponses()
	{
		return map.values();
	}
}
