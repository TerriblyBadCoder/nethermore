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

public class RuneParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int rotoff=0;
    private Vec2 angled = new Vec2(0,0);
    protected RuneParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=25;
        this.rotoff=(int)(Math.random()*300*3.14f);
        this.gravity=-0.0f;
        this.angled= new Vec2((float) Math.atan2(xSpeed,zSpeed),(float)Math.asin(ySpeed));
        this.yd=ySpeed/2.0f;
        this.xd=xSpeed/16.0f;
        this.zd=zSpeed/16.0f;
        this.friction=0.9f;
        this.quadSize*=0.5f;
        this.spriteSet=sprite;
        this.pickSprite(sprite);
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.rCol=1.0f;
        this.gCol=1.0f-(float)Math.random()/8.0f;
        this.bCol=0.45f-(float)Math.random()/12.0f;
        this.oRoll=roll;
        this.alpha=0.0f;
    }

    @Override
    public void tick() {
        this.oRoll=roll;
        float aged=  (float)this.age/(float)this.lifetime;
        this.quadSize+=(1.0f-aged)*0.05f/24.0f;

        this.roll+=(1.0f-aged)*0.2f;
        alpha=Math.min((1.0f-aged)*4.0f,1.0f);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float angle = this.oRoll*(1.0f-partialTicks)+this.roll*partialTicks;
        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0,this.angled.x,0), partialTicks);

        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0,this.angled.x+3.14f,0), partialTicks);
    }
    @Override
    public int getLightColor(float partialTick) {
        float f = ((float)this.age + partialTick) / (float)this.lifetime;
        f = Mth.clamp(f*2.0f-0.2f, 0.0F, 1.0F);
        int i = super.getLightColor(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
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
            RuneParticle flameparticle = new RuneParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
