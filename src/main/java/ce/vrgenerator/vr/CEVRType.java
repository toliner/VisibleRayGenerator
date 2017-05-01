package ce.vrgenerator.vr;

import static ce.vrgenerator.vr.CEVRType.CETileType.*;

import java.util.EnumMap;
import java.util.HashMap;

import com.google.common.collect.Maps;

import ce.vrgenerator.CEItems;
import ce.vrgenerator.vr.logic.CEPowerVR;
import ce.vrgenerator.vr.logic.CETileEntityVR;
import ce.vrgenerator.vr.logic.CETileEntityVRCEPermanentLight;
import ce.vrgenerator.vr.logic.CETileEntityVRVisibleRay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

/**
 * 発電機一覧
 *
 * @author Kamesuta
 */
public enum CEVRType implements IStringSerializable {
	/** {Level = 0, Production = 2, Packet = 1} */
	VisibleRay1(0, "visibleray1", VisibleRay, new CEPowerVR(0, 1, 1)),
	/** {Level = 1, Production = 8, Packet = 1} */
	VisibleRay8(1, "visibleray8", VisibleRay, new CEPowerVR(1, 8, 1)),
	/** {Level = 2, Production = 64, Packet = 1} */
	VisibleRay64(2, "visibleray64", VisibleRay, new CEPowerVR(2, 64, 1)),
	/** {Level = 3, Production = 512, Packet = 1} */
	VisibleRay512(3, "visibleray512", VisibleRay, new CEPowerVR(3, 512, 1)),
	/** {Level = 0, Production = 2, Packet = 1} */
	CEPermanentLight2(4, "cepermanentlight2", CEPermanentLight, new CEPowerVR(0, 2, 1)),
	/** {Level = 1, Production = 8, Packet = 1} */
	CEPermanentLight8(5, "cepermanentlight8", CEPermanentLight, new CEPowerVR(1, 8, 1)),
	/** {Level = 2, Production = 32, Packet = 1} */
	CEPermanentLight32(6, "cepermanentlight32", CEPermanentLight, new CEPowerVR(2, 32, 1)),
	/** {Level = 3, Production = 128, Packet = 1} */
	CEPermanentLight128(7, "cepermanentlight128", CEPermanentLight, new CEPowerVR(3, 128, 1)),
	/** {Level = 4, Production = 512, Packet = 1} */
	CEPermanentLight512(8, "cepermanentlight512", CEPermanentLight, new CEPowerVR(4, 512, 1)),
	/** {Level = 5, Production = 2048, Packet = 4} */
	CEPermanentLight2048(9, "cepermanentlight2048", CEPermanentLight, new CEPowerVR(5, 2048, 4)),
	/** {Level = 6, Production = 8192, Packet = 16} */
	CEPermanentLight8192(10, "cepermanentlight8192", CEPermanentLight, new CEPowerVR(6, 8192, 16)),
	/** {Level = 7, Production = 32768, Packet = 64} */
	CEPermanentLight32768(11, "cepermanentlight32768", CEPermanentLight, new CEPowerVR(7, 32768, 64)),
	/** {Level = 8, Production = 131072, Packet = 256} */
	CEPermanentLight131072(12, "cepermanentlight131072", CEPermanentLight, new CEPowerVR(8, 131072, 256)),
	/** {Level = 9, Production = 532480, Packet = 1040} */
	CEPermanentLight532480(13, "cepermanentlight532480", CEPermanentLight, new CEPowerVR(9, 532480, 1040)),
	;

	private final int id;
	private final String name;
	private final CETileType type;
	private final CEPowerVR power;
	private final String displayName;

	private CEVRType(final int id, final String name, final CETileType type, final CEPowerVR power) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.power = power;
		this.displayName = String.format("vrgenerator.visibleraysolar.%s", name);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public CETileType getType() {
		return this.type;
	}

	public CEPowerVR getPower() {
		return this.power;
	}

	public CETileEntityVR createTileEntity() {
		final CEPowerVR power = getPower();
		return getType().createTileEntity(power);
	}

	public ItemStack createItemStack(final int amount) {
		return new ItemStack(CEItems.solarBlock, amount, getID());
	}

	private static final HashMap<Integer, CEVRType> map = Maps.newHashMap();
	private static final EnumMap<CETileType, HashMap<Integer, CEVRType>> tilemap = Maps.newEnumMap(CETileType.class);

	static {
		for (final CEVRType ce : values()) {
			map.put(ce.getID(), ce);

			final CETileType type = ce.getType();
			HashMap<Integer, CEVRType> tile = tilemap.get(type);
			if (tile==null) {
				tile = Maps.newHashMap();
				tilemap.put(type, tile);
			}
			tile.put(ce.getTileID(), ce);
		}
	}

	public int getID() {
		return this.id;
	}

	public int getTileID() {
		return getPower().getLevel();
	}

	public static CEVRType fromID(final int id) {
		CEVRType ce = map.get(id);
		if (ce==null)
			ce = VisibleRay1;
		return ce;
	}

	public static CEVRType fromTileID(final CETileType type, final int id) {
		final HashMap<Integer, CEVRType> tmap = tilemap.get(type);
		CEVRType ce = tmap.get(id);
		if (ce==null)
			ce = type.defaultType;
		return ce;
	}

	/**
	 * 発電機の種類
	 *
	 * @author Kamesuta
	 */
	public static enum CETileType {
		VisibleRay(VisibleRay1) {
			@Override
			public CETileEntityVR createTileEntity(final CEPowerVR power) {
				return new CETileEntityVRVisibleRay(power);
			}
		},
		CEPermanentLight(CEPermanentLight2) {
			@Override
			public CETileEntityVR createTileEntity(final CEPowerVR power) {
				return new CETileEntityVRCEPermanentLight(power);
			}
		};

		private final CEVRType defaultType;

		private CETileType(final CEVRType defaultType) {
			this.defaultType = defaultType;
		}

		public abstract CETileEntityVR createTileEntity(CEPowerVR power);
	}
}