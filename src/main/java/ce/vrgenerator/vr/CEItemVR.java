package ce.vrgenerator.vr;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * 発電機ブロックのアイテム
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class CEItemVR extends ItemBlock {
	public CEItemVR(final Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(final int i) {
		return i;
	}

	@Override
	public String getUnlocalizedName(final ItemStack itemstack) {
		final Item item = itemstack.getItem();
		if (item instanceof ItemBlock) {
			final Block block = ((ItemBlock) item).getBlock();
			if (block instanceof CEBlockVR)
				return ((CEBlockVR) block).getTypeFromMeta(itemstack.getItemDamage()).getDisplayName();
		}
		return null;
	}
}
