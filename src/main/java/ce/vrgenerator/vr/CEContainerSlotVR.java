package ce.vrgenerator.vr;

import ic2.api.item.IElectricItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * 人工光ソーラーのスロット
 *
 * @author takanasayo
 * @author A.K
 */
public class CEContainerSlotVR extends Slot {
	private final int tier;

	public CEContainerSlotVR(final IInventory iinventory, final int i, final int j, final int k, final int l) {
		super(iinventory, j, k, l);
		this.tier = i;
	}

	public CEContainerSlotVR(final IInventory iinventory, final int i, final int j, final int k) {
		this(iinventory, 0x7fffffff, i, j, k);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return itemstack.getItem() instanceof ItemBlock&&((ItemBlock) itemstack.getItem()).block.getLightValue()>=7||isItemValidCharge(itemstack);
	}

	public boolean isItemValidCharge(final ItemStack itemstack) {
		return itemstack.getItem() instanceof IElectricItem&&((IElectricItem) itemstack.getItem()).getTier(itemstack)<=this.tier;
	}
}
