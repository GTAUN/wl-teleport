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

import net.gtaun.shoebill.common.AbstractShoebillContext;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.teleport.WorldTransferService;

/**
 * 新未来世界世界传送服务实现类。
 * 
 * @author MK124
 */
@SuppressWarnings("unused")
public class WorldTransferServiceImpl extends AbstractShoebillContext implements WorldTransferService
{
	private boolean isCommandEnabled = true;
	private String commandOperation = "/tp";
	
	
	public WorldTransferServiceImpl(EventManager rootEventManager)
	{
		super(rootEventManager);
		init();
	}
	
	@Override
	protected void onInit()
	{
		
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
		commandOperation = op;
	}
}
