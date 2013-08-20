package net.gtaun.wl.teleport.util;

import org.apache.commons.lang3.StringUtils;

public class TeleoportUtils
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
	
	private TeleoportUtils()
	{
		
	}
}
