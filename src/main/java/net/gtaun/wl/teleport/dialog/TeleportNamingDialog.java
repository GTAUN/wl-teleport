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

package net.gtaun.wl.teleport.dialog;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.common.dialog.AbstractInputDialog;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;
import net.gtaun.wl.teleport.util.TeleoportUtils;

public abstract class TeleportNamingDialog extends AbstractInputDialog
{
	private final TeleportServiceImpl teleportService;
	
	
	public TeleportNamingDialog(Player player, Shoebill shoebill, EventManager rootEventManager, AbstractDialog parentDialog, String caption, String message, TeleportServiceImpl teleportService)
	{
		super(player, shoebill, rootEventManager, parentDialog, caption, message);
		this.teleportService = teleportService;
	}
	
	@Override
	public void onClickOk(String inputText)
	{
		player.playSound(1083, player.getLocation());
		if (!TeleoportUtils.isVaildName(inputText))
		{
			append = String.format("{FF0000}* 您输入的名称 {FFFFFF}%1$s{FF0000} 不合法，请重新输入。", inputText);
			show();
			return;
		}
		if (teleportService.hasTeleport(inputText))
		{
			append = String.format("{FF0000}* 您输入的名称 {FFFFFF}%1$s{FF0000} 已被使用，请重新输入。", inputText);
			show();
			return;
		}
		
		onNaming(inputText);
	}
	
	protected abstract void onNaming(String name);
}
