package ce.vrgenerator.vr.logic;

import java.util.List;

import com.google.common.collect.Lists;

import ce.vrgenerator.CEItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.util.Constants;

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
	//private static int[] lightPower = { 1,2,4,8,16,32,64,128,256,512,1024 };

	private ItemStack inventory[] = new ItemStack[1];

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
		final int lightblock = world.getBlockState(pos).getBlock().getLightValue(world, posup);
		final float light = getSkyLight(world, posup);
		// FMLLog.info("sun:%s, block:%s", light, lightblock);
		return light>0.0F||lightblock>=15;
	}

	public static float getSkyLight(final World world, final BlockPos pos) {
		// FMLLog.getLogger().info(pos);
		if (world.provider.getHasNoSky())
			return 0.0F;
		else {
			float sunBrightness = limit((float) Math.cos(world.getCelestialAngleRadians(1.0F))*2.0F+0.2F, 0.0F, 1.0F);

			if (!BiomeDictionary.isBiomeOfType(world.getChunkFromBlockCoords(pos).getBiome(pos, world.getBiomeProvider()), Type.SANDY)) {
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
			if (stack!=null&&stack.stackSize>0)
				drop.add(stack);
		return drop;
	}

	public void updateSunVisibility() {
		final int maxProduction = getPower().getMaxProduction();
		//真上のブロックが太陽光を浴びていれば（この判定がかなり重い）
		if (isSunVisible(this.worldObj, this.pos)) {
			//通常通り発電(ソーラーと同じ）
			this.production = maxProduction;
			this.isSunVisible = true;
		} else {
			//内部インベントリを調べて
			if (this.inventory[0]!=null) {
				double light = 0;
				final Item item = this.inventory[0].getItem();
				//溶岩なら1/8
				if (item instanceof ItemBlock) {
					final Block block = ((ItemBlock) item).block;
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

		final NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.inventory = new ItemStack[getSizeInventory()];

		for (int i = 0; i<nbttaglist.tagCount(); i++) {
			final NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			final byte byte0 = nbttagcompound1.getByte("Slot");

			if (byte0>=0&&byte0<this.inventory.length)
				this.inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound = super.writeToNBT(nbttagcompound);

		final NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i<this.inventory.length; i++)
			if (this.inventory[i]!=null) {
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}

		nbttagcompound.setTag("Items", nbttaglist);
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
		if (this.worldObj.getTotalWorldTime()%80L==0L)
			updateSunVisibility();
		markDirty();
		if (this.ticksSinceLastActiveUpdate%256==0)
			this.activityMeter = 0;
		this.activityMeter++;
		this.ticksSinceLastActiveUpdate++;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(final int var1) {
		return this.inventory[0];
	}

	@Override
	public ItemStack decrStackSize(final int i, final int j) {
		if (this.inventory[i]!=null) {
			if (this.inventory[i].stackSize<=j) {
				final ItemStack itemstack = this.inventory[i];
				this.inventory[i] = null;
				return itemstack;
			}

			final ItemStack itemstack1 = this.inventory[i].splitStack(j);

			if (this.inventory[i].stackSize==0)
				this.inventory[i] = null;

			return itemstack1;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(final int var1, final ItemStack var2) {
		if (var1<this.inventory.length) {
			this.inventory[var1] = var2;

			if (var2!=null&&var2.stackSize>getInventoryStackLimit())
				var2.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer var1) {
		return this.worldObj.getTileEntity(this.pos)==this&&var1.getDistance(this.pos.getX()+0.5D, this.pos.getY()+0.5D, this.pos.getZ()+0.5D)<=64D;
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

	@Override
	public ItemStack removeStackFromSlot(final int index) {
		if (this.inventory[index]!=null) {
			final ItemStack stack = this.inventory[index];
			this.inventory[index] = null;
			return stack;
		} else
			return null;
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
		for (int i = 0; i<this.inventory.length; ++i)
			this.inventory[i] = null;
	}
}
