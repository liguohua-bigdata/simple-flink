package code.book.stream.datastreamapi

import java.lang.Iterable

import org.apache.flink.api.common.functions.CoGroupFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object CoGroup001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //置为使用EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //2.创建Worker数据流
    val source1 = env.fromElements(
      "zhangsan,daqiang,1800,1461756862000",
      "lisi,daqiang,1800,1461756862000",
      "zhangsan,daqiang,1198,1461756868000",
      "lisi,daqiang,1198,1461756868000")
    case class Worker(name: String, bossName: String, salary: Int, time: Long)
    val dst1: DataStream[Worker] = source1.map(value => {
      val columns = value.split(",")
      Worker(columns(0), columns(1), columns(2).trim.toInt, columns(3).trim.toLong)
    }).assignAscendingTimestamps(_.time)


    //3.创建Boos数据流
    val source2 = env.fromElements("daqiang,38,1461756862000", "xiaoqiang,28,1461756868000")
    case class Boss(name: String, age: Int, time: Long)
    val dst2: DataStream[Boss] = source2.map(value => {
      val columns = value.split(",")
      Boss(columns(0), columns(1).trim.toInt, columns(2).trim.toLong)
    }).assignAscendingTimestamps(_.time)



    //4.显示结果
//    //inner
//    dst1.coGroup(dst2).where(_.bossName).equalTo(_.name).window(TumblingEventTimeWindows.of(Time.seconds(3))).apply(new CoGroupInnerJoin()).print()
//
//    //left
//    dst1.coGroup(dst2).where(_.bossName).equalTo(_.name).window(TumblingEventTimeWindows.of(Time.seconds(3))).apply(new CoGroupLeftOutJoin).print()

    //right
    dst1.coGroup(dst2).where(_.bossName).equalTo(_.name).window(TumblingEventTimeWindows.of(Time.seconds(3))).apply(new CoGroupRightOutJoin).print()
    //5.触发流计算
    env.execute(this.getClass.getName)



    class CoGroupInnerJoin extends CoGroupFunction[Worker, Boss, (String, String, Double)] {
      override def coGroup(worker: Iterable[Worker], boos: Iterable[Boss], collector: Collector[(String, String, Double)]): Unit = {
        /**
          * 将Java中的Iterable对象转换为Scala的Iterable
          * scala的集合操作效率高，简洁
          */
        import scala.collection.JavaConverters._
        val scalaW = worker.asScala.toList
        val scalaB = worker.asScala.toList

        /**
          * Inner Join要比较的是同一个key下，同一个时间窗口内的数据
          */
        if (scalaW.nonEmpty && scalaB.nonEmpty) {
          for (w <- scalaW) {
            for (b <- scalaB) {
              collector.collect(b.bossName, w.name, w.salary)
            }
          }
        }
      }
    }

    class CoGroupLeftOutJoin extends CoGroupFunction[Worker, Boss, (String, Double)] {
      override def coGroup(worker: Iterable[Worker], boos: Iterable[Boss], collector: Collector[(String, Double)]): Unit = {
        /**
          * 将Java中的Iterable对象转换为Scala的Iterable
          * scala的集合操作效率高，简洁
          */
        import scala.collection.JavaConverters._
        val scalaW = worker.asScala.toList
        val scalaB = worker.asScala.toList

        /**
          * Left Join要比较的是同一个key下，同一个时间窗口内的数据
          */
        if (scalaW.nonEmpty && scalaB.nonEmpty) {
          for (w <- scalaW) {
            collector.collect(w.name, w.salary)
          }
        }
      }
    }

    class CoGroupRightOutJoin extends CoGroupFunction[Worker, Boss, (String, String)] {
      override def coGroup(worker: Iterable[Worker], boos: Iterable[Boss], collector: Collector[(String, String)]): Unit = {
        /**
          * 将Java中的Iterable对象转换为Scala的Iterable
          * scala的集合操作效率高，简洁
          */
        import scala.collection.JavaConverters._
        val scalaW = worker.asScala.toList
        val scalaB = worker.asScala.toList

        /**
          * Right Join要比较的是同一个key下，同一个时间窗口内的数据
          */
        if (scalaW.nonEmpty && scalaB.nonEmpty) {
          for (b <- scalaB) {
            collector.collect(b.bossName, b.name)
          }
        }
      }
    }
  }
}
