package dev.menga.metris.cable.packet;

import dev.menga.metris.cable.CablePacket;
import dev.menga.metris.cable.CableString;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.net.InetSocketAddress;

public class PlayerLoginPacket extends CablePacket {

    @Getter
    private CableString playerName;

    public PlayerLoginPacket(InetSocketAddress sender) {
        super(sender);
    }

    @Override
    public byte id() {
        return 0x02;
    }

    @Override
    protected void encodeData(ByteBuf buf) {
        this.getPlayerName().encode(buf);
    }

    @Override
    protected void decodeData(ByteBuf buf) {
        this.playerName.decode(buf);
    }
}
