/**
 * Copyright (C) 2012 MK124
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

package net.gtaun.wl.teleport.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.PlayerDesc;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.event.PlayerEventHandler;
import net.gtaun.shoebill.event.player.PlayerCommandEvent;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManager.HandlerPriority;
import net.gtaun.util.event.ManagedEventManager;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.TeleportService;
import net.gtaun.wl.teleport.event.PlayerTeleportEvent;
import net.gtaun.wl.teleport.event.TeleportCreateEvent;

/**
 * 新未来世界传送服务实现类。
 * 
 * @author MK124
 */
@SuppressWarnings("unused")
public class TeleportServiceImpl implements TeleportService
{
	private final Shoebill shoebill;
	private final EventManager rootEventManager;
	
	private final ManagedEventManager eventManager;
	private final Map<String, Teleport> teleports;
	
	private boolean isCommandEnabled = true;
	private String commandOperation = "/tp";
	
	
	public TeleportServiceImpl(Shoebill shoebill, EventManager rootEventManager)
	{
		this.shoebill = shoebill;
		this.rootEventManager = rootEventManager;
		
		eventManager = new ManagedEventManager(rootEventManager);
		teleports = new HashMap<>();
		
		initialize();
	}
	
	private void initialize()
	{
		eventManager.registerHandler(PlayerCommandEvent.class, playerEventHandler, HandlerPriority.NORMAL);
	}

	public void uninitialize()
	{
		eventManager.cancelAll();
		
		teleports.clear();
	}

	@Override
	public void setCommandEnabled(boolean enable)
	{
		isCommandEnabled = enable;
	}
	
	@Override
	public void setCommandOperation(String op)
	{
		commandOperation = op;
	}
	
	private Teleport generateTeleport(final String name, PlayerDesc createrDesc, AngledLocation location)
	{
		return new Teleport(createrDesc, location)
		{
			private boolean isDestroyed;
			
			{
				teleports.put(name, this);
			}
			
			@Override
			public String getName()
			{
				return name;
			}
			
			@Override
			public boolean teleport(Player player)
			{
				PlayerTeleportEvent event = new PlayerTeleportEvent(this, player);
				eventManager.dispatchEvent(event, player, this);
				
				if (event.isCanceled()) return false;
				
				player.setLocation(getLocation());
				return true;
			}
			
			@Override
			public boolean isDestroyed()
			{
				return isDestroyed;
			}
			
			@Override
			public void destroy()
			{
				if (isDestroyed()) return;
				teleports.remove(name);
			}
		};
	}
	
	@Override
	public Teleport createTeleport(String name, PlayerDesc createrDesc, AngledLocation location)
	{
		Teleport teleport = generateTeleport(name, createrDesc, location);
		
		TeleportCreateEvent event = new TeleportCreateEvent(teleport);
		eventManager.dispatchEvent(event, this);
		
		return teleport;
	}

	@Override
	public Teleport getTeleport(String name)
	{
		return teleports.get(name);
	}
	
	@Override
	public boolean teleport(Player player, String name)
	{
		Teleport teleport = getTeleport(name);
		if (teleport == null) return false;
		
		return teleport.teleport(player);
	}
	
	private boolean processPlayerCommand(Player player, String op, Queue<String> args)
	{
		if (op.equals("make"))
		{
			if (args.size() != 1)
			{
				player.sendMessage(Color.YELLOW, "Usage: /tp make [name]");
				return true;
			}
			String name = args.poll();
			createTeleport(name, new PlayerDesc(player), player.getLocation());
			player.sendMessage(Color.WHITE, "Make Teleport: " + name);
			return true;
		}
		else
		{
			String name = op;
			teleport(player, name);
			return true;
		}
	}
	
	private PlayerEventHandler playerEventHandler = new PlayerEventHandler()
	{
		protected void onPlayerCommand(PlayerCommandEvent event)
		{
			if (isCommandEnabled == false) return;
			
			Player player = event.getPlayer();
			
			String command = event.getCommand();
			String[] splits = command.split(" ", 2);
			
			String operation = splits[0].toLowerCase();
			Queue<String> args = new LinkedList<>();
			
			if (splits.length > 1)
			{
				String[] argsArray = splits[1].split(" ");
				args.addAll(Arrays.asList(argsArray));
			}
			
			if (operation.equals(commandOperation))
			{
				if (args.size() < 1)
				{
					player.sendMessage(Color.YELLOW, "Usage: /tp [name]");
					event.setProcessed();
					return;
				}
				
				String op = args.poll();
				boolean ret = processPlayerCommand(player, op, args);
				if (ret) event.setProcessed();
				return;
			}
		}
	};
}
