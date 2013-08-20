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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.Datastore;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.AbstractShoebillContext;
import net.gtaun.shoebill.common.Filter;
import net.gtaun.shoebill.common.FilterUtils;
import net.gtaun.shoebill.common.Saveable;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.teleport.event.TeleportCreateEvent;

public class TeleportManager extends AbstractShoebillContext implements Saveable
{
	private final Map<String, Teleport> teleports;
	
	private Datastore datastore;
	
	
	public TeleportManager(Shoebill shoebill, EventManager rootEventManager, Datastore datastore)
	{
		super(shoebill, rootEventManager);
		this.datastore = datastore;
		this.teleports = new HashMap<>();
		init();
	}
	
	@Override
	protected void onInit()
	{
		load();
	}
	
	@Override
	protected void onDestroy()
	{
		save();
	}

	@Override
	public void save()
	{
		datastore.save(teleports.values());
	}
	
	public void load()
	{
		List<Teleport> teleportList = datastore.createQuery(Teleport.class).asList();
		for (Teleport teleport : teleportList)
		{
			teleport.setManager(this);
			teleports.put(teleport.getName(), teleport);
		}
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
	
	public List<Teleport> getTeleports()
	{
		return new ArrayList<>(teleports.values());
	}
	
	public List<Teleport> getTeleports(final String creater)
	{
		return FilterUtils.filter(teleports.values(), new Filter<Teleport>()
		{
			public boolean isAcceptable(Teleport t)
			{
				return t.getCreater().equalsIgnoreCase(creater);
			}
		});
	}

	public Teleport createTeleport(String name, Player creater, AngledLocation location)
	{
		Teleport teleport = new Teleport(this, name, creater.getName(), location);
		
		TeleportCreateEvent event = new TeleportCreateEvent(teleport);
		eventManager.dispatchEvent(event, this);
		
		return teleport;
	}
}
