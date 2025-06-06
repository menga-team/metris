package dev.menga.metris.cable;

import com.google.common.reflect.ClassPath;
import dev.menga.metris.Metris;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class CableProtocol {

    public final static Map<Byte, Class<? extends CablePacket>> PACKET_IDS = new HashMap<>();

    public static int registerPackets(String packagePath) throws IOException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InetSocketAddress dummy = new InetSocketAddress("localhost", 0);
        int count = 0;
        for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
            if (info.getName().startsWith(packagePath)) {
                final Class<?> clazz = info.load();
                try {
                    Object instance = clazz.getConstructor(InetSocketAddress.class).newInstance(dummy);
                    if (instance instanceof CablePacket p) {
                        PACKET_IDS.put(p.id(), p.getClass());
                        Metris.getLogger().info("Registered packet {} ({})", clazz.getName(), p.id());
                        count++;
                    }
                } catch (Exception e) {
                    Metris.getLogger().error("Failed to register packet {}: {}", clazz, e);
                }
            }
        }
        return count;
    }
}
