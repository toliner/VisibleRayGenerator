package ce.vrgenerator.vr.logic;

import ce.vrgenerator.vr.CEVRType;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 永久光発電機タイルエンティティロジック
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class CETileEntityVRCEPermanentLight extends CETileEntityVR {
	public CETileEntityVRCEPermanentLight(final CEPowerVR power) {
		super(power);
	}

	public CETileEntityVRCEPermanentLight() {
	}

	@Override
	protected CEPowerVR getPowerFromLevel(final int level) {
		return CEVRType.fromTileID(CEVRType.CETileType.CEPermanentLight, level).getPower();
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
	}
}
