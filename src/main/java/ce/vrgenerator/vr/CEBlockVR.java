package ce.vrgenerator.vr;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import ce.vrgenerator.CEItems;
import ce.vrgenerator.VisibleRayGenerator;
import ce.vrgenerator.vr.logic.CETileEntityVR;
import ce.vrgenerator.vr.logic.CETileEntityVRCEPermanentLight;
import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 発電機ブロック
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class CEBlockVR extends BlockContainer implements IWrenchable {
	public static final PropertyEnum<CEVRType> TYPE = PropertyEnum.create("type", CEVRType.class);

	public CEBlockVR() {
		super(Material.iron);
		setRegistryName("CE_Solar");
		setDefaultState(this.blockState.getBaseState().withProperty(TYPE, CEVRType.VisibleRay1));
		setHardness(3F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public String getUnlocalizedName() {
		return "vrgenerator.visibleraysolar";
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public Item getItemDropped(final IBlockState state, final Random random, final int j) {
		return CEItems.IC2.casing.getItem();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return CEItems.IC2.casing.getItemDamage();
	}

	@Override
	public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final BlockPos pos, final EntityPlayer player) {
		return getItemStackFromState(world.getBlockState(pos));
	}

	@Override
	public int quantityDropped(final Random random) {
		return 1;
	}

	@Override
	public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer entityplayer, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final TileEntity tile = world.getTileEntity(pos);
		if (tile==null)
			return false;
		if (tile instanceof CETileEntityVRCEPermanentLight)
			return false;

		final ItemStack itemstackequip = entityplayer.getCurrentEquippedItem();
		if (itemstackequip!=null) {
			final Item itemequip = itemstackequip.getItem();
			if (itemequip==CEItems.IC2.wrench.getItem()||itemequip==CEItems.IC2.electricwrench.getItem())
				return false;
			else if (itemequip==CEItems.IC2.ecmeter.getItem())
				return false;
		}
		if (entityplayer.isSneaking())
			return false;
		entityplayer.openGui(VisibleRayGenerator.instance, VisibleRayGenerator.artificialSunGuiID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
		final CETileEntityVR tile = (CETileEntityVR) world.getTileEntity(pos);
		if (tile!=null) {
			if (tile instanceof CETileEntityVR)
				for (final ItemStack ist : tile.getAdditionalDrops())
					// アイテムエンティティを散らかします
					spawnAsEntity(world, pos, ist);
			tile.onChunkUnload();
		}
		super.breakBlock(world, pos, state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(final Item item, final CreativeTabs tabs, final List<ItemStack> list) {
		for (final CEVRType ce : CEVRType.values())
			list.add(ce.createItemStack(1));
	}

	public CEVRType getTypeFromMeta(final int type) {
		return CEVRType.fromID(type);
	}

	public CEVRType getTypeFromState(final IBlockState state) {
		return state.getValue(TYPE);
	}

	public IBlockState getStateFromType(final CEVRType type) {
		return getDefaultState().withProperty(TYPE, type);
	}

	@Override
	public IBlockState getStateFromMeta(final int type) {
		return getStateFromType(getTypeFromMeta(type));
	}

	public int getMetaFromType(final CEVRType type) {
		return type.getID();
	}

	public ItemStack getItemStackFromState(final IBlockState state) {
		final Block block = state.getBlock();
		final int meta = block.getMetaFromState(state);
		return new ItemStack(block, 1, meta);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return getMetaFromType(state.getValue(TYPE));
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, TYPE);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int type) {
		return getTypeFromMeta(type).createTileEntity();
	}

	@Override
	public TileEntity createTileEntity(final World world, final IBlockState state) {
		return createNewTileEntity(world, getMetaFromState(state));
	}

	@Override
	public EnumFacing getFacing(final World world, final BlockPos pos) {
		return EnumFacing.UP;
	}

	@Override
	public boolean setFacing(final World world, final BlockPos pos, final EnumFacing newDirection, final EntityPlayer player) {
		return false;
	}

	@Override
	public boolean wrenchCanRemove(final World world, final BlockPos pos, final EntityPlayer player) {
		return true;
	}

	/**
	 * レンチで100％アイテム化（製造コストが高すぎるための処置）
	 */
	@Override
	public List<ItemStack> getWrenchDrops(final World world, final BlockPos pos, final IBlockState state, final TileEntity te, final EntityPlayer player, final int fortune) {
		return Lists.newArrayList(getItemStackFromState(state));
	}
}
