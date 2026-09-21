package net.atired.nethermore.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class WhisperParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int sprOff = 0;
    protected WhisperParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=40;
        this.yd=0.0f;
        this.xd*=0.78f;
        this.zd*=0.78f;
        this.gravity=-0.1f;
        this.rCol=0.34f;
        this.gCol=0.5f;
        this.bCol=0.31f;
        this.quadSize*=0.1f;
        this.sprOff=(int)(Math.random()*50);
        this.spriteSet=sprite;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.oRoll=roll;
        if(this.age>24){
            this.rCol= Mth.lerp(0.1f,this.rCol,1.0f);
            this.gCol= Mth.lerp(0.1f,this.gCol,0.4f);
            this.bCol= Mth.lerp(0.1f,this.bCol,0.4f);
        }
        if(this.quadSize<0.25){
            this.quadSize*=1.1f;
        }
        //this.roll+=(1.0f-(float)this.age/this.lifetime)/3.0f;
        alpha=Math.min((1.0f-(float)((float)this.age/(float)this.lifetime))*4.0f,1.0f);
        this.setSprite(spriteSet.get((this.age*2+this.sprOff)%this.lifetime,this.lifetime));
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
            WhisperParticle flameparticle = new WhisperParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            return flameparticle;
        }
    }
}
