package dev.menga.metris.cable;

import lombok.Getter;

import java.net.InetSocketAddress;

public abstract class CablePacket {

    // CABLE -> Of the remaining 4 bits, 2 are used to encode the reliability.
    public final static byte[] MAGIC = {(byte) 0xCA, (byte) 0xB1, (byte) 0xE};

    @Getter
    private InetSocketAddress sender;
    @Getter
    private int seq;

    public CablePacket(InetSocketAddress sender) {
        this.sender = sender;
    }



}
