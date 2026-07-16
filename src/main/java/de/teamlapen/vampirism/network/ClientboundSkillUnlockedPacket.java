package de.teamlapen.vampirism.network;

import de.teamlapen.vampirism.api.util.VResourceLocation;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientboundSkillUnlockedPacket(ResourceLocation skillId) implements CustomPacketPayload {
    public static final Type<ClientboundSkillUnlockedPacket> TYPE = new Type<>(VResourceLocation.mod("skill_unlocked"));
    public static final StreamCodec<ByteBuf, ClientboundSkillUnlockedPacket> STREAM_CODEC =
            ResourceLocation.STREAM_CODEC.map(ClientboundSkillUnlockedPacket::new, ClientboundSkillUnlockedPacket::skillId);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}