package net.atired.nethermore.client;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.atired.nethermore.Nethermore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.SequencedMap;

public class NMClientProxy {
    public float blueNess=0.0f;
    public float nihiloNess=0.0f;
    public float tarNess=0.0f;
    public float redNess=0.0f;
    public float redNess2=0.0f;
    public int blueCD=0;
    public int redCD=0;
    public float whispered=0.0f;
    public float lightNess=0.0f;
    private static final ResourceLocation SLITHER_LOCATION = Nethermore.getId("textures/entity/slither.png");
    public static MultiBufferSource.BufferSource SLITHER_SOURCE = null;

    public static MultiBufferSource.BufferSource getSlitherSource(){
        if(SLITHER_SOURCE!=null){
            return SLITHER_SOURCE;
        }else{
            SequencedMap<RenderType, ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(NMRenderLayers.entityMonochromeCull(SLITHER_LOCATION),
                    new ByteBufferBuilder(NMRenderLayers.entityMonochromeCull(SLITHER_LOCATION).bufferSize()));
            SLITHER_SOURCE=MultiBufferSource.immediateWithBuffers(buffers,new ByteBufferBuilder(256));
            return SLITHER_SOURCE;
        }
    }
}
