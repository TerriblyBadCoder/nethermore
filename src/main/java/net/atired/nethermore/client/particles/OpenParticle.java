package net.atired.nethermore.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class OpenParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int rotoff=0;
    private Vec2 angled = new Vec2(0,0);
    protected OpenParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=10;
        this.rotoff=(int)(Math.random()*300*3.14f);
        this.gravity=-0.0f;
        this.angled= new Vec2((float) Math.atan2(xSpeed,zSpeed),(float)Math.asin(ySpeed));
        this.yd=ySpeed/2.0f;
        this.xd=xSpeed/10.0f;
        this.zd=zSpeed/10.0f;
        this.friction=0.9f;
        this.quadSize=0.5f;
        this.spriteSet=sprite;
        this.alpha=0.0f;
    }

    @Override
    public void tick() {
        float aged=  (float)this.age/(float)this.lifetime;
        this.quadSize+=aged/240.0f;

        alpha=Math.min((1.0f-aged)*4.0f,1.0f);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0,this.angled.x,0), partialTicks);

        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0,3.14f+this.angled.x,0), partialTicks);
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
            OpenParticle flameparticle = new OpenParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
