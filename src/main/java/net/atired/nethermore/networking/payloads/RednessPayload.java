package net.atired.nethermore.networking.payloads;

import io.netty.buffer.ByteBuf;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.init.NMSoundInit;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RednessPayload(int no) implements CustomPacketPayload {

    public static final Type<RednessPayload> TYPE = new Type<>(Nethermore.getId("red"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, RednessPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            RednessPayload::no,
            RednessPayload::new
    );
    public void handleData(final IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                if(NethermoreClient.PROXY!=null&&NethermoreClient.PROXY.nihiloNess>0.01f){
                    Minecraft.getInstance().player.playSound(NMSoundInit.HEARTBEAT.value(),8.2f,1.24f-(float)Math.random()/2.0f);
                    NethermoreClient.PROXY.redNess=0.99f;
                }
                NethermoreClient.PROXY.redNess2=0.99f;

            });
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}