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

package net.gtaun.wl.teleport;

import java.util.List;

import net.gtaun.shoebill.common.dialog.AbstractDialog;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.resource.Plugin;
import net.gtaun.shoebill.service.Service;

/**
 * 新未来世界传送服务接口。
 * 
 * @author MK124
 */
public interface TeleportService extends Service
{
	Plugin getPlugin();
	
	void showMainDialog(Player player, AbstractDialog parentDialog);
	
	void setCommandEnabled(boolean enable);
	void setCommandOperation(String op);

	boolean hasTeleport(String name);
	Teleport getTeleport(String name);
	boolean teleport(Player player, String name);
	
	List<Teleport> getTeleports();
	List<Teleport> getTeleports(String creater);
	
	Teleport createTeleport(String name, Player creater, AngledLocation location) throws AlreadyExistException;
	void deleteTeleport(Teleport teleport);
}
