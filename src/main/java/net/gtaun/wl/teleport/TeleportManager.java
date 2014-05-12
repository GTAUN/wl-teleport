/**
 * WL Teleport Plugin
 * Copyright (C) 2013 MK124
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.gtaun.wl.teleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.gtaun.shoebill.common.AbstractShoebillContext;
import net.gtaun.shoebill.common.Saveable;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.teleport.event.TeleportCreateEvent;

import org.mongodb.morphia.Datastore;

public class TeleportManager extends AbstractShoebillContext implements Saveable
{
	private final Map<String, Teleport> teleports;
	
	private Datastore datastore;
	
	
	public TeleportManager(EventManager rootEventManager, Datastore datastore)
	{
		super(rootEventManager);
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
		return eventManagerNode;
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
		return teleports.values().stream().filter((t) -> t.getCreater().equalsIgnoreCase(creater)).collect(Collectors.toList());
	}

	public Teleport createTeleport(String name, Player creater, AngledLocation location) throws AlreadyExistException
	{
		if (teleports.containsKey(name)) throw new AlreadyExistException();
		
		Teleport teleport = new Teleport(this, name, creater.getName(), location);
		teleports.put(teleport.getName(), teleport);
		
		TeleportCreateEvent event = new TeleportCreateEvent(teleport);
		eventManagerNode.dispatchEvent(event, this);
		
		return teleport;
	}

	public void renameTeleport(Teleport teleport, String newName) throws AlreadyExistException, IllegalArgumentException
	{
		if (getTeleport(teleport.getName()) != teleport) throw new IllegalArgumentException();
		if (teleports.containsKey(newName)) throw new AlreadyExistException();
		
		teleports.remove(teleport.getName());
		teleport.setName(newName);
		teleports.put(teleport.getName(), teleport);
	}

	public void deleteTeleport(Teleport teleport)
	{
		if (getTeleport(teleport.getName()) != teleport) throw new IllegalArgumentException();
		teleports.remove(teleport.getName());
	}
}
