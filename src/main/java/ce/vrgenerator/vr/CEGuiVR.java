package ce.vrgenerator.vr;

import ce.vrgenerator.vr.logic.CETileEntityVRVisibleRay;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ソーラーGUI
 *
 * @author takanasayo
 * @author A.K
 * @author Kamesuta
 */
@SideOnly(Side.CLIENT)
public class CEGuiVR extends GuiContainer {
	/**
	 * GUIテクスチャ
	 */
	private static final ResourceLocation gui = new ResourceLocation("vrgenerator", "textures/gui/GUISolarGenerator.png");
	private final CETileEntityVRVisibleRay tileentity;

	public CEGuiVR(final EntityPlayer player, final CETileEntityVRVisibleRay tile) {
		super(new CEContainerVR(player, tile));
		this.tileentity = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everythin in front of the items)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.tileentity.updateSunVisibility();
		//現在の発電量をGuiに表示
		final String name = I18n.format("vrgenerator.gui.solar", this.tileentity.isSunVisible() ? this.tileentity.getProduction() : 0);
		final String inv = I18n.format("container.inventory");
		this.fontRendererObj.drawString(name, (this.xSize-this.fontRendererObj.getStringWidth(name))/2, 6, 0x404040);
		this.fontRendererObj.drawString(inv, 8, this.ySize-96+2, 0x404040);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	public void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
		// 1.8からGlStateManagerを使用するべきです
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(gui);
		final int l = (this.width-this.xSize)/2;
		final int i1 = (this.height-this.ySize)/2;
		drawTexturedModalRect(l, i1, 0, 0, this.xSize, this.ySize);

		if (this.tileentity.isSunVisible())
			drawTexturedModalRect(l+80, i1+45, 176, 0, 14, 14);
	}
}
