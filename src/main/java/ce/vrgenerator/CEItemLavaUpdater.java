package ce.vrgenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * 溶岩更新機
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class CEItemLavaUpdater extends Item {
	public CEItemLavaUpdater() {
		super();
		setRegistryName("lavaupdater");
		setMaxDamage(132);
		setMaxStackSize(1);
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		return "vrgenerator.lavaupdater";
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
		final int px = (int) player.posX;
		final int py = (int) player.posY;
		final int pz = (int) player.posZ;

		final int LavaUpdateArea = VisibleRayGenerator.LavaUpdateArea;

		//mod_CompactEngine.addChat("start lava update %d,%d,%d %d", px, py, pz, LavaUpdateArea);
		for (int y = py+LavaUpdateArea; y>=py-LavaUpdateArea; y--)
			for (int z = pz-LavaUpdateArea; z<=pz+LavaUpdateArea; z++)
				for (int x = px-LavaUpdateArea; x<=px+LavaUpdateArea; x++) {
					final BlockPos pos = new BlockPos(x, y, z);
					final IBlockState state = world.getBlockState(pos);
					if (state.getBlock().getMaterial()==CEItems.Vanilla.materialLava) {
						final int meta = CEItems.Vanilla.blocklava.getMetaFromState(state);
						if (meta!=0)
							//mod_CompactEngine.addChat("update lava %d,%d,%d", x, y, z);
							if (meta<7) {
							world.setBlockState(pos, CEItems.Vanilla.blocklava.getStateFromMeta(meta+1), 3);
							world.scheduleUpdate(pos, CEItems.Vanilla.blocklava, 1);
							} else
							world.setBlockToAir(pos);
					}
				}
		//mod_CompactEngine.addChat("end lava update");
		stack.damageItem(1, player);
		return stack;
	}
}