package org.luvx.kafka.streams.processor;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;
import java.util.Arrays;

/**
 * @package: org.luvx.kafka.stream.processor
 * @author: Ren, Xie
 * @desc:
 */
public class WordCountProcessor implements Processor<String, String> {
    private ProcessorContext            context;
    private KeyValueStore<String, Long> kvStore;

    @Override
    public void init(final ProcessorContext context) {
        this.context = context;
        this.context.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, timestamp -> {
            try (final KeyValueIterator<String, Long> it = kvStore.all()) {
                while (it.hasNext()) {
                    final KeyValue<String, Long> entry = it.next();
                    context.forward(entry.key, entry.value.toString());
                }
            }
        });
        kvStore = (KeyValueStore<String, Long>) context.getStateStore("Counts");
    }

    @Override
    public void process(String key, String value) {
        Arrays.stream(value.split(" ")).forEach(
                word -> {
                    Long oldValue = kvStore.get(word);
                    if (oldValue == null) {
                        kvStore.put(word, 1L);
                    } else {
                        kvStore.put(word, oldValue + 1L);
                    }
                }
        );
    }

    @Override
    public void close() {
    }
}
