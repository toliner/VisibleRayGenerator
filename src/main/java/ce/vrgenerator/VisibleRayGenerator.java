package ce.vrgenerator;

import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * 永久光発電機Mod
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY, useMetadata = true)
public class VisibleRayGenerator {
	/** CompactEngineがロードされているかどうか */
	public static boolean isCE;

	/** 溶岩流更新機範囲 */
	public static int LavaUpdateArea = 3;
	/** ソーラーのコストアップ */
	public static boolean isSolarCostUp;
	//	public static boolean isICstacksizeUp;
	/** バニラのブロックの耐性アップ */
	public static boolean isResistanceVanilla;
	//	public static Block artificiaalSun;

	/** 人工光ソーラー GUI ID */
	public static int artificialSunGuiID = 0;

	@Instance
	public static VisibleRayGenerator instance;
	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
	public static CommonProxy proxy;

	/** コンフィグ */
	public static Configuration config;

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		setUpCredit(event.getModMetadata());

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		isSolarCostUp = config.get(Configuration.CATEGORY_GENERAL, "isSolarCostUp", true, "Using a capacitor instead of a transformer recipe CE-Solar").setRequiresMcRestart(true).setLanguageKey("vrgenerator.config.solarCostUp.name").getBoolean(true);
		isResistanceVanilla = config.get(Configuration.CATEGORY_GENERAL, "isResistanceVanilla", true, "Set in the vanilla resistance of Obsidian, Water, Lava").setRequiresMcRestart(true).setLanguageKey("vrgenerator.config.resistanceVanilla.name").getBoolean(true);
		config.save();

		proxy.preInit(event);
	}

	/**
	 * クレジット
	 * <p>
	 * Note: useMetadata=trueによりmcmod.infoをベースにこのメソッドで編集します。
	 *
	 * @param meta
	 */
	private void setUpCredit(final ModMetadata meta) {
		meta.authorList = Arrays.asList("takanasayo", "A.K", "Kamesuta");
		meta.url = "https://minecraft.curseforge.com/projects/visibleraygenerator";
		meta.credits = "Updated to mc1.8.9 By A.K. and Kamesuta";
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		proxy.init(event);
	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		isCE = Loader.isModLoaded("CompactEngine");

		proxy.postInit(event);
	}
}