package ce.vrgenerator.vr;

import ce.vrgenerator.vr.logic.CETileEntityVRVisibleRay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
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
	public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);

		if (slot!=null&&slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index<1) {
				if (!mergeItemStack(itemstack1, 1, 37, true))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(itemstack1, 0, 9, false))
				return ItemStack.EMPTY;

			if (itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			if (itemstack1.getCount()==itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i<this.listeners.size(); i++) {
			final IContainerListener icrafting = this.listeners.get(i);

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
		return this.tileEntity.isUsableByPlayer(entityplayer);
	}

	public int guiInventorySize() {
		return 1;
	}

	public int getInput() {
		return 0;
	}
}
