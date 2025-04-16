package dev.menga.metris.cable;

import io.netty.buffer.ByteBuf;

public interface NetSerializable {
    void encode(ByteBuf buf);
    void decode(ByteBuf buf);
}
