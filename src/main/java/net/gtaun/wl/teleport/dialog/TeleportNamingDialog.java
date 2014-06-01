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

import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.common.dialog.WlInputDialog;
import net.gtaun.wl.lang.LocalizedStringSet.PlayerStringSet;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;
import net.gtaun.wl.teleport.util.TeleportUtils;

public abstract class TeleportNamingDialog
{
	public interface NamingHandler
	{
		void onNaming(WlInputDialog dialog, String name);
	}


	public static WlInputDialog create
	(Player player, EventManager eventManager, AbstractDialog parent, String caption, String message, TeleportServiceImpl service, NamingHandler namingHandler)
	{
		PlayerStringSet stringSet = service.getLocalizedStringSet().getStringSet(player);
		return WlInputDialog.create(player, eventManager)
			.parentDialog(parent)
			.caption(caption)
			.message(message)
			.onClickOk((d, t) ->
			{
				player.playSound(1083);
				if (!TeleportUtils.isVaildName(t))
				{
					((WlInputDialog) d).setAppendMessage(stringSet.format("Dialog.TeleportNamingDialog.InvaildName", t));
					d.show();
					return;
				}
				if (service.hasTeleport(t))
				{
					((WlInputDialog) d).setAppendMessage(stringSet.format("Dialog.TeleportNamingDialog.AlreadyExist", t));
					d.show();
					return;
				}

				namingHandler.onNaming((WlInputDialog) d, t);
			})
			.build();
	}
}
