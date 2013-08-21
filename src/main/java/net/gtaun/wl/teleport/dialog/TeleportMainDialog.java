package net.gtaun.wl.teleport.dialog;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.resource.Plugin;
import net.gtaun.shoebill.resource.ResourceDescription;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.common.dialog.AbstractListDialog;
import net.gtaun.wl.common.dialog.MsgboxDialog;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

public class TeleportMainDialog extends AbstractListDialog
{
	public TeleportMainDialog(final Player player, final Shoebill shoebill, final EventManager eventManager, AbstractDialog parentDialog, final TeleportServiceImpl teleportService)
	{
		super(player, shoebill, eventManager, parentDialog);
		this.caption = "传送和世界系统";
		
		dialogListItems.add(new DialogListItem("浏览传送点 ...")
		{
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				new TeleportListDialog(player, shoebill, eventManager, TeleportMainDialog.this, teleportService, teleportService.getTeleports()).show();
			}
		});
		
		dialogListItems.add(new DialogListItem("我的传送点 ...")
		{
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				new TeleportListDialog(player, shoebill, eventManager, TeleportMainDialog.this, teleportService, teleportService.getTeleports(player.getName())).show();
			}
		});
		
		dialogListItems.add(new DialogListItem("传送点收藏夹 ...")
		{
			@Override
			public void onItemSelect()
			{
				
			}
		});
		
		dialogListItems.add(new DialogListItem("在当前位置创建新传送点")
		{
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				
				String message = "请输入新传送点的名称，要求长度为 2-24 个字之间:";
				new TeleportNamingDialog(player, shoebill, eventManager, TeleportMainDialog.this, "创建新传送点", message, teleportService)
				{
					@Override
					protected void onNaming(String name)
					{
						Teleport teleport;
						try
						{
							teleport = teleportService.createTeleport(name, player, player.getLocation());
							new TeleportDialog(player, shoebill, eventManager, TeleportMainDialog.this, teleportService, teleport).show();
						}
						catch (AlreadyExistException e)
						{
							append = "{FF0000}* 无法以此名称创建新传送点，请重试。";
							show();
						}
					}
				}.show();
			}
		});
		
		dialogListItems.add(new DialogListItem("个人偏好设置 ...")
		{
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
			}
		});
		
		dialogListItems.add(new DialogListItem("帮助信息")
		{
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				String caption = String.format("%1$s: %2$s", "传送和世界系统", "帮助信息");
				new MsgboxDialog(player, shoebill, eventManager, TeleportMainDialog.this, caption, "偷懒中，暂无帮助信息……").show();
			}
		});
		
		dialogListItems.add(new DialogListItem("关于传送和世界系统")
		{
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				
				Plugin plugin = teleportService.getPlugin();
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
					"本组件使用 GPL v2 许可证开放源代码。\n" +
					"本组件禁止在任何商业或盈利性服务器上使用。\n";
				String message = String.format(format, desc.getVersion(), desc.getBuildNumber(), desc.getBuildDate());
				
				new MsgboxDialog(player, shoebill, eventManager, TeleportMainDialog.this, caption, message).show();
			}
		});
	}
}
