
```bash
./bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
./bin/kafka-server-start.sh -daemon config/server.properties
```


## Client 

消息生产, 消费

分区器

拦截器

## Connect

```shell
# 单节点
bin/connect-standalone.sh config/connect-standalone.properties \
    config/connect-file-source.properties config/connect-file-sink.properties
# 集群
bin/connect-distributed.sh config/connect-distributed.properties
```

> https://kafka.apache.org/documentation/#connect_rest

```http request
### 查询

GET http://luvx:8083/connectors HTTP/1.1

### 创建source

POST http://luvx:8083/connectors HTTP/1.1
Content-type: application/json
Accept: application/json

{
    "name": "test-file-source",
    "config": {
        "connector.class": "FileStreamSource",
        "tasks.max": "1",
        "topic": "connect-test",
        "file": "test.txt"
    }
}

### 创建sink

POST http://luvx:8083/connectors HTTP/1.1
Content-type: application/json
Accept: application/json

{
    "name": "test-file-sink",
    "config": {
        "connector.class": "FileStreamSink",
        "tasks.max": "1",
        "topics": "connect-test",
        "file": "test.sink.txt"
    }
}

### 查看

GET http://luvx:8083/connectors/test-file-source/config HTTP/1.1
```

https://my.oschina.net/hnrpf/blog/1555915

https://blog.csdn.net/u011687037/article/details/57411790

https://yanbin.blog/kafka-connect-how-to/#more-9655

实现connect的关键类

```Java
org.apache.kafka.connect.source.SourceConnector
org.apache.kafka.connect.source.SourceTask
org.apache.kafka.connect.source.SourceRecord
org.apache.kafka.connect.sink.SinkConnector
org.apache.kafka.connect.sink.SinkTask
org.apache.kafka.connect.sink.SinkRecord
org.apache.kafka.connect.storage.Converter
org.apache.kafka.connect.transforms.Transformation
```

## Streams