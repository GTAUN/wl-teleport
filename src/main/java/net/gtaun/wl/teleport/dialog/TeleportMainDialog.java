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
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

public class TeleportMainDialog
{
	public static WlListDialog create
	(Player player, EventManager eventManager, AbstractDialog parent, TeleportServiceImpl service)
	{
		return WlListDialog.create(player, eventManager)
			.parentDialog(parent)
			.caption("传送和世界系统")
			.item("浏览传送点 ...", (i) -> new TeleportListDialog(player, eventManager, i.getCurrentDialog(), service, service.getTeleports()).show())
			.item("我的传送点 ...", (i) -> new TeleportListDialog(player, eventManager, i.getCurrentDialog(), service, service.getTeleports(player.getName())).show())
			.item("传送点收藏夹 ...", (i) -> i.getCurrentDialog().show())
			.item("创建新传送点", (i) ->
			{
				String message = "请输入新传送点的名称，要求长度为 2-24 个字之间:";
				TeleportNamingDialog.create(player, eventManager, i.getCurrentDialog(), "创建新传送点", message, service, (d, name) ->
				{
					try
					{
						Teleport teleport = service.createTeleport(name, player, player.getLocation());
						TeleportDialog.create(player, eventManager, i.getCurrentDialog(), service, teleport).show();
					}
					catch (AlreadyExistException e)
					{
						d.setAppendMessage("{FF0000}* 无法以此名称创建新传送点，请重试。");
						d.show();
					}
				}).show();
			})
			.item("个人偏好设置 ...", (i) -> i.getCurrentDialog().show())
			.item("帮助信息", (i) ->
			{
				WlMsgboxDialog.create(player, eventManager)
					.parentDialog(i.getCurrentDialog())
					.caption(String.format("%1$s: %2$s", "传送和世界系统", "帮助信息"))
					.message("偷懒中，暂无帮助信息……")
					.build();
			})
			.item("关于传送和世界系统", (i) ->
			{
				Plugin plugin = service.getPlugin();
				ResourceDescription desc = plugin.getDescription();
				
				String caption = String.format("%1$s: %2$s", "传送和世界系统", "关于");
				String format =
					"--- 新未来世界 传送和世界系统组件 ---\n" +
					"版本: %1$s (Build %2$d)\n" +
					"编译时间: %3$s\n\n" +
					"开发: mk124\n" +
					"功能设计: mk124\n" +
					"设计顾问: 52_PLA(aka. Yin.J), [ITC]1314, [ITC]KTS, snwang1996\n" +
					"数据采集: mk124, 52_PLA\n" +
					"测试: 52_PLA, [ITC]1314, [ITC]KTS, SMALL_KR, snwang1996\n" +
					"感谢: 原未来世界制作团队成员(yezhizhu, vvg, fangye), Luck, Waunny\n\n" +
					"本组件是新未来世界项目的一部分。\n" +
					"本组件使用 AGPL v3 许可证开放源代码。\n" +
					"本组件禁止在任何商业或盈利性服务器上使用。\n";
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
