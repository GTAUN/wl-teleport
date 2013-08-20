package net.gtaun.wl.teleport.dialog;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.wl.common.dialog.AbstractListDialog;
import net.gtaun.wl.teleport.impl.TeleportServiceImpl;

public class TeleportCreateDialog extends AbstractListDialog
{
	public TeleportCreateDialog(Player player, Shoebill shoebill, EventManager eventManager, AbstractDialog parentDialog, TeleportServiceImpl teleportService)
	{
		super(player, shoebill, eventManager, parentDialog);
	}
	
	
}
