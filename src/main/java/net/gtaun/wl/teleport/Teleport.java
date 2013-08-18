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

import net.gtaun.shoebill.common.player.PlayerUtils;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.object.Player;
import net.gtaun.wl.teleport.event.PlayerTeleportEvent;

/**
 * 新未来世界传送点数据。
 * 
 * @author MK124
 */
public class Teleport
{
	private TeleportManager manager;
	
	private String name;
	private String creater;
	private AngledLocation location;
	

	Teleport()
	{
		
	}
	
	Teleport(TeleportManager manager, String name, String creater, AngledLocation location)
	{
		this.manager = manager;
		this.name = name;
		this.creater = creater;
		this.location = location;
	}
	
	void setManager(TeleportManager manager)
	{
		this.manager = manager;
	}

	public String getName()
	{
		return name;
	}
	
	public String getCreater()
	{
		return creater;
	}
	
	public Player getCreaterPlayer()
	{
		return PlayerUtils.getPlayer(creater);
	}
	
	public AngledLocation getLocation()
	{
		return location.clone();
	}
	
	public boolean teleport(Player player)
	{
		PlayerTeleportEvent event = new PlayerTeleportEvent(this, player);
		manager.getEventManager().dispatchEvent(event, player, this);
		
		if (event.isCanceled()) return false;
		
		player.setLocation(getLocation());
		return true;
	}
}
