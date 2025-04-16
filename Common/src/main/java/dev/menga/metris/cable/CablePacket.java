package dev.menga.metris.cable;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import java.net.InetSocketAddress;

public abstract class CablePacket implements NetSerializable{

    // CABLE -> Of the remaining 4 bits, 2 are used to encode the reliability.
    public final static byte[] MAGIC = {(byte) 0xCA, (byte) 0xB1, (byte) 0xE};

    @Getter
    protected InetSocketAddress sender;
    @Getter
    protected PacketReliability reliability;
    @Getter
    protected int seq;

    public CablePacket(InetSocketAddress sender) {
        this.sender = sender;
    }

    public abstract byte id();

    protected abstract void encodeData(ByteBuf buf);

    protected abstract void decodeData(ByteBuf buf);

    public void decode(ByteBuf buf) {
        // ID was already read inorder to identify the packet.
        this.seq = buf.readInt();
        byte[] magic = new byte[MAGIC.length];
        buf.readBytes(magic);
        this.reliability = PacketReliability.fromId(magic[MAGIC.length - 1] >> 4);
        this.decodeData(buf);
    }

    public void encode(ByteBuf buf) {
        buf.writeByte(this.id());
        buf.writeInt(this.getSeq());
        final byte[] magicRel = MAGIC;
        // Reliability is encoded in the last nibble of the magic.
        magicRel[MAGIC.length - 1] |= (byte) (this.getReliability().getId() << 4);
        buf.writeBytes(magicRel);
        this.encodeData(buf);
    }
}
