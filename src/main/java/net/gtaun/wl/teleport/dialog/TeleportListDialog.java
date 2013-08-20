package net.gtaun.wl.teleport.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.common.dialog.AbstractPageListDialog;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

import org.apache.commons.lang3.time.DateFormatUtils;

public class TeleportListDialog extends AbstractPageListDialog
{
	private final List<Teleport> teleports;
	
	private int operation;
	private List<Comparator<Teleport>> sortComparators;
	private Comparator<Teleport> sortComparator;
	
	
	public TeleportListDialog
	(final Player player, Shoebill shoebill, EventManager eventManager, AbstractDialog parentDialog, TeleportServiceImpl teleportService, List<Teleport> teleports)
	{
		super(player, shoebill, eventManager, parentDialog);
		this.teleports = teleports;
		
		sortComparators = new ArrayList<Comparator<Teleport>>();
		sortComparators.add(new Comparator<Teleport>()
		{
			@Override
			public int compare(Teleport o1, Teleport o2)
			{
				Location loc = player.getLocation();
				return (int) (o1.getLocation().distance(loc) - o2.getLocation().distance(loc));
			}
		});
		sortComparators.add(new Comparator<Teleport>()
		{
			@Override
			public int compare(Teleport o1, Teleport o2)
			{
				return o2.getTeleportCounter() - o1.getTeleportCounter();
			}
		});
		sortComparators.add(new Comparator<Teleport>()
		{
			@Override
			public int compare(Teleport o1, Teleport o2)
			{
				return (int) (o2.getUpdateDate().getTime()/1000 - o1.getUpdateDate().getTime()/1000);
			}
		});
		
		sortComparator = sortComparators.get(0);
		update();
	}
	
	private void update()
	{
		dialogListItems.clear();
		dialogListItems.add(new DialogListItemRadio("操作: ")
		{
			{
				addItem(new RadioItem("传送", Color.LIGHTBLUE));
				addItem(new RadioItem("查看选项", Color.LIGHTPINK));
			}
			
			@Override
			public int getSelected()
			{
				return operation;
			}
			
			@Override
			public void onItemSelect(RadioItem item, int index)
			{
				player.playSound(1083, player.getLocation());
				operation = index;
				show();
			}
		});
		
		dialogListItems.add(new DialogListItemRadio("排序方式: ")
		{
			{
				addItem(new RadioItem("距离最近", Color.RED));
				addItem(new RadioItem("人气最高", Color.GREEN));
				addItem(new RadioItem("最近更新", Color.YELLOW));
			}
			
			@Override
			public int getSelected()
			{
				return sortComparators.indexOf(sortComparator);
			}
			
			@Override
			public void onItemSelect(RadioItem item, int index)
			{
				player.playSound(1083, player.getLocation());
				sortComparator = sortComparators.get(index);
				show();
			}
		});
		
		for (Teleport teleport : teleports)
		{
			String item = String.format("%1$s	{808080}创建者: %2$s, 人气: %3$d, 更新日期: %4$s",
				teleport.getName(), teleport.getCreater(), teleport.getTeleportCounter(), DateFormatUtils.ISO_DATE_FORMAT);
			
			dialogListItems.add(new DialogListItem(item)
			{	
				@Override
				public void onItemSelect()
				{
					
				}
			});
		}
	}
}
