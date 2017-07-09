package ce.vrgenerator;

import ce.vrgenerator.vr.CEBlockVR;
import ce.vrgenerator.vr.CEContainerVR;
import ce.vrgenerator.vr.CEGuiVR;
import ce.vrgenerator.vr.CEItemVR;
import ce.vrgenerator.vr.logic.CETileEntityVRCEPermanentLight;
import ce.vrgenerator.vr.logic.CETileEntityVRVisibleRay;
import ic2.api.info.Info;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * 共通処理
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class CommonProxy implements IGuiHandler {
	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		if (ID==VisibleRayGenerator.artificialSunGuiID) {
			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity!=null&&tileEntity instanceof CETileEntityVRVisibleRay)
				return new CEContainerVR(player, (CETileEntityVRVisibleRay) tileEntity);
			else
				return null;
		} else
			return null;
	}

	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		if (ID==VisibleRayGenerator.artificialSunGuiID) {
			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity!=null&&tileEntity instanceof CETileEntityVRVisibleRay)
				return new CEGuiVR(player, (CETileEntityVRVisibleRay) tileEntity);
			else
				return null;
		} else
			return null;
	}

	public World getClientWorld() {
		return null;
	}

	public void preInit(final FMLPreInitializationEvent event) {
		// GameRegistry.registerBlock(CEItems.solarBlock = new CEBlockVR(), CEItemVR.class, "visibleraysolar");
		ForgeRegistries.BLOCKS.register(CEItems.solarBlock = new CEBlockVR());
		ForgeRegistries.ITEMS.register(CEItems.solarItemBlock = new CEItemVR(CEItems.solarBlock));
		// GameRegistry.register(CEItems.lavaUpdater = new CEItemLavaUpdater(), "lavaupdater");
		ForgeRegistries.ITEMS.register(CEItems.lavaUpdater = new CEItemLavaUpdater());

		try {
			final CreativeTabs tabIC2 = (CreativeTabs) Info.ic2ModInstance.getClass().getField("tabIC2").get(Info.ic2ModInstance);
			CEItems.solarBlock.setCreativeTab(tabIC2);
			CEItems.lavaUpdater.setCreativeTab(tabIC2);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void init(final FMLInitializationEvent event) {
	}

	public void postInit(final FMLPostInitializationEvent event) {
		initIC();
		registerRecipesIC();
	}

	/**
	 * 初期化
	 */
	private void initIC() {
		GameRegistry.registerTileEntity(CETileEntityVRVisibleRay.class, "CE_Solar");
		GameRegistry.registerTileEntity(CETileEntityVRCEPermanentLight.class, "CE_Generator");

		//	if(isICstacksizeUp)
		//	{
		//		VRGItemFieldAccessHelper.changeField(Items.getItem("energyCrystal").getItem(), 64);
		//		VRGItemFieldAccessHelper.changeField(Items.getItem("lapotronCrystal").getItem(), 64);
		//		VRGItemFieldAccessHelper.changeField(Items.getItem("treetap").getItem(), 64);
		//		Items.getItem("reBattery").getItem().setMaxStackSize(64);
		//		Items.getItem("coolingCell").getItem().setMaxStackSize(64);
		//		Items.getItem("reinforcedDoorBlock").getItem().setMaxStackSize(64);
		//	}

		if (VisibleRayGenerator.isResistanceVanilla) {
			CEItems.Vanilla.blockobsidian.setResistance(2000F);
			CEItems.Vanilla.blockflowingwater.setResistance(500F);
			CEItems.Vanilla.blockwater.setResistance(500F);
			CEItems.Vanilla.blocklava.setResistance(500F);
		}
	}

	public static ResourceLocation getRecipeGroup(final ItemStack stack) {
		String s = stack.getUnlocalizedName();
		final int idx = s.lastIndexOf(":");
		if (idx>=0)
			s = s.substring(idx+1);
		return new ResourceLocation(Reference.MODID, s);
	}

	public static ShapedOreRecipe addOredictRecipe(final ItemStack output, final Object... recipe) {
		final ShapedOreRecipe sor = new ShapedOreRecipe(getRecipeGroup(output), output, recipe);
		ForgeRegistries.RECIPES.register(sor.setRegistryName(sor.getGroup()));
		return sor;
	}

	public static void addShapelessOredictRecipe(final ItemStack output, final Object... recipe) {
		final ShapelessOreRecipe sor = new ShapelessOreRecipe(getRecipeGroup(output), output, recipe);
		ForgeRegistries.RECIPES.register(sor.setRegistryName(sor.getGroup()));
	}

	/**
	 * IC2レシピ登録
	 */
	private void registerRecipesIC() {
		final ItemStack solar1 = new ItemStack(CEItems.solarBlock, 1, 0);
		final ItemStack solar2 = new ItemStack(CEItems.solarBlock, 1, 1);
		final ItemStack solar3 = new ItemStack(CEItems.solarBlock, 1, 2);
		final ItemStack solar4 = new ItemStack(CEItems.solarBlock, 1, 3);

		addOredictRecipe(new ItemStack(CEItems.lavaUpdater), "c", "a", "i",
				'c', CEItems.IC2.celllava, 'a', CEItems.IC2.advcircuit, 'i', CEItems.IC2.ironplate);
		addShapelessOredictRecipe(solar1, CEItems.IC2.solar, CEItems.IC2.advcircuit);
		if (VisibleRayGenerator.isSolarCostUp) {
			addOredictRecipe(solar2, "sss", "sms", "sss", 's', solar1, 'm', CEItems.IC2.batbox);
			addOredictRecipe(solar3, "sss", "sms", "sss", 's', solar2, 'm', CEItems.IC2.mfe);
			addOredictRecipe(solar4, "sss", "sms", "sss", 's', solar3, 'm', CEItems.IC2.mfsu);
		} else {
			addOredictRecipe(solar2, "sss", "sms", "sss", 's', solar1, 'm', CEItems.IC2.lvtransformer);
			addOredictRecipe(solar3, "sss", "sms", "sss", 's', solar2, 'm', CEItems.IC2.mvtransformer);
			addOredictRecipe(solar4, "sss", "sms", "sss", 's', solar3, 'm', CEItems.IC2.hvtransformer);
		}

		final ItemStack[] rlg = new ItemStack[11];
		rlg[0] = solar1;
		for (int i = 0; i<10; i++)
			rlg[i+1] = new ItemStack(CEItems.solarBlock, 1, i+4);
		final ItemStack[] items = {
				CEItems.Vanilla.glowstone,
				CEItems.IC2.circuit,
				CEItems.IC2.advcircuit,
				CEItems.IC2.lvtransformer,
				CEItems.IC2.mvtransformer,
				CEItems.IC2.hvtransformer,
				CEItems.IC2.advcasing,
				CEItems.IC2.advcasing,
				CEItems.IC2.advcasing,
				rlg[7],
		};
		final ItemStack[] plates = {
				CEItems.IC2.platecarbon,
				CEItems.IC2.platecarbon,
				CEItems.IC2.platecarbon,
				CEItems.IC2.platecarbon,
				CEItems.IC2.platecarbon,
				CEItems.IC2.platealloy,
				CEItems.IC2.platealloy,
				CEItems.IC2.plateiridium,
				CEItems.IC2.plateiridium,
				CEItems.IC2.plateiridium,
		};

		addOredictRecipe(rlg[5], "psp", "gmg", "psp",
				'p', CEItems.IC2.coalchunk, 'g', solar4, 'm', CEItems.IC2.mvtransformer, 's', CEItems.Vanilla.glowstone);

		for (int i = 0; i<10; i++)
			addOredictRecipe(rlg[i+1], "pgp", "gmg", "pgp", 'p', plates[i], 'g', rlg[i], 'm', items[i]);
	}
}