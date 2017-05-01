package ce.vrgenerator.vr.logic;

import java.util.List;

import com.google.common.collect.Lists;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
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
	protected CEPowerVR power;
	/** 発電レベル */
	protected int level;
	/** 総出力EU / Tick */
	protected int production;
	/** 送電するパケット数 / Tick */
	@Deprecated
	protected int packet;
	/** 電圧 (IC2 exp) {1=LV, 2=MV, 3=HV, 4=EV, 5=EVより大きい} */
	protected int tier = 1;
	// 電圧 (IC2 Classic) [総出力÷パケット数]
	// public short output = 1;

	protected CETileEntityVR(final CEPowerVR power) {
		this.power = power;
		this.level = power.getLevel();
		this.production = power.getProduction();
		this.packet = power.getPacket();
		this.tier = power.getTier();
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

	protected abstract CEPowerVR getPowerFromLevel(int level);

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		final int level = nbttagcompound.getInteger("level");
		this.power = getPowerFromLevel(level);
		this.level = level;
		this.production = nbttagcompound.getInteger("production");
		this.tier = nbttagcompound.getInteger("tier");
		this.activityMeter = nbttagcompound.getInteger("activitymeter");
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("level", this.level);
		nbttagcompound.setInteger("production", getProduction());
		nbttagcompound.setInteger("tier", getTier());
		nbttagcompound.setInteger("activitymeter", this.activityMeter);
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
		return getTier();
	}

	@Override
	public boolean emitsEnergyTo(final IEnergyAcceptor receiver, final EnumFacing side) {
		return true;
	}

	/**
	 * ティア
	 * @return ティア
	 */
	public int getTier() {
		return this.tier;
	}

	/**
	 * 発電量
	 * @return 発電量
	 */
	public int getProduction() {
		return this.production;
	}
}