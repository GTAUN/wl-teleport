/**
 * Copyright (C) 2013 MK124
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package net.gtaun.wl.teleport;

import java.util.HashMap;
import java.util.Map;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.AbstractShoebillContext;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.teleport.event.TeleportCreateEvent;

public class TeleportManager extends AbstractShoebillContext
{
	private final Map<String, Teleport> teleports;
	
	
	public TeleportManager(Shoebill shoebill, EventManager rootEventManager)
	{
		super(shoebill, rootEventManager);
		teleports = new HashMap<>();
		init();
	}
	
	@Override
	protected void onInit()
	{
		
	}
	
	@Override
	protected void onDestroy()
	{
		
	}
	
	EventManager getEventManager()
	{
		return eventManager;
	}
	
	public boolean hasTeleport(String name)
	{
		return teleports.containsKey(name);
	}

	public Teleport getTeleport(String name)
	{
		return teleports.get(name);
	}
	
	public boolean teleport(Player player, String name)
	{
		Teleport teleport = getTeleport(name);
		if (teleport == null) return false;
		
		return teleport.teleport(player);
	}

	public Teleport createTeleport(String name, Player creater, AngledLocation location)
	{
		Teleport teleport = new Teleport(this, name, creater.getName(), location);
		
		TeleportCreateEvent event = new TeleportCreateEvent(teleport);
		eventManager.dispatchEvent(event, this);
		
		return teleport;
	}
}
