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

import java.util.Date;

import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.wl.teleport.event.PlayerTeleportEvent;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;

/**
 * 新未来世界传送点数据。
 * 
 * @author MK124
 */
@Entity("Teleport")
public class Teleport
{
	@Transient private TeleportManager manager;
	
	private String name;
	private String creater;
	private AngledLocation location;
	
	private Date updateDate;
	private int teleportCounter;
	

	Teleport()
	{
		
	}
	
	Teleport(TeleportManager manager, String name, String creater, AngledLocation location)
	{
		this.manager = manager;
		this.name = name;
		this.creater = creater;
		updateLocation(location);
		
		teleportCounter = 0;
	}
	
	void setManager(TeleportManager manager)
	{
		this.manager = manager;
	}

	public String getName()
	{
		return name;
	}
	
	void setName(String name)
	{
		this.name = name;
	}
	
	public String getCreater()
	{
		return creater;
	}
	
	public Player getCreaterPlayer()
	{
		return Player.get(creater);
	}
	
	public AngledLocation getLocation()
	{
		return location.clone();
	}
	
	public Date getUpdateDate()
	{
		return updateDate;
	}
	
	public int getTeleportCounter()
	{
		return teleportCounter;
	}

	public void updateLocation(AngledLocation loc)
	{
		location = new AngledLocation(loc).clone();
		updateDate = new Date();
	}

	public void rename(String newName) throws IllegalArgumentException, AlreadyExistException
	{
		manager.renameTeleport(this, newName);
	}
	
	public void delete()
	{
		manager.deleteTeleport(this);
	}
	
	public boolean teleport(Player player)
	{
		PlayerTeleportEvent event = new PlayerTeleportEvent(this, player);
		manager.getEventManager().dispatchEvent(event, player, this);
		
		if (event.isCanceled()) return false;
		
		player.setLocation(getLocation());
		teleportCounter++;
		return true;
	}
}
