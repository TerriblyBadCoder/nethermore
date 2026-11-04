package net.atired.nethermore.client.screens;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.blockentities.InfusingStandMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;

public class InfusingStandScreen extends AbstractContainerScreen<InfusingStandMenu> {
    private static final ResourceLocation INFUSING_STAND_LOCATION = Nethermore.getId("textures/gui/container/infusing_stand.png");
    private static final ResourceLocation BUBBLES_SPRITE = ResourceLocation.withDefaultNamespace("container/brewing_stand/bubbles");

    private static final ResourceLocation PROGRESS_SPRITE = Nethermore.getId("container/infusing_stand/brew_progress");
    private static final ResourceLocation GAUGE_SPRITE = Nethermore.getId("container/infusing_stand/fuel_gauge");
    private static final int[] BUBBLELENGTHS = new int[]{29, 26, 22, 17, 9, 0, 0};

    public InfusingStandScreen(InfusingStandMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int i1 = ((InfusingStandMenu)this.menu).getBrewingTicks();
        guiGraphics.blit(INFUSING_STAND_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);

        int k = ((InfusingStandMenu)this.menu).getFuel()*5;
        int l = Mth.clamp((18 * k + 20 - 1) / 20, 0, 18);
        if (l > 0) {
            guiGraphics.blitSprite(GAUGE_SPRITE, 18, 4, 18-l, 0, i + 60+18-l+8, j + 44+21, l, 4);
        }
        if (i1 > 0) {
            int j1 = (int) (28.0F * (1.0F - (float) i1 / 400.0F));
            if (j1 > 0) {
                guiGraphics.blitSprite(PROGRESS_SPRITE, 9, 28, 0, 0, i + 97, j + 16, 9, j1);
            }
            j1 = 29-(i1/8)%29;
            if (j1 > 0) {
                guiGraphics.blitSprite(BUBBLES_SPRITE, 12, 29, 0, 29 - j1, i + 63+8, j + 14 + 29 - j1+20, 12, j1);
            }
        }
    }
}
