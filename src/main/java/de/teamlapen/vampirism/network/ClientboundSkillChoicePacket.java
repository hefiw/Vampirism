package de.teamlapen.vampirism.network;

import de.teamlapen.vampirism.api.util.VResourceLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record ClientboundSkillChoicePacket(List<ResourceLocation> skillIds) implements CustomPacketPayload {

    public static final Type<ClientboundSkillChoicePacket> TYPE = new Type<>(VResourceLocation.mod("skill_choice"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSkillChoicePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.collection(java.util.ArrayList::new, ResourceLocation.STREAM_CODEC),
                    ClientboundSkillChoicePacket::skillIds,
                    ClientboundSkillChoicePacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}