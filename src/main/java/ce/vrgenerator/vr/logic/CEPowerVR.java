package ce.vrgenerator.vr.logic;

/**
 * 電力詳細
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class CEPowerVR {
	/** Low Voltage		| 低圧 */
	public static final int LV = 32;
	/** Medium Voltage	| 中圧 */
	public static final int MV = 128;
	/** High Voltage	| 高圧 */
	public static final int HV = 512;
	/** Extreme Voltage	| 超高圧 */
	public static final int EV = 2048;

	private final int level;
	private final int production;
	private final int packet;

	public CEPowerVR(final int level, final int power, final int packet) {
		this.level = level;
		this.production = power;
		this.packet = packet;
	}

	/** 発電レベル */
	public int getLevel() {
		return this.level;
	}

	/** 総出力EU / Tick */
	public int getProduction() {
		return this.production;
	}

	/** 送電するパケット数 / Tick */
	public int getPacket() {
		return this.packet;
	}

	/** 電圧 (IC2 exp) {1=LV, 2=MV, 3=HV, 4=EV, 5=EVより大きい} */
	public int getTier() {
		return getTierFromProduction(getProduction());
	}

	/** 総電力 → 電圧 (IC2 exp) {1=LV, 2=MV, 3=HV, 4=EV, 5=EVより大きい} */
	public static int getTierFromProduction(final int prod) {
		return prod<=LV ? 1 : prod<=MV ? 2 : prod<=HV ? 3 : prod<=EV ? 4 : 5;
	}
}
