package mob_grinding_utils.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageFlagSync {

    public boolean wither, dragon;

    public MessageFlagSync(boolean witherIn, boolean dragonIn) {
        this.wither = witherIn;
        this.dragon = dragonIn;
    }

    public static void encode(final MessageFlagSync message, PacketBuffer buf) {
        buf.writeBoolean(message.wither);
        buf.writeBoolean(message.dragon);
    }

    public static MessageFlagSync decode(PacketBuffer buf) {
        return new MessageFlagSync(buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(MessageFlagSync message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CompoundNBT nbt = Minecraft.getInstance().player.getPersistentData();
            nbt.putBoolean("MGU_WitherMuffle", message.wither);
            nbt.putBoolean("MGU_DragonMuffle", message.dragon);
        });
        ctx.get().setPacketHandled(true);
    }
}