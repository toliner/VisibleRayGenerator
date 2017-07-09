package ce.vrgenerator.vr.logic;

import java.util.List;

import com.google.common.collect.Lists;

import ce.vrgenerator.CEItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

/**
 * 人工光ソーラータイルエンティティロジック
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class CETileEntityVRVisibleRay extends CETileEntityVR implements IInventory, ISidedInventory {
	private boolean isSunVisible;
	// private int ticker;
	// private boolean initialized;

	// 光レベル計算用
	// private static int[] lightPower = { 1,2,4,8,16,32,64,128,256,512,1024 };

	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack> withSize(1, ItemStack.EMPTY);
	// private ItemStack inventory[] = new ItemStack[1];

	private int production;

	public CETileEntityVRVisibleRay(final CEPowerVR power) {
		super(power);
	}

	public CETileEntityVRVisibleRay() {
	}

	@Override
	public int getProduction() {
		return this.production;
	}

	public boolean isSunVisible() {
		return this.isSunVisible;
	}

	public void setSunVisible(final boolean isSunVisible) {
		this.isSunVisible = isSunVisible;
	}

	//	public String getGuiClassName(EntityPlayer entityplayer)
	//	{
	//		return "CESolarGui";
	//	}
	//
	//	public String getGuiName()
	//	{
	//		return String.format("CE.block.Solar.%d.name", level);
	//	}
	//
	//	public int tickRate()
	//	{
	//		return 128;
	//	}

	public static boolean isSunVisible(final World world, final BlockPos pos) {
		final BlockPos posup = pos.up();
		world.calculateInitialSkylight();
		final int lightblock = world.getBlockState(posup).getLightValue(world, posup);
		final float light = getSkyLight(world, posup);
		// FMLLog.info("sun:%s, block:%s", light, lightblock);
		return light>0.0F||lightblock>=15;
	}

	public static float getSkyLight(final World world, final BlockPos pos) {
		// FMLLog.getLogger().info(pos);
		if (!world.provider.hasSkyLight())
			return 0.0F;
		else {
			float sunBrightness = limit((float) Math.cos(world.getCelestialAngleRadians(1.0F))*2.0F+0.2F, 0.0F, 1.0F);

			if (!BiomeDictionary.hasType(world.getChunkFromBlockCoords(pos).getBiome(pos, world.getBiomeProvider()), Type.SANDY)) {
				sunBrightness *= 1.0F-world.getRainStrength(1.0F)*5.0F/16.0F;
				sunBrightness *= 1.0F-world.getThunderStrength(1.0F)*5.0F/16.0F;

				sunBrightness = limit(sunBrightness, 0.0F, 1.0F);
			}

			return world.getLightFor(EnumSkyBlock.SKY, pos)/15.0F*sunBrightness;
		}
	}

	private static float limit(final float value, final float min, final float max) {
		return !Float.isNaN(value)&&value>min ? value>=max ? max : value : min;
	}

	@Override
	public List<ItemStack> getAdditionalDrops() {
		final List<ItemStack> drop = Lists.newArrayList();
		for (final ItemStack stack : this.inventory)
			if (stack!=null&&stack.getCount()>0)
				drop.add(stack);
		return drop;
	}

	public void updateSunVisibility() {
		final int maxProduction = getPower().getMaxProduction();
		//真上のブロックが太陽光を浴びていれば（この判定がかなり重い）
		if (isSunVisible(this.world, this.pos)) {
			//通常通り発電(ソーラーと同じ）
			this.production = maxProduction;
			this.isSunVisible = true;
		} else {
			//内部インベントリを調べて
			final ItemStack stack = this.inventory.get(0);
			if (!stack.isEmpty()) {
				double light = 0;
				final Item item = stack.getItem();
				//溶岩なら1/8
				if (item instanceof ItemBlock) {
					final Block block = ((ItemBlock) item).getBlock();
					final int blocklight = CEItems.getItemLightLevel(item);
					if (block==CEItems.Vanilla.blocklava||block==CEItems.Vanilla.blockflowinglava)
						light = maxProduction/8.0d;
					else if (blocklight==15)
						light = maxProduction/2.0d;
					else if (blocklight==14)
						light = maxProduction/4.0d;
					else if (blocklight>=7)
						light = maxProduction/16.0d;
				}

				//トータル発電量がプラスなら
				if (light>0.0d) {
					//最低1EU/t発電
					this.production = light<1.0d ? 1 : (int) light;
					this.isSunVisible = true;
					return;
				}
			}
			this.production = 0;
			this.isSunVisible = false;
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		ItemStackHelper.loadAllItems(nbttagcompound, this.inventory);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound = super.writeToNBT(nbttagcompound);
		ItemStackHelper.saveAllItems(nbttagcompound, this.inventory);
		return nbttagcompound;
	}

	//	public String getGuiClassName(EntityPlayer entityplayer)
	//	{
	//		return "CESolarGui";
	//	}
	//
	//	public String getGuiName()
	//	{
	//		return String.format("CE.block.Solar.%d.name", level);
	//	}
	//
	//	public int tickRate()
	//	{
	//		return 128;
	//	}

	//不要な処理を削除して軽量化
	@Override
	public void update() {
		super.update();
		if (this.world.getTotalWorldTime()%80L==0L)
			updateSunVisibility();
		markDirty();
		if (this.ticksSinceLastActiveUpdate%256==0)
			this.activityMeter = 0;
		this.activityMeter++;
		this.ticksSinceLastActiveUpdate++;
	}

	@Override
	public ItemStack decrStackSize(final int index, final int count) {
		final ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, index, count);

		if (!itemstack.isEmpty())
			markDirty();

		return itemstack;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.size();
	}

	@Override
	public ItemStack getStackInSlot(final int var1) {
		return this.inventory.get(0);
	}

	@Override
	public void setInventorySlotContents(final int index, final ItemStack stack) {
		this.inventory.set(index, stack);

		if (stack.getCount()>getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());

		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUsableByPlayer(final EntityPlayer var1) {
		return this.world.getTileEntity(this.pos)==this&&var1.getDistance(this.pos.getX()+0.5D, this.pos.getY()+0.5D, this.pos.getZ()+0.5D)<=64D;
	}

	@Override
	public void openInventory(final EntityPlayer player) {
	}

	@Override
	public void closeInventory(final EntityPlayer player) {
	}

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return new int[] { 0 };
	}

	@Override
	public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
		return CEItems.getItemLightLevel(itemstack.getItem())>0;
	}

	@Override
	public boolean canInsertItem(final int i, final ItemStack itemstack, final EnumFacing direction) {
		return CEItems.getItemLightLevel(itemstack.getItem())>0;
	}

	@Override
	public boolean canExtractItem(final int i, final ItemStack itemstack, final EnumFacing direction) {
		return true;
	}

	@Override
	public String getName() {
		return String.format("CE Solar %d", this.production);
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(final int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public int getField(final int id) {
		return 0;
	}

	@Override
	public void setField(final int id, final int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public boolean isEmpty() {
		for (final ItemStack itemstack : this.inventory)
			if (!itemstack.isEmpty())
				return false;

		return true;
	}
}
