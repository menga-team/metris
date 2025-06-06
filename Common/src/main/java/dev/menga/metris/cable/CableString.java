package dev.menga.metris.cable;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

public class CableString implements NetSerializable {

    @Getter
    private byte[] data;

    public CableString(String str) throws CableStringTooLargeException {
        this.setData(str);
    }

    public void setData(String str) throws CableStringTooLargeException {
        this.setData(str.getBytes(StandardCharsets.UTF_8));
    }

    public void setData(byte[] data) throws CableStringTooLargeException {
        if (data.length > 0xff) {
            throw new CableStringTooLargeException();
        }
        this.data = data;
    }

    public String toString() {
        return new String(this.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeByte(this.getData().length);
        buf.writeBytes(this.getData());
    }

    @Override
    public void decode(ByteBuf buf) {
        int size = buf.readByte();
        byte[] data = new byte[size];
        buf.readBytes(data);
        this.data = data;
    }
}

class CableStringTooLargeException extends Exception {
    public CableStringTooLargeException() {
        super("CableString can only store up to 255 UTF-8 bytes.");
    }
}
