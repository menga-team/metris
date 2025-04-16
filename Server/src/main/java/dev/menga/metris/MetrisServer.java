package dev.menga.metris;

import dev.menga.metris.cable.CableProtocol;

public class MetrisServer {

    private int port;

    public MetrisServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {

    }

    public static void main(String[] args) throws Exception {
        CableProtocol.registerPackets("dev.menga.metris.cable.packet");

        int port = 45561;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        Metris.getLogger().info("Starting server on 0.0.0.0:{}", port);
        new MetrisServer(port).run();
    }
}
