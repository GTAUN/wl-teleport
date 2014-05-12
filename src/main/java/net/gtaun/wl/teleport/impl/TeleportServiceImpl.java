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

package net.gtaun.wl.teleport.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.gtaun.shoebill.common.AbstractShoebillContext;
import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.event.player.PlayerCommandEvent;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.resource.Plugin;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.gamemode.event.MainMenuDialogExtendEvent;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.TeleportManager;
import net.gtaun.wl.teleport.TeleportPlugin;
import net.gtaun.wl.teleport.TeleportService;
import net.gtaun.wl.teleport.dialog.TeleportDialog;
import net.gtaun.wl.teleport.dialog.TeleportMainDialog;

import org.mongodb.morphia.Datastore;

/**
 * 新未来世界传送服务实现类。
 * 
 * @author MK124
 */
public class TeleportServiceImpl extends AbstractShoebillContext implements TeleportService
{
	private final TeleportPlugin plugin;
	
	private TeleportManager teleportManager;
	
	private boolean isCommandEnabled = true;
	private String commandOperation = "/tp";
	private String teleportCommandOperation = "//";
	
	
	public TeleportServiceImpl(EventManager rootEventManager, TeleportPlugin plugin, Datastore datastore)
	{
		super(rootEventManager);
		this.plugin = plugin;
		this.teleportManager = new TeleportManager(eventManagerNode, datastore);
		init();
	}

	@Override
	protected void onInit()
	{
		eventManagerNode.registerHandler(PlayerCommandEvent.class, (e) ->
		{
			if (isCommandEnabled == false) return;
			
			Player player = e.getPlayer();
			String command = e.getCommand();
			
			if (command.startsWith(teleportCommandOperation))
			{
				if (command.length() <= 2)
				{
					player.sendMessage(Color.YELLOW, "传送指令用法: //[地点名称]");
					e.setProcessed();
					return;
				}
				
				String name = command.substring(2);
				if (!hasTeleport(name))
				{
					player.sendMessage(Color.YELLOW, "没有叫做 %1$s 的传送点。", name);
					e.setProcessed();
					return;
				}
				
				teleport(player, name);

				player.sendMessage(Color.WHITE, "你已传送到 %1$s 。", name);
				e.setProcessed();
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
				if (args.size() < 1) args.offer("");
				String op = args.poll();
				boolean ret = processPlayerCommand(player, op, args);
				if (ret) e.setProcessed();
				return;
			}
		});
		
		eventManagerNode.registerHandler(MainMenuDialogExtendEvent.class, (e) ->
		{
			Player player = e.getPlayer();
			e.getDialog().addItem("传送点系统", (i) ->
			{
				player.playSound(1083);
				showMainDialog(player, e.getDialog());
			});
		});
	}

	@Override
	protected void onDestroy()
	{

	}
	
	@Override
	public Plugin getPlugin()
	{
		return plugin;
	}
	
	@Override
	public void showMainDialog(Player player, AbstractDialog parentDialog)
	{
		TeleportMainDialog.create(player, eventManagerNode, parentDialog, this).show();
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
	public List<Teleport> getTeleports()
	{
		return teleportManager.getTeleports();
	}
	
	@Override
	public List<Teleport> getTeleports(String creater)
	{
		return teleportManager.getTeleports(creater);
	}
	
	@Override
	public Teleport createTeleport(String name, Player creater, AngledLocation location) throws AlreadyExistException
	{
		return teleportManager.createTeleport(name, creater, location);
	}
	
	@Override
	public void deleteTeleport(Teleport teleport)
	{
		teleportManager.deleteTeleport(teleport);
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
			
			try
			{
				Teleport teleport = createTeleport(name, player, player.getLocation());
				player.sendMessage(Color.WHITE, "传送点 %1$s 已创建。", name);
				TeleportDialog.create(player, eventManagerNode, null, this, teleport).show();
			}
			catch (AlreadyExistException e)
			{
				player.sendMessage(Color.RED, "错误: 无法以名称 %1$s 创建新传送点，请重试。", name);
			}
			return true;
		}
		else
		{
			showMainDialog(player, null);
			return true;
		}
	}
}
