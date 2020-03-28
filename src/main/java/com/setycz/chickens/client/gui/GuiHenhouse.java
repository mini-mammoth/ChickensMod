package com.setycz.chickens.client.gui;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.block.TileEntityHenhouse;
import com.setycz.chickens.client.gui.container.ContainerHenhouse;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by setyc on 06.03.2016.
 */
@OnlyIn(Dist.CLIENT)
public class GuiHenhouse extends ContainerScreen {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ChickensMod.MODID, "textures/gui/henhouse.png");
    private final PlayerInventory playerInv;
    private final TileEntityHenhouse tileEntityHenhouse;

    public GuiHenhouse(PlayerInventory playerInv, TileEntityHenhouse tileEntityHenhouse) {
        super(new ContainerHenhouse(playerInv, tileEntityHenhouse));
        this.playerInv = playerInv;
        this.tileEntityHenhouse = tileEntityHenhouse;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        int energy = tileEntityHenhouse.getEnergy();
        final int BAR_HEIGHT = 57;
        int offset = BAR_HEIGHT - (energy * BAR_HEIGHT / TileEntityHenhouse.hayBaleEnergy);
        drawTexturedModalRect(i + 75, j + 14 + offset, 195, offset, 12, BAR_HEIGHT - offset);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        ITextComponent henhouseDisplayName = tileEntityHenhouse.getDisplayName();
        assert henhouseDisplayName != null;
        String henhouseName = henhouseDisplayName.getUnformattedText();
        this.fontRenderer.drawString(
                henhouseName,
                xSize / 2 - fontRenderer.getStringWidth(henhouseName) / 2, 6,
                4210752);
        this.fontRenderer.drawString(
                playerInv.getDisplayName().getUnformattedText(),
                8, ySize - 96 + 2,
                4210752);
    }
}
