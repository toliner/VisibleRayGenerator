package ce.vrgenerator.vr.logic;

/**
 * 電力詳細
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class CEPowerVR {
	public static final CEPowerVR DefaultPower = new CEPowerVR(0, 1, 1);

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

	/** 最大総出力EU / Tick */
	public int getMaxProduction() {
		return this.production;
	}

	/** 送電するパケット数 / Tick */
	public int getPacket() {
		return this.packet;
	}

	/** 最大総出力EU / パケット [総出力÷パケット数]*/
	public int getMaxOutput() {
		return getMaxProduction()/* /getPacket() */;
	}

	/** 総出力EU / パケット [総出力÷パケット数]*/
	public int getOutput(final int production) {
		return production/* /getPacket() */;
	}

	/** 電圧 (IC2 exp) {1=LV, 2=MV, 3=HV, 4=EV, 5=EVより大きい} */
	public int getTier() {
		return getTierFromProduction(getMaxProduction());
	}

	/** 総電力 → 電圧 (IC2 exp) {1=LV, 2=MV, 3=HV, 4=EV, 5=EVより大きい} */
	public static int getTierFromProduction(final int prod) {
		return (log2(prod) >> 1) + 1;
	}

	private static int log2( int bits ) {// returns 0 for bits=0
		int log = 0;
		if( ( bits & 0xffff0000 ) != 0 ) { bits >>>= 16; log = 16; }
		if( bits >= 256 ) { bits >>>= 8; log += 8; }
		if( bits >= 16  ) { bits >>>= 4; log += 4; }
		if( bits >= 4   ) { bits >>>= 2; log += 2; }
		return log + ( bits >>> 1 );
	}
}
