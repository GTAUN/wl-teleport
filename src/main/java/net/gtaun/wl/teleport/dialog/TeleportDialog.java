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
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

import org.apache.commons.lang3.time.DateFormatUtils;

public class TeleportDialog
{
	public static WlListDialog create
	(Player player, EventManager eventManager, AbstractDialog parent, TeleportServiceImpl service, Teleport teleport)
	{
		return WlListDialog.create(player, eventManager)
			.parentDialog(parent)
			
			.item(() -> String.format("名称: %1$s", teleport.getName()), (i) ->
			{
				player.playSound(1083);
				
				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					i.getCurrentDialog().show();
					return;
				}
				
				String message = "请输入传送点的新名称，要求长度为 2-24 个字之间:";
				TeleportNamingDialog.create(player, eventManager, i.getCurrentDialog(), "修改传送点名称", message, service, (d, name) ->
				{
					try
					{
						teleport.rename(name);
						d.showParentDialog();
					}
					catch (IllegalArgumentException | AlreadyExistException e)
					{
						d.setAppendMessage("{FF0000}* 由于数据错误而无法更改名称，请重试。");
						d.show();
					}
				}).show();
			})
			
			.item(() -> String.format("创建者: %1$s", teleport.getCreater()), (i) -> i.getCurrentDialog().show())
			
			.item(() ->
			{
				AngledLocation loc = teleport.getLocation();
				return String.format("坐标: x=%1$1.2f, y=%2$1.2f, z=%3$1.2f, interior=%4$d", loc.getX(), loc.getY(), loc.getZ(), loc.getInteriorId());
			}, (i) ->
			{
				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					i.getCurrentDialog().show();
					return;
				}

				AngledLocation oldLoc = teleport.getLocation();
				String msg = String.format("当前坐标值为: x=%1$1.2f, y=%2$1.2f z=%3$1.2f interior=%4$d\n请输入新坐标值，格式: [x] [y] [z] [interior]", oldLoc.getX(), oldLoc.getY(), oldLoc.getZ(), oldLoc.getInteriorId());
				
				WlInputDialog.create(player, eventManager)
					.caption("编辑传送点坐标")
					.message(msg)
					.onClickOk((d, text) ->
					{
						player.playSound(1083, player.getLocation());
						
						try (Scanner scanner = new Scanner(text))
						{
							AngledLocation location = new AngledLocation(teleport.getLocation());
							location.set(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat());
							teleport.updateLocation(location);
							d.showParentDialog();
						}
						catch (NoSuchElementException e)
						{
							((WlInputDialog) d).setAppendMessage("{FF0000}* 请按照正确的格式输入坐标值。");
							d.show();
						}
					})
					.build().show();
			})
			
			.item(() ->
			{
				AngledLocation loc = teleport.getLocation();
				return String.format("角度: %1$1.1f", loc.getAngle());
			}, (i) ->
			{
				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					i.getCurrentDialog().show();
					return;
				}
				
				String msg = String.format("当前角度值为: %1$1.1f\n请输入新角度值:", teleport.getLocation().getAngle());
				WlInputDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption("编辑传送点角度")
					.message(msg)
					.onClickOk((d, text) ->
					{
						player.playSound(1083, player.getLocation());
						
						try (Scanner scanner = new Scanner(text))
						{
							AngledLocation location = new AngledLocation(teleport.getLocation());
							location.setAngle(scanner.nextFloat());
							teleport.updateLocation(location);
							d.showParentDialog();
						}
						catch (NoSuchElementException e)
						{
							((WlInputDialog) d).setAppendMessage("{FF0000}* 请按照正确的格式输入角度值。");
							d.show();
						}
					})
					.build().show();
			})
			
			.item(() -> String.format("更新日期: %1$s", DateFormatUtils.ISO_DATETIME_FORMAT.format(teleport.getUpdateDate())), (i) -> i.getCurrentDialog().show())
			
			.item(() -> String.format("人气: %1$d", teleport.getTeleportCounter()), (i) -> i.getCurrentDialog().show())
			
			.item("更新坐标到当前位置", () -> player.getName().equalsIgnoreCase(teleport.getCreater()), (i) ->
			{
				String message = String.format("您确认要将传送点 {0000AF}%1$s{A9C4E4} 的位置更新到当前位置吗？", teleport.getName());
				WlMsgboxDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption("更新传送点坐标")
					.message(message)
					.onClickOk((d) ->
					{
						player.playSound(1083);
						teleport.updateLocation(player.getLocation());
						d.showParentDialog();
					})
					.build().show();
			})
			
			.item("删除传送点", () -> player.getName().equalsIgnoreCase(teleport.getCreater()), (i) ->
			{
				String message = String.format("您确认要删除传送点 {0000AF}%1$s{A9C4E4} 吗？\n警告，此删除操作无法恢复！！", teleport.getName());
				MsgboxDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption("更新传送点坐标")
					.message(message)
					.onClickOk((d)->
					{
						player.playSound(1083, player.getLocation());
						teleport.delete();
						d.showParentDialog();
					}).build().show();
			})
			
			.item("传送", (i) ->
			{
				player.playSound(1083, player.getLocation());
				teleport.teleport(player);
				player.sendMessage(Color.WHITE, "你已传送到 %1$s 。", teleport.getName());
			})
			// FIXME
			// parentDialog instanceof TeleportMainDialog == false
			
			.item("完成")
			// FIXME
			// parentDialog instanceof TeleportMainDialog
			
			.build();
	}
}
