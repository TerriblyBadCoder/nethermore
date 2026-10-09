package net.atired.nethermore.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class PaperTrailParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private float rotoff=0;
    private Vec2 angled = new Vec2(0,0);
    protected PaperTrailParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=80;
        this.gravity=0.13f;
        this.angled= new Vec2((float) Math.atan2(xSpeed,zSpeed),(float)Math.asin(ySpeed));
        this.yd=ySpeed/2.0f+0.1f;
        this.xd=xSpeed/2.0f;
        this.zd=zSpeed/2.0f;
        this.friction=0.9f;
        this.quadSize*=2.1f-(float)Math.random()/3.0f;
        this.spriteSet=sprite;
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.oRoll=roll;
        this.alpha=0.0f;
    }

    @Override
    public void tick() {
        this.oRoll=roll;
        float aged=  (float)this.age/(float)this.lifetime;

        if(!onGround) {
            this.roll += (1.0f - aged) * 0.2f;
            this.rotoff+=age;
        }else{
            this.oRoll=this.roll;
        }
        this.alpha=Math.min((1.0f-aged)*4.0f,1.0f);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float angle = this.oRoll*(1.0f-partialTicks)+this.roll*partialTicks;
        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0,angle,angle-3.14f/2.0f), partialTicks);

        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0,angle+3.14f,-angle+3.14f/2.0f), partialTicks);
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
            PaperTrailParticle flameparticle = new PaperTrailParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
