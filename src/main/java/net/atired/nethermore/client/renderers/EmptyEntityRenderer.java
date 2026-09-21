package net.atired.nethermore.client.renderers;

import net.atired.nethermore.Nethermore;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EmptyEntityRenderer extends EntityRenderer<Entity> {
    private static final ResourceLocation DISGUSTLING_LOCATION = Nethermore.getId("textures/entity/disgustling.png");

    public EmptyEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return DISGUSTLING_LOCATION;
    }
}
