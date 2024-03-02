package mob_grinding_utils.fakeplayer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.handshake.ClientIntent;
import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.SampleLogger;
import net.minecraft.world.entity.RelativeMovement;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import java.net.SocketAddress;
import java.util.Set;
import java.util.function.Consumer;

public class FakeNetHandler extends ServerGamePacketListenerImpl {
    public FakeNetHandler(MinecraftServer pServer, ServerPlayer pPlayer, CommonListenerCookie pCookie) {
        super(pServer, new FakeConnection(), pPlayer, pCookie);
    }

    private static class FakeManager extends Connection {
        public FakeManager(PacketFlow packetDirection) {
            super(packetDirection);
        }

        @Override
        public void setListener(PacketListener pHandler) {}

        @Override
        public void setListenerForServerboundHandshake(PacketListener pPacketListener) {}

        @Override
        public void initiateServerboundStatusConnection(String pHostName, int pPort, ClientStatusPacketListener pDisconnectListener) {}

        @Override
        public void initiateServerboundPlayConnection(String pHostName, int pPort, ClientLoginPacketListener pDisconnectListener) {}

        @Override
        public void setClientboundProtocolAfterHandshake(ClientIntent pIntention) {}

        @Override
        public void send(Packet<?> pPacket, @Nullable PacketSendListener pSendListener) {}

        @Override
        public void send(Packet<?> pPacket, @Nullable PacketSendListener pListener, boolean pFlush) {}

        @Override
        public void runOnceConnected(Consumer<Connection> pAction) {}

        @Override
        public void flushChannel() {}

        @Override
        public String getLoggableAddress(boolean pLogIps) {
            return "";
        }

        @Override
        public void configurePacketHandler(ChannelPipeline pPipeline) {}

        @Override
        public boolean isEncrypted() {
            return false;
        }

        @Nullable
        @Override
        public PacketListener getPacketListener() {
            return null;
        }

        @Nullable
        @Override
        public Component getDisconnectedReason() {
            return null;
        }

        @Override
        public void setupCompression(int pThreshold, boolean pValidateDecompressed) {}

        @Override
        public float getAverageReceivedPackets() {
            return 0.0f;
        }

        @Override
        public float getAverageSentPackets() {
            return 0.0f;
        }

        @Override
        public void setBandwidthLogger(SampleLogger pBandwithLogger) {}

        @Override
        public void resumeInboundAfterProtocolChange() {}

        @Override
        public void suspendInboundAfterProtocolChange() {}

        @Override
        public void channelActive(@Nonnull ChannelHandlerContext p_channelActive_1_) throws Exception {}

        @Override
        public void channelInactive(@Nonnull ChannelHandlerContext p_channelInactive_1_) {}

        @Override
        public void exceptionCaught(@Nonnull ChannelHandlerContext p_exceptionCaught_1_, @Nonnull Throwable p_exceptionCaught_2_) {}

        @Override
        protected void channelRead0(@Nonnull ChannelHandlerContext p_channelRead0_1_, @Nonnull Packet<?> p_channelRead0_2_) {}

        @Override
        public void send(@Nonnull Packet<?> packetIn) {}

        @Override
        public void tick() {}

        @Override
        protected void tickSecond() {}

        @Override
        public SocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public void disconnect(@Nonnull Component message) {}

        @Override
        public boolean isConnecting() {
            return true;
        }

        @Override
        public boolean isMemoryConnection() {
            return false;
        }

        @Override
        public void setEncryptionKey(@Nonnull Cipher p_244777_1_, @Nonnull Cipher p_244777_2_) {}

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public void setReadOnly() {}

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
    public void resetPosition() {}

    @Override
    public void disconnect(@Nonnull Component textComponent) {}

    @Override
    public void handlePlayerInput(@Nonnull ServerboundPlayerInputPacket packetIn) {}

    @Override
    public void handleMoveVehicle(@Nonnull ServerboundMoveVehiclePacket packetIn) {}

    @Override
    public void handleAcceptTeleportPacket(@Nonnull ServerboundAcceptTeleportationPacket packetIn) {}

    @Override
    public void handleRecipeBookSeenRecipePacket(@Nonnull ServerboundRecipeBookSeenRecipePacket packetIn) {}

    @Override
    public void handleRecipeBookChangeSettingsPacket(@Nonnull ServerboundRecipeBookChangeSettingsPacket p_241831_1_) {}

    @Override
    public void handleSeenAdvancements(@Nonnull ServerboundSeenAdvancementsPacket packetIn) {}

    @Override
    public void handleCustomCommandSuggestions(@Nonnull ServerboundCommandSuggestionPacket packetIn) {}

    @Override
    public void handleSetCommandBlock(@Nonnull ServerboundSetCommandBlockPacket packetIn) {}

    @Override
    public void handleSetCommandMinecart(@Nonnull ServerboundSetCommandMinecartPacket packetIn) {}

    @Override
    public void handlePickItem(@Nonnull ServerboundPickItemPacket packetIn) {}

    @Override
    public void handleRenameItem(ServerboundRenameItemPacket packetIn) {}

    @Override
    public void handleSetBeaconPacket(ServerboundSetBeaconPacket packetIn) {}

    @Override
    public void handleSetStructureBlock(ServerboundSetStructureBlockPacket packetIn) {}

    @Override
    public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket p_217262_1_) {}

    @Override
    public void handleJigsawGenerate(ServerboundJigsawGeneratePacket p_230549_1_) {}

    @Override
    public void handleSelectTrade(ServerboundSelectTradePacket packetIn) {}

    @Override
    public void handleEditBook(ServerboundEditBookPacket packetIn) {}

    @Override
    public void handleEntityTagQuery(ServerboundEntityTagQuery packetIn) {}

    @Override
    public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQuery packetIn) {}

    @Override
    public void handleMovePlayer(ServerboundMovePlayerPacket packetIn) {}

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch, Set<RelativeMovement> relativeSet) {}

    @Override
    public void handlePlayerAction(ServerboundPlayerActionPacket packetIn) {}

    @Override
    public void handleUseItemOn(ServerboundUseItemOnPacket packetIn) {}

    @Override
    public void handleUseItem(ServerboundUseItemPacket packetIn) {}

    @Override
    public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket packetIn) {}

    @Override
    public void handleResourcePackResponse(ServerboundResourcePackPacket packetIn) {}

    @Override
    public void handlePaddleBoat(ServerboundPaddleBoatPacket packetIn) {}

    @Override
    public void onDisconnect(Component reason) {}

    @Override
    public void send(Packet<?> packetIn) {}

    @Override
    public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packetIn) {}

    @Override
    public void handleChat(ServerboundChatPacket packetIn) {}

    @Override
    public void handleAnimate(ServerboundSwingPacket packetIn) {}

    @Override
    public void handlePlayerCommand(ServerboundPlayerCommandPacket packetIn) {}

    @Override
    public void handleInteract(ServerboundInteractPacket packetIn) {}

    @Override
    public void handleClientCommand(ServerboundClientCommandPacket packetIn) {}

    @Override
    public void handleContainerClose(ServerboundContainerClosePacket packetIn) {}

    @Override
    public void handleContainerClick(ServerboundContainerClickPacket packetIn) {}

    @Override
    public void handlePlaceRecipe(ServerboundPlaceRecipePacket packetIn) {}

    @Override
    public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packetIn) {}

    @Override
    public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packetIn) {}

    @Override
    public void handleSignUpdate(ServerboundSignUpdatePacket packetIn) {}

    @Override
    public void handleKeepAlive(ServerboundKeepAlivePacket packetIn) {}

    @Override
    public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packetIn) {}

    @Override
    public void handleClientInformation(ServerboundClientInformationPacket packetIn) {}

    @Override
    public void handleCustomPayload(ServerboundCustomPayloadPacket packetIn) {}

    @Override
    public void handleChangeDifficulty(ServerboundChangeDifficultyPacket p_217263_1_) {}

    @Override
    public void handleLockDifficulty(ServerboundLockDifficultyPacket p_217261_1_) {}

    @Override
    public void handlePong(ServerboundPongPacket p_143652_) {
    }

    private static final class FakeConnection extends Connection {
        public FakeConnection() {
            super(PacketFlow.SERVERBOUND);
        }

        @Override
        public void setListener(PacketListener listener) {}
    }
}
