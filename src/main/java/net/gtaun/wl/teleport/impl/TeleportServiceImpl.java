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

package net.gtaun.wl.teleport.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.AbstractShoebillContext;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.event.PlayerEventHandler;
import net.gtaun.shoebill.event.player.PlayerCommandEvent;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManager.HandlerPriority;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.TeleportManager;
import net.gtaun.wl.teleport.TeleportService;

/**
 * 新未来世界传送服务实现类。
 * 
 * @author MK124
 */
public class TeleportServiceImpl extends AbstractShoebillContext implements TeleportService
{
	private TeleportManager teleportManager;
	
	private boolean isCommandEnabled = true;
	private String commandOperation = "/tp";
	private String teleportCommandOperation = "//";
	
	
	public TeleportServiceImpl(Shoebill shoebill, EventManager rootEventManager)
	{
		super(shoebill, rootEventManager);
		teleportManager = new TeleportManager(shoebill, eventManager);
		init();
	}

	@Override
	protected void onInit()
	{
		eventManager.registerHandler(PlayerCommandEvent.class, playerEventHandler, HandlerPriority.NORMAL);
	}

	@Override
	protected void onDestroy()
	{

	}

	@Override
	public void setCommandEnabled(boolean enable)
	{
		isCommandEnabled = enable;
	}
	
	@Override
	public void setCommandOperation(String op)
	{
		teleportCommandOperation = op;
	}
	
	@Override
	public boolean hasTeleport(String name)
	{
		return teleportManager.hasTeleport(name);
	}

	@Override
	public Teleport getTeleport(String name)
	{
		return teleportManager.getTeleport(name);
	}
	
	@Override
	public boolean teleport(Player player, String name)
	{
		return teleportManager.teleport(player, name);
	}
	
	@Override
	public Teleport createTeleport(String name, Player creater, AngledLocation location)
	{
		return teleportManager.createTeleport(name, creater, location);
	}
	
	private boolean processPlayerCommand(Player player, String op, Queue<String> args)
	{
		if (op.equals("make"))
		{
			if (args.size() != 1)
			{
				player.sendMessage(Color.YELLOW, "创建新地点指令: /tp make [地点名称]");
				return true;
			}
			String name = args.poll();
			createTeleport(name, player, player.getLocation());
			player.sendMessage(Color.WHITE, "传送点 %1$s 已创建。", name);
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
			
			if (command.startsWith(teleportCommandOperation))
			{
				if (command.length() <= 2)
				{
					player.sendMessage(Color.YELLOW, "传送指令用法: //[地点名称]");
					event.setProcessed();
					return;
				}
				
				String name = command.substring(2);
				if (!hasTeleport(name))	
				{
					player.sendMessage(Color.YELLOW, "没有叫做 %1$s 的传送点。", name);
					event.setProcessed();
					return;
				}
				
				teleport(player, name);
			}
			
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
					player.sendMessage(Color.YELLOW, "传送指令用法: /tp [地点名称]");
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
