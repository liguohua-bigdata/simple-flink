#第一部分：flink概况
##一、flink简介
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

#第二部分：flink的特性
##一、流处理特性
###1.高吞吐，低延时  
有图有真相，有比较有差距。且看下图：
![](images/streaming_performance.png) 
```
1.flink的吞吐量大
2.flink的延时低
3.flink的配置少
```

###2.支持Event-Time 和乱序-Event
![](images/out_of_order_stream.png) 
```
1.flink支持流处理
2.flink支持在Event-Time上的窗口处理
3.因为有Event-Time做保障，即使消息乱序或延时也能轻松应对。
```

###3.支持Stateful-data的Exactly-once处理方式
![](images/exactly_once_state.png) 
```
1.flink支持自定义状态
2.flink的checkpoint机制保障即便在failure的情况下Stateful-data的Exactly-once处理方式。
```
   
###4.支持高度灵活的窗口操作
![](images/windows-2.png) 
```
1.flink支持time-Window，count-window, session-window,data-window等多种窗口操作。
2.flink支持多种触发窗口操作的条件，以便应对各种流处理的情况。
```

###5.通过Backpressure机制支持不间断的流处理
![](images/continuous_streams.png) 
```
1.flink支持long-live流处理。
2.flink支持slow-sinks背压fast-sources,以保障流处理的不间断
```

###6.通过轻量级分布式Snapshot机制支持Fault-tolerance
![](images/distributed_snapshots.png) 
```
1.flink支持Chandy-Lamport轻量级分布式快照来保障容错处理
2.Chandy-Lamport快照是轻量级的，在保障强一致性的同时，不影响其高吞吐。
```


##二、流处理，批处理合二为一

###1.同一个运行时环境，同时支持流处理，批处理
![](images/one_runtime.png) 
```
1.flink的一套runtime环境，统一了流处理，批处理，两大业务场景
2.flink本质是一个流处理系统，同时它将批处理看出特殊的流处理，因此也能应付批处理的场景

注意：
1.这与spark相反，spark本质是一个批处理系统，它将流处理看成特殊的批处理的。
2.spark-streaming本质是mirc-batch，无论多么mirc依然是batch,因此延时较大。
3.spark的本质是批处理，它将流处理看出无边界的批处理
4.flink的本质是流处理，它将批处理看出有边界的流处理。
```

###2.实现了自己的内存管理机制
![](images/memory_heap_division.png) 
```
1.flinK在jvm内部实现了自己的内存管理机制，以提高内存使用效率，防止大规模GC.
2.flink将大规模的数据存放到out-heap内存，以防止在jvm的heap中创建大量对象，而引起大规模GC.

注意：
不知spark是否受到flink的启发，现如今spark也实现了自己的内存管理机制，那就是Tungsten计划。
```


###3.支持迭代和增量迭代
![](images/iterations.png) 
```
1.flinK支持迭代和增量迭代操作（这一特性在图计算和机器学习领域非常有用）
2.增量迭代可以根据计算的依赖关系，优化计算环境，获得最好的计算效率
```
###hadoop MR的迭代计算   
![](images/img10.wtoutiao.com.jpeg)     
###spark的迭代计算   
![](images/55a75f7c5c033_middle.jpg.png)    
###flink的迭代计算   
![](images/55a75f89b2d7a_middle.jpg.png)    
```
spark和Hadoop的迭代计算都是在driver端由用户自己实现的，flink是原生支持迭代计算。这一点上做的比较优秀。
```

###4.支持程序优化
![](images/optimizer_choice.png) 
```
1.flink的批处理场景下可以根据计算的依赖关系，自动的避免一些昂贵的不必要的中间操作（诸如：sort,shuffle等）
2.flink会自动缓存一些中间结果，以便后续计算的多次使用，这样能显著的提高效率。
```


##三、类库和API

###1.流处理程序

```
flink的 DataStream API在流处理的业务场景下，支持多种数据转换，支持用户自定义状态的操作，支持灵活的窗口操作！
```

示例程序：  
```scala

//1.定义case class
case class Word(word: String, freq: Long)

//2.定义数据源
val texts: DataStream[String] = ...

//3.支持数据的流操作
val counts = text
  .flatMap { line => line.split("\\W+") }
  .map { token => Word(token, 1) }
  .keyBy("word")
  .timeWindow(Time.seconds(5), Time.seconds(1))
  .sum("freq")
```
程序说明：
```
以上程序演示了如何在一个数据流上，对源源不断流入的消息进行一个word-count操作！
```

###2.批处理程序

```
flink的 DataSet API具有以下特性：
    1.支持Java和scale开发语言
    2.支持编写类型安全的程序
    3.能够编写漂亮的易于维护的程序
    4.支持丰富的数据类型
    5.支持键值对数据类型
    6.支持丰富的算子
```

示例程序：  
```scala
//1.定义case class 
case class Page(pageId: Long, rank: Double)
case class Adjacency(id: Long, neighbors: Array[Long])

//2.执行运算
val result = initialRanks.iterate(30) { pages =>
  pages.join(adjacency).where("pageId").equalTo("id") {

    (page, adj, out: Collector[Page]) => {
      out.collect(Page(page.pageId, 0.15 / numPages))

      val nLen = adj.neighbors.length
      for (n <- adj.neighbors) {
        out.collect(Page(n, 0.85 * page.rank / nLen))
      }
    }
  }
  .groupBy("pageId").sum("rank")
}
```
程序说明：
```
以上程序演示了一个在图计算中PageRank算法的核心代码！
```


###3.类库和软件栈

```
flinK为支持各种计算场景提供了相应的高层api
    1.提供DataSet API来支持批处理场景
    2.提供DataSream API来支持批流理场景
    3.提供CEP API来支持F复杂事件处理（Complex Event Processing）的场景
    4.提供Gelly API来支持图分析场景
    5.提供Table API来支持SQL-ON-BIGDATA场景
    6.提供FlinkML API来支持机器学习场景
    
```
###flink的软件栈  
![](images/flink-stack-frontpage.png)   

###spark的软件栈  
![](images/spark-stack.png)   
```
1.在高层api方面，flink和spark几乎一样都覆盖了大多数的大数据处理场景。
2.由于发展的原因spark在API方面要超出flink，这方面flink也在大力发展。
```


##四、flink的生态系统
![](images/ecosystem_logos.png) 

```
flink和开源大数据处理的各种框架有很好的集成,这样它就能和其他框架密切合作形成大数据的统一的解决方案。
flink支持YARN,HDFS,Kafka,hbase,alluxio等其他大数据系统的集成。
```

###flink生态系统  
![](images/middle.jpg.png)   
###spark生态系统  
![](images/spark-ecosystem.jpeg) 

```
flink和spark都在努力的打造以自己为中心的大数据生态系统。在现阶段spark是具有一定优势
，但是flink凭借着其优秀的架构，先进的设计，良好的势头，在大数据领域中也能占据一席之地。
```