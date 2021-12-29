package mob_grinding_utils.fakeplayer;

import java.net.SocketAddress;
import java.util.Set;

import javax.annotation.Nullable;
import javax.crypto.Cipher;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundContainerAckPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ServerboundEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;

public class FakeNetHandler extends ServerGamePacketListenerImpl {
    public FakeNetHandler(MinecraftServer server, ServerPlayer playerIn) {
        super(server, new FakeManager(PacketFlow.CLIENTBOUND), playerIn);
    }

    private static class FakeManager extends Connection {
        public FakeManager(PacketFlow packetDirection) {
            super(packetDirection);
        }

        @Override
        public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {}

        @Override
        public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception {}

        @Override
        public void setProtocol(ConnectionProtocol newState) {}

        @Override
        public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {}

        @Override
        protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_) throws Exception {}

        @Override
        public void send(Packet<?> packetIn) {}

        @Override
        public void send(Packet<?> packetIn, @Nullable GenericFutureListener<? extends Future<? super Void>> p_201058_2_) {}

        @Override
        public void tick() {}

        @Override
        protected void tickSecond() {}

        @Override
        public SocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public void disconnect(Component message) {}

        @Override
        public boolean isConnecting() {
            return true;
        }

        @Override
        public boolean isMemoryConnection() {
            return false;
        }

        @Override
        public void setEncryptionKey(Cipher p_244777_1_, Cipher p_244777_2_) {}

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public void setReadOnly() {}

        @Override
        public void setupCompression(int threshold) {}

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
    public void disconnect(Component textComponent) {}

    @Override
    public void handlePlayerInput(ServerboundPlayerInputPacket packetIn) {}

    @Override
    public void handleMoveVehicle(ServerboundMoveVehiclePacket packetIn) {}

    @Override
    public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packetIn) {}

    @Override
    public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packetIn) {}

    @Override
    public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket p_241831_1_) {}

    @Override
    public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket packetIn) {}

    @Override
    public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packetIn) {}

    @Override
    public void handleSetCommandBlock(ServerboundSetCommandBlockPacket packetIn) {}

    @Override
    public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket packetIn) {}

    @Override
    public void handlePickItem(ServerboundPickItemPacket packetIn) {}

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
    public void teleport(double x, double y, double z, float yaw, float pitch, Set<ClientboundPlayerPositionPacket.RelativeArgument> relativeSet) {}

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
    public void send(Packet<?> packetIn, @Nullable GenericFutureListener<? extends Future<? super Void>> futureListeners) {}

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
    public void handleContainerAck(ServerboundContainerAckPacket packetIn) {}

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
}
