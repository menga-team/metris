package dev.menga.metris.server;

import dev.menga.metris.Metris;
import dev.menga.metris.cable.CablePacket;
import dev.menga.metris.cable.CableProtocol;
import dev.menga.metris.cable.PacketDecodeException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.net.InetSocketAddress;
import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
        InetSocketAddress sender = packet.sender();
        ByteBuf buf = packet.content();
        if (buf.readableBytes() == 0) {
            return;
        }

        byte id = buf.readByte();
        Class<? extends CablePacket> clazz = CableProtocol.PACKET_IDS.get(id);
        if (clazz == null) {
            throw new PacketDecodeException("Invalid packet id: " + id);
        }
        Metris.getLogger().debug("Decoded new {}: {}", clazz.getName(), clazz.toString());
        CablePacket cablePacket = clazz.getConstructor(InetSocketAddress.class).newInstance(sender);
        cablePacket.decode(buf);
        out.add(cablePacket);
    }
}
