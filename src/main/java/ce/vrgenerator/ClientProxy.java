package ce.vrgenerator;

import ce.vrgenerator.vr.CEVRType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * クライアント処理
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
public class ClientProxy extends CommonProxy {
	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}

	@Override
	public void preInit(final FMLPreInitializationEvent event) {
		super.preInit(event);
		ModelLoader.setCustomModelResourceLocation(CEItems.lavaUpdater, 0, new ModelResourceLocation("vrgenerator:lavaupdater", "inventory"));
		final Item item = Item.getItemFromBlock(CEItems.solarBlock);
		for (final CEVRType ce : CEVRType.values())
			ModelLoader.setCustomModelResourceLocation(item, ce.getID(), new ModelResourceLocation("vrgenerator:visibleraysolar/"+ce.getName(), "inventory"));
	}

	@Override
	public void init(final FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	/**
	 * クライアントチャット
	 * @param format フォーマット
	 * @param args 変数
	 */
	public static void addChat(final String format, final Object... args) {
		addChat(String.format(format, args));
	}

	/**
	 * クライアントチャット
	 * @param message メッセージ
	 */
	public static void addChat(final String message) {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			final Minecraft mc = Minecraft.getMinecraft();
			mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(message));
		}
	}
}