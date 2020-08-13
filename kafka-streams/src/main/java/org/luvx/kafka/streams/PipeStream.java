package org.luvx.kafka.streams;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.luvx.kafka.streams.config.Config;

import java.util.concurrent.CountDownLatch;

/**
 * @author: Ren, Xie
 * @desc:
 */
public class PipeStream {
    public static final String source_topic = "streams-plaintext-input";
    public static final String sink_topic   = "streams-pipe-output";

    public static void main(String[] args) throws Exception {
        final StreamsBuilder builder = new StreamsBuilder();
        builder.stream(source_topic).to(sink_topic);
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, Config.sourceConfig());

        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
