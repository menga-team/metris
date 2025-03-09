package dev.menga.metris.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;

public interface PipelineFactory {

    ChannelInitializer<DatagramChannel> createInitializer();

}
