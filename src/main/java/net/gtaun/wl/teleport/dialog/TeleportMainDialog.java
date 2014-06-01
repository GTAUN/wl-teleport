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
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.resource.Plugin;
import net.gtaun.shoebill.resource.ResourceDescription;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.common.dialog.WlListDialog;
import net.gtaun.wl.common.dialog.WlMsgboxDialog;
import net.gtaun.wl.lang.LocalizedStringSet.PlayerStringSet;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

public class TeleportMainDialog
{
	public static WlListDialog create
	(Player player, EventManager eventManager, AbstractDialog parent, TeleportServiceImpl service)
	{
		PlayerStringSet stringSet = service.getLocalizedStringSet().getStringSet(player);
		return WlListDialog.create(player, eventManager)
			.parentDialog(parent)
			.caption(stringSet.get("Dialog.TeleportMainDialog.Caption"))
			.item(stringSet.get("Dialog.TeleportMainDialog.TeleportList"), (i) -> new TeleportListDialog(player, eventManager, i.getCurrentDialog(), service, service.getTeleports()).show())
			.item(stringSet.get("Dialog.TeleportMainDialog.MyTeleport"), (i) -> new TeleportListDialog(player, eventManager, i.getCurrentDialog(), service, service.getTeleports(player.getName())).show())
			.item(stringSet.get("Dialog.TeleportMainDialog.MyFavorite"), (i) -> i.getCurrentDialog().show())
			.item(stringSet.get("Dialog.TeleportMainDialog.Create"), (i) ->
			{
				String caption = stringSet.get("Dialog.TeleportMainDialog.CreateDialog.Caption");
				String message = stringSet.get("Dialog.TeleportMainDialog.CreateDialog.Message");
				TeleportNamingDialog.create(player, eventManager, i.getCurrentDialog(), caption, message, service, (d, name) ->
				{
					try
					{
						Teleport teleport = service.createTeleport(name, player, player.getLocation());
						TeleportDialog.create(player, eventManager, i.getCurrentDialog(), service, teleport).show();
					}
					catch (AlreadyExistException e)
					{
						d.setAppendMessage(stringSet.get("Dialog.TeleportMainDialog.CreateDialog.Failed"));
						d.show();
					}
				}).show();
			})
			.item(stringSet.get("Dialog.TeleportMainDialog.Preferences"), (i) -> i.getCurrentDialog().show())
			.item(stringSet.get("Dialog.TeleportMainDialog.Help"), (i) ->
			{
				WlMsgboxDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption(stringSet.get("Dialog.TeleportMainDialog.HelpDialog.Caption"))
					.message(stringSet.get("Dialog.TeleportMainDialog.HelpDialog.Message"))
					.build();
			})
			.item(stringSet.get("Dialog.TeleportMainDialog.About"), (i) ->
			{
				Plugin plugin = service.getPlugin();
				ResourceDescription desc = plugin.getDescription();

				String caption = stringSet.get("Dialog.TeleportMainDialog.AboutDialog.Caption");
				String format =stringSet.get("Dialog.TeleportMainDialog.AboutDialog.Message");
				String message = String.format(format, desc.getVersion(), desc.getBuildNumber(), desc.getBuildDate());

				WlMsgboxDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption(caption)
					.message(message)
					.build();
			})
			.onClickOk((d, i) -> player.playSound(1083))
			.build();
	}
}
