package org.luvx.kafka.streams.processor;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;

/**
 * @package: org.luvx.kafka.stream.processor
 * @author: Ren, Xie
 * @desc:
 */
public class WordCountProcessor implements Processor<String, String> {
    private ProcessorContext               context;
    private KeyValueStore<String, Integer> kvStore;

    @Override
    public void init(final ProcessorContext context) {
        this.context = context;
        this.context.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, timestamp -> {
            try (final KeyValueIterator<String, Integer> iter = kvStore.all()) {
                System.out.println("----------- " + timestamp + " ----------- ");
                while (iter.hasNext()) {
                    final KeyValue<String, Integer> entry = iter.next();
                    System.out.println("[" + entry.key + ", " + entry.value + "]");
                    context.forward(entry.key, entry.value.toString());
                }
            }
        });
        this.kvStore = (KeyValueStore<String, Integer>) context.getStateStore("Counts");
    }

    @Override
    public void process(String key, String value) {
        final String[] words = value.split(" ");
        for (final String word : words) {
            final Integer oldValue = this.kvStore.get(word);
            if (oldValue == null) {
                this.kvStore.put(word, 1);
            } else {
                this.kvStore.put(word, oldValue + 1);
            }
        }
    }

    @Override
    public void close() {
    }
}
