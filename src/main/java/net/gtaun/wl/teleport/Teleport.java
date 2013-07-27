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

package net.gtaun.wl.teleport;

import net.gtaun.shoebill.common.player.PlayerDesc;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.object.Destroyable;
import net.gtaun.shoebill.object.Player;

/**
 * 新未来世界传送点数据。
 * 
 * @author MK124
 */
public abstract class Teleport implements Destroyable
{
	private final PlayerDesc createrDesc;
	private final AngledLocation location;
	
	
	protected Teleport(PlayerDesc createrDesc, AngledLocation location)
	{
		this.createrDesc = createrDesc;
		this.location = location;
	}

	public abstract String getName();
	public abstract boolean teleport(Player player);
	
	public PlayerDesc getCreaterDesc()
	{
		return createrDesc;
	}
	
	public AngledLocation getLocation()
	{
		return location.clone();
	}
}
