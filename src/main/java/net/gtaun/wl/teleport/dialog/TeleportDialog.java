package net.gtaun.wl.teleport.dialog;

import java.util.NoSuchElementException;
import java.util.Scanner;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.common.dialog.AbstractInputDialog;
import net.gtaun.wl.common.dialog.AbstractListDialog;
import net.gtaun.wl.common.dialog.MsgboxDialog;
import net.gtaun.wl.teleport.Teleport;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

import org.apache.commons.lang3.time.DateFormatUtils;

public class TeleportDialog extends AbstractListDialog
{
	public TeleportDialog(final Player player, final Shoebill shoebill, final EventManager eventManager, final AbstractDialog parentDialog, final TeleportServiceImpl teleportService, final Teleport teleport)
	{
		super(player, shoebill, eventManager, parentDialog);
		
		dialogListItems.add(new DialogListItem()
		{
			@Override
			public String toItemString()
			{
				return String.format("名称: %1$s", teleport.getName());
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					show();
					return;
				}
				
				String message = "请输入传送点的新名称，要求长度为 2-24 个字之间:";
				new TeleportNamingDialog(player, shoebill, eventManager, TeleportDialog.this, "修改传送点名称", message, teleportService)
				{
					@Override
					protected void onNaming(String name)
					{
						try
						{
							teleport.rename(name);
							showParentDialog();
						}
						catch (IllegalArgumentException | AlreadyExistException e)
						{
							append = "{FF0000}* 由于数据错误而无法更改名称，请重试。";
							show();
						}
					}
				}.show();
			}
		});
		
		dialogListItems.add(new DialogListItem()
		{
			@Override
			public String toItemString()
			{
				return String.format("创建者: %1$s", teleport.getCreater());
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				show();
			}
		});

		dialogListItems.add(new DialogListItem()
		{
			@Override
			public String toItemString()
			{
				AngledLocation loc = teleport.getLocation();
				return String.format("坐标: x=%1$1.2f, y=%2$1.2f, z=%3$1.2f, interior=%4$d", loc.getX(), loc.getY(), loc.getZ(), loc.getInteriorId());
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					show();
					return;
				}

				AngledLocation oldLoc = teleport.getLocation();
				String msg = String.format("当前坐标值为: x=%1$1.2f, y=%2$1.2f z=%3$1.2f interior=%4$d\n请输入新坐标值，格式: [x] [y] [z] [interior]", oldLoc.getX(), oldLoc.getY(), oldLoc.getZ(), oldLoc.getInteriorId());
				new AbstractInputDialog(player, shoebill, eventManager, TeleportDialog.this, "编辑传送点坐标", msg)
				{
					public void onClickOk(String inputText)
					{
						player.playSound(1083, player.getLocation());
						
						try (Scanner scanner = new Scanner(inputText))
						{
							AngledLocation location = new AngledLocation(teleport.getLocation());
							location.set(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat());
							teleport.updateLocation(location);
							showParentDialog();
						}
						catch (NoSuchElementException e)
						{
							append = "{FF0000}* 请按照正确的格式输入坐标值。";
							show();
						}
					}
				}.show();
			}
		});
		
		dialogListItems.add(new DialogListItem()
		{
			@Override
			public String toItemString()
			{
				AngledLocation loc = teleport.getLocation();
				return String.format("角度: %1$1.1f", loc.getAngle());
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				if (!player.getName().equalsIgnoreCase(teleport.getCreater()))
				{
					show();
					return;
				}
				
				String msg = String.format("当前角度值为: %1$1.1f\n请输入新角度值:", teleport.getLocation().getAngle());
				new AbstractInputDialog(player, shoebill, eventManager, TeleportDialog.this, "编辑传送点角度", msg)
				{
					private String append;
					
					public void onClickOk(String inputText)
					{
						player.playSound(1083, player.getLocation());
						
						try (Scanner scanner = new Scanner(inputText))
						{
							AngledLocation location = new AngledLocation(teleport.getLocation());
							location.setAngle(scanner.nextFloat());
							teleport.updateLocation(location);
							showParentDialog();
						}
						catch (NoSuchElementException e)
						{
							append = "{FF0000}* 请按照正确的格式输入角度值。";
							show();
						}
					}
					
					protected void show(String text)
					{
						if (append != null) super.show(this.message + "\n\n" + append);
						else super.show(text);
					}
				}.show();
			}
		});
		
		dialogListItems.add(new DialogListItem()
		{
			@Override
			public String toItemString()
			{
				return String.format("更新日期: %1$s", DateFormatUtils.ISO_DATETIME_FORMAT.format(teleport.getUpdateDate()));
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				show();
			}
		});
		
		dialogListItems.add(new DialogListItem()
		{
			@Override
			public String toItemString()
			{
				return String.format("人气: %1$d", teleport.getTeleportCounter());
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				show();
			}
		});
		
		dialogListItems.add(new DialogListItem("更新坐标到当前位置")
		{
			@Override
			public boolean isEnabled()
			{
				return player.getName().equalsIgnoreCase(teleport.getCreater());
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				
				String message = String.format("您确认要将传送点 {0000AF}%1$s{A9C4E4} 的位置更新到当前位置吗？", teleport.getName());
				new MsgboxDialog(player, shoebill, eventManager, TeleportDialog.this, "更新传送点坐标", message)
				{
					protected void onClickOk()
					{
						player.playSound(1083, player.getLocation());
						teleport.updateLocation(player.getLocation());
						showParentDialog();
					}
				}.show();
			}
		});
		
		dialogListItems.add(new DialogListItem("删除传送点")
		{
			@Override
			public boolean isEnabled()
			{
				return player.getName().equalsIgnoreCase(teleport.getCreater());
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				
				String message = String.format("您确认要删除传送点 {0000AF}%1$s{A9C4E4} 吗？\n警告，此删除操作无法恢复！！", teleport.getName());
				new MsgboxDialog(player, shoebill, eventManager, TeleportDialog.this, "更新传送点坐标", message)
				{
					protected void onClickOk()
					{
						player.playSound(1083, player.getLocation());
						teleport.delete();
						TeleportDialog.this.showParentDialog();
					}
				}.show();
			}
		});
		
		dialogListItems.add(new DialogListItem("传送")
		{
			@Override
			public boolean isEnabled()
			{
				return parentDialog instanceof TeleportMainDialog == false;
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
				teleport.teleport(player);
				player.sendMessage(Color.WHITE, "你已传送到 %1$s 。", teleport.getName());
			}
		});
		
		dialogListItems.add(new DialogListItem("完成")
		{
			@Override
			public boolean isEnabled()
			{
				return parentDialog instanceof TeleportMainDialog;
			}
			
			@Override
			public void onItemSelect()
			{
				player.playSound(1083, player.getLocation());
			}
		});
	}
}
