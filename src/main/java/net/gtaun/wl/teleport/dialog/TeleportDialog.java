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

import java.util.NoSuchElementException;
import java.util.Scanner;

import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.common.dialog.MsgboxDialog;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.common.dialog.WlInputDialog;
import net.gtaun.wl.common.dialog.WlListDialog;
import net.gtaun.wl.common.dialog.WlMsgboxDialog;
import net.gtaun.wl.lang.LocalizedStringSet.PlayerStringSet;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

import org.apache.commons.lang3.time.DateFormatUtils;

public class TeleportDialog
{
	public static WlListDialog create
	(Player player, EventManager eventManager, AbstractDialog parent, TeleportServiceImpl service, Teleport teleport)
	{
		PlayerStringSet stringSet = service.getLocalizedStringSet().getStringSet(player);
		return WlListDialog.create(player, eventManager)
			.parentDialog(parent)
			.caption(() -> stringSet.format("Dialog.TeleportDialog.Caption", teleport.getName()))

			.item(() -> stringSet.format("Dialog.TeleportDialog.Name", teleport.getName()), (i) ->
			{
				player.playSound(1083);

				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					i.getCurrentDialog().show();
					return;
				}

				String caption = stringSet.get("Dialog.TeleportDialog.EditNameDialog.Caption");
				String message = stringSet.get("Dialog.TeleportDialog.EditNameDialog.Message");
				TeleportNamingDialog.create(player, eventManager, i.getCurrentDialog(), caption, message, service, (d, name) ->
				{
					try
					{
						teleport.rename(name);
						d.showParentDialog();
					}
					catch (IllegalArgumentException | AlreadyExistException e)
					{
						d.setAppendMessage(stringSet.get("Dialog.TeleportDialog.EditNameDialog.InvaildName"));
						d.show();
					}
				}).show();
			})

			.item(() -> stringSet.format("Dialog.TeleportDialog.Creater", teleport.getCreater()), (i) -> i.getCurrentDialog().show())

			.item(() ->
			{
				AngledLocation loc = teleport.getLocation();
				return stringSet.format("Position", loc.x, loc.y, loc.z, loc.interiorId);
			}, (i) ->
			{
				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					i.getCurrentDialog().show();
					return;
				}

				AngledLocation oldLoc = teleport.getLocation();
				String msg = stringSet.format("Dialog.TeleportDialog.EditPositionDialog.Message", oldLoc.x, oldLoc.y, oldLoc.z, oldLoc.interiorId);

				WlInputDialog.create(player, eventManager)
					.caption(stringSet.get("Dialog.TeleportDialog.EditPositionDialog.Caption"))
					.message(msg)
					.onClickOk((d, text) ->
					{
						player.playSound(1083);

						try (Scanner scanner = new Scanner(text))
						{
							AngledLocation location = new AngledLocation(teleport.getLocation());
							location.set(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat());
							teleport.updateLocation(location);
							d.showParentDialog();
						}
						catch (NoSuchElementException e)
						{
							((WlInputDialog) d).setAppendMessage(stringSet.get("Dialog.Common.InvaildFormat"));
							d.show();
						}
					})
					.build().show();
			})

			.item(() ->
			{
				AngledLocation loc = teleport.getLocation();
				return String.format(stringSet.format("Dialog.TeleportDialog.Angle", loc.getAngle()));
			}, (i) ->
			{
				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					i.getCurrentDialog().show();
					return;
				}

				String msg = stringSet.format("Dialog.TeleportDialog.EditAngleDialog.Message", teleport.getLocation().getAngle());
				WlInputDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption(stringSet.get("Dialog.TeleportDialog.EditAngleDialog.Caption"))
					.message(msg)
					.onClickOk((d, text) ->
					{
						player.playSound(1083);

						try (Scanner scanner = new Scanner(text))
						{
							AngledLocation location = new AngledLocation(teleport.getLocation());
							location.setAngle(scanner.nextFloat());
							teleport.updateLocation(location);
							d.showParentDialog();
						}
						catch (NoSuchElementException e)
						{
							((WlInputDialog) d).setAppendMessage(stringSet.get("Dialog.Common.InvaildFormat"));
							d.show();
						}
					})
					.build().show();
			})

			.item(() -> stringSet.format("Dialog.TeleportDialog.UpdateDate", DateFormatUtils.ISO_DATETIME_FORMAT.format(teleport.getUpdateDate())), (i) -> i.getCurrentDialog().show())

			.item(() -> stringSet.format("Dialog.TeleportDialog.Popularity", teleport.getTeleportCounter()), (i) -> i.getCurrentDialog().show())

			.item(stringSet.get("Dialog.TeleportDialog.SetPos"), () -> player.getName().equalsIgnoreCase(teleport.getCreater()), (i) ->
			{
				String message = stringSet.format("Dialog.TeleportDialog.SetPosConfirmDialog.Message", teleport.getName());
				WlMsgboxDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption(stringSet.get("Dialog.TeleportDialog.SetPosConfirmDialog.Caption"))
					.message(message)
					.onClickOk((d) ->
					{
						player.playSound(1083);
						teleport.updateLocation(player.getLocation());
						d.showParentDialog();
					})
					.build().show();
			})

			.item(stringSet.get("Dialog.TeleportDialog.Delete"), () -> player.getName().equalsIgnoreCase(teleport.getCreater()), (i) ->
			{
				String message = stringSet.format("Dialog.TeleportDialog.DeleteConfirmDialog.Message", teleport.getName());
				MsgboxDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption(stringSet.get("Dialog.TeleportDialog.DeleteConfirmDialog.Caption"))
					.message(message)
					.onClickOk((d)->
					{
						player.playSound(1083);
						teleport.delete();
						d.showParentDialog();
					}).build().show();
			})

			.item(stringSet.get("Dialog.TeleportDialog.Teleport"), (i) ->
			{
				player.playSound(1083);
				teleport.teleport(player);
				player.sendMessage(Color.WHITE, "你已传送到 %1$s 。", teleport.getName());
			})
			// FIXME
			// parentDialog instanceof TeleportMainDialog == false

			.item(stringSet.get("Dialog.TeleportDialog.Finish"))
			// FIXME
			// parentDialog instanceof TeleportMainDialog

			.build();
	}
}
