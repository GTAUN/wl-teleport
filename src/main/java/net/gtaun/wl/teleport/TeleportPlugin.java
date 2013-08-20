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

import java.io.File;

import net.gtaun.shoebill.common.ConfigurablePlugin;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.mapping.DefaultCreator;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * 新未来世界传送插件主类。
 * 
 * @author MK124
 */
public class TeleportPlugin extends ConfigurablePlugin
{
	public static final Logger LOGGER = LoggerFactory.getLogger(TeleportPlugin.class);
	
	
	private TeleportPluginConfig config;
	
	private MongoClient mongoClient;
	private Morphia morphia;
	private Datastore datastore;
	
	private TeleportServiceImpl chatChannelService;
	
	
	public TeleportPlugin()
	{
		
	}
	
	@Override
	protected void onEnable() throws Throwable
	{
		config = new TeleportPluginConfig(new File(getDataDir(), "config.yml"));
		
		mongoClient = new MongoClient(config.getDbHost());
		
		morphia = new Morphia();
		morphia.getMapper().getOptions().objectFactory = new DefaultCreator()
		{
            @Override
            protected ClassLoader getClassLoaderForClass(String clazz, DBObject object)
            {
                return getClass().getClassLoader();
            }
        };
		
		if (config.getDbUser().isEmpty() || config.getDbPass().isEmpty())
		{
			datastore = morphia.createDatastore(mongoClient, config.getDbName());
		}
		else
		{
			datastore = morphia.createDatastore(mongoClient, config.getDbName(), config.getDbUser(), config.getDbPass().toCharArray());
		}
		
		chatChannelService = new TeleportServiceImpl(getShoebill(), getEventManager(), this, datastore);
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
