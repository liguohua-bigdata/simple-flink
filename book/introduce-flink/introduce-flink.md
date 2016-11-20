##《flink简介》
```
1.flink和spark类似，是一个通用的，基于内存计算的，大数据处理引擎。
2.2008年是德国柏林理工大学一个研究性项目，用Java和Scala混合编写而成的。
3.2014年被Apache孵化器所接受，迅速地成为了阿帕奇顶级项目ASF(Apache Software Foundation)。
```
###1.官方网站
```
https://flink.apache.org
```

###2.github地址
```
https://github.com/apache/flink
```

###3.flink基本架构
![](images/Picture0.png) 
```
1.flink和Hadoop一样是一个主从式的分布式系统,有主节点（master）和从节点（worker）组成。
2.如果主节点不做HA，那么系统中有一个主节点和多个从节点组成。
3.如果主节点做了HA，那么系统中有多个主节点和多个从节点组成。
4.主节点的：负责分发计算任务，负责监控计算任务的执行情况。
5.从节点的：负责执行计算任务，负责报告计算任务的执行情况。
6.flinK使用一个client来提交计算任务。
```

###4.flink和hdfs结合
![](images/Picture1.png) 
```
1.flink作为大数据生态圈的一员，它和Hadoop的hdfs是兼容的。
2.一般将namenode和jobmanager部署到一起，将datanode和taskmanager部署到一起。
3.flink也能照顾到数据的本地行，移动计算而不是移动数据。
```

###5.flink的软件栈
![](images/Snip20161103_10.png) 
```
flink deploy:
    有三种部署方式
    1.本地部署：在本地启动基于单个jvm的flink实例。
    2.集群部署：在集群中可以单独部署成standalone模式，也可以采用hadoop的YARN进行部署成yarn模式
    3.云部署：兼容Google的云服务GCE(Google Compute Engine)，也兼容amazon的云服务AWS(Amazon Web Services)。
flink core:
    flink的核心是一个分布式基于流的数据处理引擎,将一切处理都认为是流处理，将批处理看成流处理的一个特例。
    这与spark正好相反，spark是将一切处理都认为是批处理，将流处理看成批处理的一个特例。spark的流处理不是真正的流处理
    它是一种微型批处理（micro batch），因此spark的流处理实的时性不是很高，spark streaming定位是准实时流处理引擎。
    而flink是真正的流处理系统，它的实时性要比spark高出不少，它对标是Twitter开源的storm和heron,他是一个真正的大数据
    实时分析系统。
flink API: 
    flink的API分为两个部分
    1.流处理API，流处理主要是基于dataStream
    2.批处理API，批处理主要是基于dataSet
flink liberaries:    
    Flink还针对特定的应用领域提供了相应的软件库，方便适应特定领域的操作。主要包括
    1.flink table：主要用于处理关系型的结构化数据，对结构化数据进行查询操作，将结构化数据抽象成关系表，并通过类SQL的
       DSL对关系表进行各种查询操作。提供SQL on bigdata的功能,flink table既可以在流处理中使用SQL,也可以在批处理中
       使用SQL,对应sparkSQL.
    2.flink gelly：主要用于图计算领域，提供相关的图计算API和图计算算法的实现,对应spark graph。
    3.flink ML（machine leaning）：主要用于机器学习领域，提供了机器学习Pipelines APIh和多种机器学习算法的实现，
      对应sparkML
    4.flink CEP（Complex event processing）：主要用于复杂事件处理领域。
```


