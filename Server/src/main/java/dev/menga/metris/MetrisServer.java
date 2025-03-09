package dev.menga.metris;

public class MetrisServer {

    private int port;

    public MetrisServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {

    }

    public static void main(String[] args) throws Exception {
        int port = 45561;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        Metris.getLogger().info("Starting server on 0.0.0.0:{}", port);
        new MetrisServer(port).run();
    }
}
