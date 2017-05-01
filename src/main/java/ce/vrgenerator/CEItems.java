package ce.vrgenerator;

import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * アイテム
 *
 * @author Kamesuta
 */
public class CEItems {
	public static Block solarBlock;
	public static Item lavaUpdater;

	/**
	 * バニラアイテム
	 *
	 * @author Kamesuta
	 */
	public static class Vanilla {
		// Item.lightStoneDust			// グロウストーンダスト
		// Block.glowStone				// グロウストーン
		public final static ItemStack glowstone = new ItemStack(Blocks.glowstone);
		// Block.glass					// ガラス
		public final static Block blocklava = Blocks.lava;
		public final static Block blockflowinglava = Blocks.flowing_lava;
		public final static Block blockobsidian = Blocks.obsidian;
		public final static Block blockflowingwater = Blocks.flowing_water;
		public final static Block blockwater = Blocks.water;
	}

	/**
	 * IC2アイテム
	 *
	 * @author Kamesuta
	 */
	public static class IC2 {
		// Ic2Items.generator;			// 地熱発電機
		// Ic2Items.coalDust;			// 石炭の粉
		// Ic2Items.electronicCircuit;	// 電子回路
		public static final ItemStack circuit = IC2Items.getItem("crafting", "circuit");
		// Ic2Items.advancedCircuit;	// 発展回路
		public static final ItemStack advcircuit = IC2Items.getItem("crafting", "advanced_circuit");
		// Ic2Items.solarPanel;			// ソーラーパネル
		public static final ItemStack solar = IC2Items.getItem("te", "solar_generator");
		// Ic2Items.batBox;				// バットボックス
		public static final ItemStack batbox = IC2Items.getItem("te", "batbox");
		// Ic2Items.mfeUnit;			// MFE
		public static final ItemStack mfe = IC2Items.getItem("te", "mfe");
		// Ic2Items.mfsUnit;			// MFSU
		public static final ItemStack mfsu = IC2Items.getItem("te", "mfsu");
		// Ic2Items.lvTransformer;		//低圧変圧器
		public static final ItemStack lvtransformer = IC2Items.getItem("te", "lv_transformer");
		// Ic2Items.mvTransformer;		//中圧変圧器
		public static final ItemStack mvtransformer = IC2Items.getItem("te", "mv_transformer");
		// Ic2Items.hvTransformer;		//高圧変圧器
		public static final ItemStack hvtransformer = IC2Items.getItem("te", "hv_transformer");
		// Ic2Items.energyCrystal;
		// Ic2Items.lapotronCrystal;
		// Ic2Items.reBattery;
		// Ic2Items.treetap;
		// Ic2Items.lavaCell;
		public static final ItemStack celllava = IC2Items.getItem("fluid_cell", "lava");
		// Ic2Items.carbonPlate;
		public static final ItemStack platecarbon = IC2Items.getItem("crafting", "carbon_plate");
		// Ic2Items.coalChunk;
		public static final ItemStack coalchunk = IC2Items.getItem("crafting", "coal_chunk");
		// Ic2Items.advancedAlloy;
		public static final ItemStack platealloy = IC2Items.getItem("crafting", "alloy");
		// Ic2Items.iridiumPlate;
		public static final ItemStack plateiridium = IC2Items.getItem("crafting", "iridium");
		public static final ItemStack casing = IC2Items.getItem("resource", "machine");
		// Ic2Items.advancedMachine;
		public static final ItemStack advcasing = IC2Items.getItem("resource", "advanced_machine");
		public static final ItemStack ironplate = IC2Items.getItem("plate", "iron");
		public static final ItemStack wrench = IC2Items.getItem("wrench");
		public static final ItemStack electricwrench = IC2Items.getItem("electric_wrench");
		public static final ItemStack ecmeter = IC2Items.getItem("meter");
	}
}
