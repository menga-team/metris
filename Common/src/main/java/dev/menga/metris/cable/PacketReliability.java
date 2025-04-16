package dev.menga.metris.cable;

import lombok.Getter;

@Getter
public enum PacketReliability {
    UNRELIABLE(0),
    RELIABLE(1),
    RELIABLE_ORDERED(2);

    private final byte id;

    PacketReliability(int id) {
        this.id = (byte) id;
    }

    public static PacketReliability fromId(int id) throws PacketDecodeException {
        switch (id) {
            case 0 -> {
                return UNRELIABLE;
            }
            case 1 -> {
                return RELIABLE;
            }
            case 2 -> {
                return RELIABLE_ORDERED;
            }
            default -> throw new PacketDecodeException("Invalid reliability.");
        }
    }
}
