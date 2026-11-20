package net.atired.nethermore.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TearParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int sprOff = 0;
    protected TearParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=50;
        this.yd=ySpeed;
        this.xd=xSpeed;
        this.zd=zSpeed;
        this.xd*=0.28f;
        this.zd*=0.28f;
        this.gravity=-0.02f-(float)Math.random()/7.0f;
        this.quadSize*=2.6f;
        this.sprOff=(int)(Math.random()*50);
        this.spriteSet=sprite;
        this.oRoll=this.roll;
        this.alpha=0;
        this.setSpriteFromAge(spriteSet);
        this.age=(int)(6-Math.random()*12);

    }

    @Override
    public void tick() {
        if(this.age<0){
            this.age+=1;
            this.alpha=0;
            return;
        }
        this.oRoll=roll;
        float tempA =Math.min((1.0f-(float)((float)this.age/(float)this.lifetime))*6.0f,1.0f);
        this.alpha= Mth.lerp(0.4f,this.alpha,tempA);
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
            TearParticle flameparticle = new TearParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            return flameparticle;
        }
    }
}
