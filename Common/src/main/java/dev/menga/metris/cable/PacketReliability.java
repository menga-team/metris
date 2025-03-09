package dev.menga.metris.cable;

import lombok.Getter;

@Getter
public enum PacketReliability {
    UNRELIABLE(0),
    RELIABLE(1),
    RELIABLE_ORDERED(2);

    private final int id;

    PacketReliability(int id) {
        this.id = id;
    }
}
