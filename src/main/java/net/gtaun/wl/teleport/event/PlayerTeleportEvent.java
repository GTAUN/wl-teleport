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
