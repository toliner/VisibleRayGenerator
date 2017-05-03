package ce.vrgenerator.vr;

import ce.vrgenerator.vr.logic.CETileEntityVRVisibleRay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * 人工光ソーラーのコンテナー
 *
 * @author takanasayo
 * @author A.K
 */
public class CEContainerVR extends Container {
	private final CETileEntityVRVisibleRay tileEntity;
	private boolean sunIsVisible;
	private boolean initialized;

	public CEContainerVR(final EntityPlayer entityplayer, final CETileEntityVRVisibleRay solar) {
		//現在の発電量を更新するために、あちこちに判定追加
		this.sunIsVisible = false;
		this.initialized = false;
		this.tileEntity = solar;
		addSlotToContainer(new CEContainerSlotVR(solar, solar.getPower().getTier(), 0, 80, 26));

		for (int i = 0; i<3; i++)
			for (int k = 0; k<9; k++)
				addSlotToContainer(new Slot(entityplayer.inventory, k+i*9+9, 8+k*18, 84+i*18));

		for (int j = 0; j<9; j++)
			addSlotToContainer(new Slot(entityplayer.inventory, j, 8+j*18, 142));
	}

	@Override
	public void onCraftMatrixChanged(final IInventory par1IInventory) {
		this.tileEntity.updateSunVisibility();
		this.sunIsVisible = this.tileEntity.isSunVisible();
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
		ItemStack var3 = null;
		final Slot var4 = this.inventorySlots.get(par2);

		if (var4!=null&&var4.getHasStack()) {
			final ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2==0) {
				if (!mergeItemStack(var5, 1, 37, true))
					return null;

				var4.onSlotChange(var5, var3);
			} else if (!mergeItemStack(var5, 0, 1, false))
				return null;

			if (var5.stackSize==0)
				var4.putStack((ItemStack) null);
			else
				var4.onSlotChanged();

			if (var5.stackSize==var3.stackSize)
				return null;

			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;
	}

	@Override
	public ItemStack slotClick(final int slot, final int button, final int shift, final EntityPlayer entityplayer) {
		ItemStack result = null;
		if (slot!=0&&shift==1) {
			final Slot topslot = this.inventorySlots.get(0);
			final Slot currentSlot = this.inventorySlots.get(slot);
			ItemStack stack = currentSlot.getStack();
			if (topslot.getStack()==null&&stack!=null&&topslot.isItemValid(stack)) {
				final ItemStack topstack = stack.copy();
				topstack.stackSize = 1;
				if (--stack.stackSize<=0) {
					stack = null;
					currentSlot.putStack(stack);
				}

				topslot.putStack(topstack);
				result = stack;
			}
		} else
			result = super.slotClick(slot, button, shift, entityplayer);
		this.tileEntity.updateSunVisibility();
		this.sunIsVisible = this.tileEntity.isSunVisible();
		return result;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i<this.crafters.size(); i++) {
			final ICrafting icrafting = this.crafters.get(i);

			if (this.sunIsVisible!=this.tileEntity.isSunVisible()||!this.initialized) {
				icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.isSunVisible() ? 1 : 0);
				this.initialized = true;
			}
		}

		this.sunIsVisible = this.tileEntity.isSunVisible();
	}

	@Override
	public void updateProgressBar(final int i, final int j) {
		switch (i) {
			case 0:
				this.tileEntity.setSunVisible(j!=0);
				break;
		}
	}

	@Override
	public boolean canInteractWith(final EntityPlayer entityplayer) {
		return this.tileEntity.isUseableByPlayer(entityplayer);
	}

	public int guiInventorySize() {
		return 1;
	}

	public int getInput() {
		return 0;
	}
}
