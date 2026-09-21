package net.atired.nethermore.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SoulParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int sprOff = 0;
    protected SoulParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=20;
        this.yd=0.0f;
        this.xd*=0.28f;
        this.zd*=0.28f;
        this.gravity=-0.1f;
        this.quadSize*=2.6f;
        this.sprOff=(int)(Math.random()*50);
        this.spriteSet=sprite;
        this.roll=(float)Math.random()*3.14f*8.0f;
        this.oRoll=this.roll;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.oRoll=roll;
        this.roll+=(1.0f-(float)this.age/this.lifetime)/6.0f;
        alpha=Math.min((1.0f-(float)((float)this.age/(float)this.lifetime))*6.0f,1.0f);
        this.setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {

        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SoulParticle flameparticle = new SoulParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            return flameparticle;
        }
    }
}
