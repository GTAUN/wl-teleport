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

package net.gtaun.wl.teleport.event;

import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.Interruptable;
import net.gtaun.wl.teleport.Teleport;

/**
 * 玩家被传送事件，可取消。
 * 
 * @author MK124
 */
public class PlayerTeleportEvent extends TeleportEvent implements Interruptable
{
	protected final Player player;
	
	private boolean isCanceled;
	
	
	public PlayerTeleportEvent(Teleport teleport, Player player)
	{
		super(teleport);
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public void interrupt()
	{
		super.interrupt();
	}
	
	/**
	 * 取消传送。
	 */
	public void cancel()
	{
		isCanceled = true;
		interrupt();
	}
	
	public boolean isCanceled()
	{
		return isCanceled;
	}
}
