package mob_grinding_utils.fakeplayer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.ProtocolType;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import java.net.SocketAddress;
import java.util.Set;

public class FakeNetHandler extends ServerPlayNetHandler {
    public FakeNetHandler(MinecraftServer server, ServerPlayerEntity playerIn) {
        super(server, new FakeManager(PacketDirection.CLIENTBOUND), playerIn);
    }

    private static class FakeManager extends NetworkManager {
        public FakeManager(PacketDirection packetDirection) {
            super(packetDirection);
        }

        @Override
        public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {}

        @Override
        public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception {}

        @Override
        public void setConnectionState(ProtocolType newState) {}

        @Override
        public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {}

        @Override
        protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, IPacket<?> p_channelRead0_2_) throws Exception {}

        @Override
        public void sendPacket(IPacket<?> packetIn) {}

        @Override
        public void sendPacket(IPacket<?> packetIn, @Nullable GenericFutureListener<? extends Future<? super Void>> p_201058_2_) {}

        @Override
        public void tick() {}

        @Override
        protected void func_241877_b() {}

        @Override
        public SocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public void closeChannel(ITextComponent message) {}

        @Override
        public boolean hasNoChannel() {
            return true;
        }

        @Override
        public boolean isLocalChannel() {
            return false;
        }

        @Override
        public void func_244777_a(Cipher p_244777_1_, Cipher p_244777_2_) {}

        @Override
        public boolean isChannelOpen() {
            return false;
        }

        @Override
        public void disableAutoRead() {}

        @Override
        public void setCompressionThreshold(int threshold) {}

        @Override
        public void handleDisconnection() {}

        @Override
        public Channel channel() {
            return null;
        }
    }

    @Override
    public void tick() {}

    @Override
    public void captureCurrentPosition() {}

    @Override
    public void disconnect(ITextComponent textComponent) {}

    @Override
    public void processInput(CInputPacket packetIn) {}

    @Override
    public void processVehicleMove(CMoveVehiclePacket packetIn) {}

    @Override
    public void processConfirmTeleport(CConfirmTeleportPacket packetIn) {}

    @Override
    public void handleRecipeBookUpdate(CMarkRecipeSeenPacket packetIn) {}

    @Override
    public void func_241831_a(CUpdateRecipeBookStatusPacket p_241831_1_) {}

    @Override
    public void handleSeenAdvancements(CSeenAdvancementsPacket packetIn) {}

    @Override
    public void processTabComplete(CTabCompletePacket packetIn) {}

    @Override
    public void processUpdateCommandBlock(CUpdateCommandBlockPacket packetIn) {}

    @Override
    public void processUpdateCommandMinecart(CUpdateMinecartCommandBlockPacket packetIn) {}

    @Override
    public void processPickItem(CPickItemPacket packetIn) {}

    @Override
    public void processRenameItem(CRenameItemPacket packetIn) {}

    @Override
    public void processUpdateBeacon(CUpdateBeaconPacket packetIn) {}

    @Override
    public void processUpdateStructureBlock(CUpdateStructureBlockPacket packetIn) {}

    @Override
    public void func_217262_a(CUpdateJigsawBlockPacket p_217262_1_) {}

    @Override
    public void func_230549_a_(CJigsawBlockGeneratePacket p_230549_1_) {}

    @Override
    public void processSelectTrade(CSelectTradePacket packetIn) {}

    @Override
    public void processEditBook(CEditBookPacket packetIn) {}

    @Override
    public void processNBTQueryEntity(CQueryEntityNBTPacket packetIn) {}

    @Override
    public void processNBTQueryBlockEntity(CQueryTileEntityNBTPacket packetIn) {}

    @Override
    public void processPlayer(CPlayerPacket packetIn) {}

    @Override
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<SPlayerPositionLookPacket.Flags> relativeSet) {}

    @Override
    public void processPlayerDigging(CPlayerDiggingPacket packetIn) {}

    @Override
    public void processTryUseItemOnBlock(CPlayerTryUseItemOnBlockPacket packetIn) {}

    @Override
    public void processTryUseItem(CPlayerTryUseItemPacket packetIn) {}

    @Override
    public void handleSpectate(CSpectatePacket packetIn) {}

    @Override
    public void handleResourcePackStatus(CResourcePackStatusPacket packetIn) {}

    @Override
    public void processSteerBoat(CSteerBoatPacket packetIn) {}

    @Override
    public void onDisconnect(ITextComponent reason) {}

    @Override
    public void sendPacket(IPacket<?> packetIn) {}

    @Override
    public void sendPacket(IPacket<?> packetIn, @Nullable GenericFutureListener<? extends Future<? super Void>> futureListeners) {}

    @Override
    public void processHeldItemChange(CHeldItemChangePacket packetIn) {}

    @Override
    public void processChatMessage(CChatMessagePacket packetIn) {}

    @Override
    public void handleAnimation(CAnimateHandPacket packetIn) {}

    @Override
    public void processEntityAction(CEntityActionPacket packetIn) {}

    @Override
    public void processUseEntity(CUseEntityPacket packetIn) {}

    @Override
    public void processClientStatus(CClientStatusPacket packetIn) {}

    @Override
    public void processCloseWindow(CCloseWindowPacket packetIn) {}

    @Override
    public void processClickWindow(CClickWindowPacket packetIn) {}

    @Override
    public void processPlaceRecipe(CPlaceRecipePacket packetIn) {}

    @Override
    public void processEnchantItem(CEnchantItemPacket packetIn) {}

    @Override
    public void processCreativeInventoryAction(CCreativeInventoryActionPacket packetIn) {}

    @Override
    public void processConfirmTransaction(CConfirmTransactionPacket packetIn) {}

    @Override
    public void processUpdateSign(CUpdateSignPacket packetIn) {}

    @Override
    public void processKeepAlive(CKeepAlivePacket packetIn) {}

    @Override
    public void processPlayerAbilities(CPlayerAbilitiesPacket packetIn) {}

    @Override
    public void processClientSettings(CClientSettingsPacket packetIn) {}

    @Override
    public void processCustomPayload(CCustomPayloadPacket packetIn) {}

    @Override
    public void func_217263_a(CSetDifficultyPacket p_217263_1_) {}

    @Override
    public void func_217261_a(CLockDifficultyPacket p_217261_1_) {}
}
