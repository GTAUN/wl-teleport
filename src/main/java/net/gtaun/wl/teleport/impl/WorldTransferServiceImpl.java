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

import net.gtaun.shoebill.Shoebill;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.ManagedEventManager;
import net.gtaun.wl.teleport.WorldTransferService;

/**
 * 新未来世界世界传送服务实现类。
 * 
 * @author MK124
 */
@SuppressWarnings("unused")
public class WorldTransferServiceImpl implements WorldTransferService
{
	private final Shoebill shoebill;
	
	private final ManagedEventManager eventManager;
	
	private boolean isCommandEnabled = true;
	private String commandOperation = "/tp";
	
	
	public WorldTransferServiceImpl(Shoebill shoebill, EventManager rootEventManager)
	{
		this.shoebill = shoebill;
		
		eventManager = new ManagedEventManager(rootEventManager);
		
		initialize();
	}
	
	private void initialize()
	{

	}

	public void uninitialize()
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
		commandOperation = op;
	}
}
