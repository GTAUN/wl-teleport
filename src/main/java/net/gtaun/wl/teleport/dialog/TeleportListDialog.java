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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.common.dialog.ListDialogItemRadio;
import net.gtaun.shoebill.common.dialog.PageListDialog;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

import org.apache.commons.lang3.time.DateFormatUtils;

public class TeleportListDialog extends PageListDialog
{
	private final TeleportServiceImpl teleportService;
	private final List<Teleport> teleports;

	private int operation;
	private List<Comparator<Teleport>> sortComparators;
	private Comparator<Teleport> sortComparator;


	public TeleportListDialog
	(Player player, EventManager eventManager, AbstractDialog parentDialog, TeleportServiceImpl teleportService, List<Teleport> teleports)
	{
		super(player, eventManager);
		setParentDialog(parentDialog);

		this.teleportService = teleportService;
		this.teleports = teleports;

		sortComparators = new ArrayList<Comparator<Teleport>>();
		sortComparators.add((o1, o2) ->
		{
			Location loc = player.getLocation();
			return (int) (o1.getLocation().distance(loc) - o2.getLocation().distance(loc));
		});
		sortComparators.add((o1, o2) -> o2.getTeleportCounter() - o1.getTeleportCounter());
		sortComparators.add((o1, o2) -> (int) (o2.getUpdateDate().getTime()/1000 - o1.getUpdateDate().getTime()/1000));

		sortComparator = sortComparators.get(0);
		update();
	}

	private void update()
	{
		items.clear();
		addItem(ListDialogItemRadio.create()
			.itemText("操作:")
			.selectedIndex(() -> operation)
			.item("传送", Color.LIGHTBLUE)
			.item("查看选项", Color.LIGHTPINK)
			.onRadioItemSelect((dialogItem, item, index) ->
			{
				player.playSound(1083);
				operation = index;
				show();
			})
			.build());

		addItem(ListDialogItemRadio.create()
			.itemText("排序方式:")
			.selectedIndex(() -> sortComparators.indexOf(sortComparator))
			.item("距离最近", Color.RED)
			.item("人气最高", Color.GREEN)
			.item("最近更新", Color.YELLOW)
			.onRadioItemSelect((dialogItem, item, index) ->
			{
				player.playSound(1083);
				sortComparator = sortComparators.get(index);
				show();
			})
			.build());

		for (Teleport teleport : teleports)
		{
			String name = teleport.getName();
			String creater = teleport.getCreater();
			int counter = teleport.getTeleportCounter();
			String date = DateFormatUtils.ISO_DATE_FORMAT.format(teleport.getUpdateDate());

			String item = String.format
			(
				"%1$s	{808080}创建者: %2$s, 人气: %3$d, 更新日期: %4$s",
				name, creater, counter, date
			);

			addItem(item, (i) ->
			{
				player.playSound(1083);
				switch (operation)
				{
				case 0:
					teleport.teleport(player);
					break;

				case 1:
					TeleportDialog.create(player, eventManagerNode.getParent(), TeleportListDialog.this, teleportService, teleport).show();
					break;

				default:
					show();
					break;
				}
			});
		}
	}
}
