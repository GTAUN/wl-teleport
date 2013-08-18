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

import net.gtaun.shoebill.common.ConfigurablePlugin;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 新未来世界传送插件主类。
 * 
 * @author MK124
 */
public class TeleportPlugin extends ConfigurablePlugin
{
	public static final Logger LOGGER = LoggerFactory.getLogger(TeleportPlugin.class);
	
	
	private TeleportServiceImpl chatChannelService;
	
	
	public TeleportPlugin()
	{
		
	}
	
	@Override
	protected void onEnable() throws Throwable
	{
		chatChannelService = new TeleportServiceImpl(getShoebill(), getEventManager());
		registerService(TeleportService.class, chatChannelService);
		
		
		LOGGER.info(getDescription().getName() + " " + getDescription().getVersion() + " Enabled.");
	}
	
	@Override
	protected void onDisable() throws Throwable
	{
		unregisterService(TeleportService.class);
		chatChannelService.destroy();
		chatChannelService = null;
		
		LOGGER.info(getDescription().getName() + " " + getDescription().getVersion() + " Disabled.");
	}
}
