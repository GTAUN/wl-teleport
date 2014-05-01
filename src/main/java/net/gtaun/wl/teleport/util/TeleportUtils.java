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

package net.gtaun.wl.teleport.util;

import org.apache.commons.lang3.StringUtils;

public class TeleportUtils
{
	public static final int NAME_MIN_LENGTH = 2;
	public static final int NAME_MAX_LENGTH = 24;
	
	
	public static boolean isVaildName(String name)
	{
		if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) return false;
		if (name.contains("%") || name.contains("\t") || name.contains("\n")) return false;
		if (!StringUtils.trimToEmpty(name).equals(name)) return false;
		return true;
	}
	
	public static String filterName(String name)
	{
		name = StringUtils.trimToEmpty(name);
		name = StringUtils.replace(name, "%", "#");
		name = StringUtils.replace(name, "\t", " ");
		name = StringUtils.replace(name, "\n", " ");
		return name;
	}
	
	private TeleportUtils()
	{
		
	}
}
