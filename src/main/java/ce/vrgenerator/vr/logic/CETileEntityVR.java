package ce.vrgenerator.vr.logic;

import java.util.List;

import com.google.common.collect.Lists;

import ce.vrgenerator.vr.CEBlockVR;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;

/**
 * 発電機タイルエンティティロジック
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public abstract class CETileEntityVR extends TileEntity implements IEnergySource, ITickable {
	/**
	 * 一度ずつ交互にチャンクロード/アンロードを発生させます
	 */
	private boolean addedToEnergyNet = false;

	protected int activityMeter = 0;
	protected int ticksSinceLastActiveUpdate = 0;

	/** 定義データ */
	private CEPowerVR power;

	protected CETileEntityVR(final CEPowerVR power) {
		this.power = power;
	}

	protected CETileEntityVR() {
	}

	public List<ItemStack> getAdditionalDrops() {
		return Lists.newArrayList();
	}

	//行う処理は送電のみの超軽量設計
	@Override
	public void update() {
		if (!this.addedToEnergyNet) {
			if (!this.worldObj.isRemote) {
				final EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(event);
			}
			this.addedToEnergyNet = true;
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		onChunkUnload();
	}

	@Override
	public void onChunkUnload() {
		if (this.addedToEnergyNet) {
			final EnergyTileUnloadEvent event = new EnergyTileUnloadEvent(this);
			MinecraftForge.EVENT_BUS.post(event);
			this.addedToEnergyNet = false;
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.activityMeter = nbttagcompound.getInteger("activitymeter");
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("activitymeter", this.activityMeter);
	}

	public int getProduction() {
		return getPower().getMaxProduction();
	}

	@Override
	public double getOfferedEnergy() {
		return getProduction();
	}

	@Override
	public void drawEnergy(final double amount) {

	}

	@Override
	public int getSourceTier() {
		return getPower().getTier();
	}

	@Override
	public boolean emitsEnergyTo(final IEnergyAcceptor receiver, final EnumFacing side) {
		return true;
	}

	/**
	 * 電力
	 * @return 電力
	 */
	public CEPowerVR getPower() {
		if (this.power==null) {
			final IBlockState state = this.worldObj.getBlockState(getPos());
			final Block block = state.getBlock();
			if (block instanceof CEBlockVR)
				this.power = ((CEBlockVR) block).getTypeFromState(state).getPower();
			else
				this.power = CEPowerVR.DefaultPower;
		}
		return this.power;
	}
}