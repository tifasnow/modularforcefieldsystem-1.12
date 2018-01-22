/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    
    Thunderdark
    Matchlighter

 */

package com.nekokittygames.mffs.common;

import java.util.ArrayList;
import java.util.List;

import com.nekokittygames.mffs.common.tileentity.TileEntitySecStorage;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryHelper {

	public static IInventory findAttachedInventory(World worldObj, BlockPos pos) {
		List<TileEntity> tes = new ArrayList<TileEntity>();

		tes.add(worldObj.getTileEntity(pos.add(1,0,0)));
		tes.add(worldObj.getTileEntity(pos.add(-1,0,0)));
		tes.add(worldObj.getTileEntity(pos.add(0,1,0)));
		tes.add(worldObj.getTileEntity(pos.add(0,-1,0)));
		tes.add(worldObj.getTileEntity(pos.add(0,0,1)));
		tes.add(worldObj.getTileEntity(pos.add(0,0,-1)));

		IInventory inv = null;

		for (TileEntity te : tes) {
			if (te instanceof IInventory)
				if (inv == null
						|| inv.getSizeInventory() < ((IInventory) te)
								.getSizeInventory()) // Find the largest
					inv = (IInventory) te;
		}
		return inv;
	}

	public static boolean addStacksToInventory(IInventory inventory,
			List<ItemStack> itemstacks) {

		int count = 0;

		if (inventory instanceof TileEntitySecStorage)
			count = 1;

		for (int a = count; a <= inventory.getSizeInventory() - 1; a++) {
			ItemStack inventorystack = inventory.getStackInSlot(a);

			for (ItemStack items : itemstacks) {
				if (items != null) {
					if (inventorystack != null) {
						if (inventorystack.getItem() == items.getItem()
								&& inventorystack.getItemDamage() == items
										.getItemDamage()
								&& inventorystack.getCount() + 1 <= inventorystack
										.getMaxStackSize()
								&& inventorystack.getCount() + 1 <= inventory
										.getInventoryStackLimit()) {
							inventorystack.setCount(inventorystack.getCount()+1);

							items.setCount(items.getCount()-1);
							return true;
						}
					} else {
						inventorystack = items.copy();
						inventorystack.setCount(1);
						items.setCount(items.getCount()-1);
						inventory.setInventorySlotContents(a, inventorystack);

						return true;
					}
				}
			}
		}
		return false;
	}

}
