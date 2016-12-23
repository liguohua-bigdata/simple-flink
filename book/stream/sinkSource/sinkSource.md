#一、flink在流处理上的source
```
flink在流处理上的source和在批处理上的source基本一致。大致有4大类
1.基于本地集合的source（Collection-based-source）
2.基于文件的source（File-based-source）
3.基于网络套接字的source（Socket-based-source）
4.自定义的source（Custom-source）
```

##1.基于本地集合的source
```
package code.book.stream.sinksource.scala

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

import scala.collection.immutable.{Queue, Stack}
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object DataSource001 {
  def main(args: Array[String]): Unit = {
    val senv = StreamExecutionEnvironment.getExecutionEnvironment
    //0.用element创建DataStream(fromElements)
    val ds0: DataStream[String] = senv.fromElements("spark", "flink")
    ds0.print()

    //1.用Tuple创建DataStream(fromElements)
    val ds1: DataStream[(Int, String)] = senv.fromElements((1, "spark"), (2, "flink"))
    ds1.print()

    //2.用Array创建DataStream
    val ds2: DataStream[String] = senv.fromCollection(Array("spark", "flink"))
    ds2.print()

    //3.用ArrayBuffer创建DataStream
    val ds3: DataStream[String] = senv.fromCollection(ArrayBuffer("spark", "flink"))
    ds3.print()

    //4.用List创建DataStream
    val ds4: DataStream[String] = senv.fromCollection(List("spark", "flink"))
    ds4.print()

    //5.用List创建DataStream
    val ds5: DataStream[String] = senv.fromCollection(ListBuffer("spark", "flink"))
    ds5.print()

    //6.用Vector创建DataStream
    val ds6: DataStream[String] = senv.fromCollection(Vector("spark", "flink"))
    ds6.print()

    //7.用Queue创建DataStream
    val ds7: DataStream[String] = senv.fromCollection(Queue("spark", "flink"))
    ds7.print()

    //8.用Stack创建DataStream
    val ds8: DataStream[String] = senv.fromCollection(Stack("spark", "flink"))
    ds8.print()

    //9.用Stream创建DataStream（Stream相当于lazy List，避免在中间过程中生成不必要的集合）
    val ds9: DataStream[String] = senv.fromCollection(Stream("spark", "flink"))
    ds9.print()

    //10.用Seq创建DataStream
    val ds10: DataStream[String] = senv.fromCollection(Seq("spark", "flink"))
    ds10.print()

    //11.用Set创建DataStream(不支持)
    //val ds11: DataStream[String] = senv.fromCollection(Set("spark", "flink"))
    //ds11.print()

    //12.用Iterable创建DataStream(不支持)
    //val ds12: DataStream[String] = senv.fromCollection(Iterable("spark", "flink"))
    //ds12.print()

    //13.用ArraySeq创建DataStream
    val ds13: DataStream[String] = senv.fromCollection(mutable.ArraySeq("spark", "flink"))
    ds13.print()

    //14.用ArrayStack创建DataStream
    val ds14: DataStream[String] = senv.fromCollection(mutable.ArrayStack("spark", "flink"))
    ds14.print()

    //15.用Map创建DataStream(不支持)
    //val ds15: DataStream[(Int, String)] = senv.fromCollection(Map(1 -> "spark", 2 -> "flink"))
    //ds15.print()

    //16.用Range创建DataStream
    val ds16: DataStream[Int] = senv.fromCollection(Range(1, 9))
    ds16.print()

    //17.用fromElements创建DataStream
    val ds17: DataStream[Long] = senv.generateSequence(1, 9)
    ds17.print()
    
    senv.execute(this.getClass.getName)
  }
}
```
##2.基于文件的source（File-based-source）
```scala

package code.book.stream.sinksource.scala

import org.apache.flink.streaming.api.scala._
object DataSource003 {
  def main(args: Array[String]): Unit = {
    //0.创建运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //1.读取本地文件
    val text1 = env.readTextFile("file:///$FLINK_HOME/README.txt")
    text1.print()
    //1.读取hdfs文件
    val text2 = env.readTextFile("hdfs://qingcheng11:9000/input/flink/README.txt")
    text2.print()
    env.execute()
  }
}
```
##3.基于网络套接字的source（Socket-based-source）
###方法原型
```
def socketTextStream(hostname: String, port: Int, delimiter: Char = '\n',
maxRetry: Long = 0):DataStream[String]
```
###示例程序
```
package code.book.stream.sinksource.scala

//0.引用必要的元素
import org.apache.flink.streaming.api.scala._
object DataSource002 {
  def main(args: Array[String]): Unit = {
    //0.创建运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //1.定义text1数据流，采用默认值,行分隔符为'\n'，失败重试0次
    val text1 = env.socketTextStream("qingcheng11", 9999)
    text1.print()

    //2.定义text2数据流,行分隔符为'|'，失败重试3次
    val text2 = env.socketTextStream("qingcheng11", 9998, delimiter = '|', maxRetry = 3)
    text2.print()
    //5.触发计算
    env.execute(this.getClass.getName)
  }
}
```

##4.自定义的source（Custom-source,以kafka为例）
```
package code.book.stream.sinksource.scala
import java.util.Properties
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
object DataSource004 {
  def main(args: Array[String]) {

    //1指定kafka数据流的相关信息
    val zkCluster = "qingcheng11,qingcheng12,qingcheng13:2181"
    val kafkaCluster = "qingcheng11:9092,qingcheng12:9092,qingcheng13:9092"
    val kafkaTopicName = "food"
    //2.创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //3.创建kafka数据流
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaCluster)
    properties.setProperty("zookeeper.connect", zkCluster)
    properties.setProperty("group.id", kafkaTopicName)

    val kafka09 = new FlinkKafkaConsumer09[String](kafkaTopicName,
    new SimpleStringSchema(), properties)
    //4.添加数据源addSource(kafka09)
    val text = env.addSource(kafka09).setParallelism(4)
    text.print()

    //5.触发运算
    env.execute("flink-kafka-wordcunt")
  }
}
```
