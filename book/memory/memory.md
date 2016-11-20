#flink的内存管理机制
##一、基于JVM的大数据生态圈
###1.bigdata on jvm
```
1.现在大多数开源大数据处理框架都是基于jvm的，像 Apache Hadoop,Apache Spark,Apache Hbase,
  Apache Kafka,Apache Flink等。
2.JVM上的程序一方面享受着它带来的好处，也要承受着JVM带来的弊端。
```
###2.jvm的弊端
```
1.Java对象存储密度低
例一：一个只包含boolean属性的对象占用了16个字节内存：对象头占了8个，boolean 属性占了1个，对齐填充占了7个。
而实际上只需要一个bit（1/8字节）就够了。
例二：“abcd”这样简单的字符串在UTF-8编码中需要4个字节存储，但Java采用UTF-16编码存储字符串，需要8个字节存储
“abcd”，同时Java对象还对象header等其他额外信息，一个4字节字符串对象，在Java中需要48字节的空间来存储。
对于大部分的大数据应用，内存都是稀缺资源，更有效率的内存存储，
则意味着CPU数据访问吞吐量更高，以及更少的磁盘落地可能。

2.
```


###1.官方网站
```
https://flink.apache.org
```

###2.github地址
连接地址
```
http://flink.apache.org/news/2015/05/11/Juggling-with-Bits-and-Bytes.html
http://wuchong.me/blog/2016/04/29/flink-internals-memory-manage/
```
