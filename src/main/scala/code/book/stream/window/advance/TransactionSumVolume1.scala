package code.book.stream.window.advance

import java.text.SimpleDateFormat

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Created by liguohua on 04/12/2016.
  */
object TransactionSumVolume1 {

  case class Transaction(szWindCode: String, szCode: Long, nAction: String, nTime: String, seq: Long, nIndex: Long, nPrice: Long,
                         nVolume: Long, nTurnover: Long, nBSFlag: Int, chOrderKind: String, chFunctionCode: String,
                         nAskOrder: Long, nBidOrder: Long, localTime: Long
                        )

  def main(args: Array[String]): Unit = {

    //1.创建执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //2.使用EventTime作为窗口的统计依据
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //3.获取数据流
    val input = env.socketTextStream("qingcheng11", 9999)
    //4.转化数据流
    val parsedStream = input.map(new EventTimeFunction)

    /**
      * assign Timestamp and WaterMark for Event time: eventTime(params should be a Long type)
      */
    val timeValue = parsedStream.assignAscendingTimestamps(_._2)
    val sumVolumePerMinute = timeValue
      .keyBy(_._1)
      .window(TumblingEventTimeWindows.of(Time.seconds(1)))
      .sum(3)
      .name("sum volume per minute")


    /**
      * Step 4. Sink the final result to standard output(.out file)
      */
    sumVolumePerMinute.map(value => (value._1, value._3, value._4)).print()



    env.execute("SocketTextStream for sum of volume Example")


  }

  class EventTimeFunction extends MapFunction[String, (Long, Long, String, Long)] {

    def map(s: String): (Long, Long, String, Long) = {

      val columns = s.split(",")

      val transaction: Transaction = Transaction(columns(0), columns(1).toLong, columns(2), columns(3), columns(4).toLong, columns(5).toLong,
        columns(6).toLong, columns(7).toLong, columns(8).toLong, columns(9).toInt, columns(9), columns(10), columns(11).toLong,
        columns(12).toLong, columns(13).toLong)

      val format = new SimpleDateFormat("yyyyMMddHHmmssSSS")

      val volume: Long = transaction.nVolume
      val szCode: Long = transaction.szCode

      if (transaction.nTime.length == 8) {
        val eventTimeString = transaction.nAction + '0' + transaction.nTime
        val eventTime: Long = format.parse(eventTimeString).getTime
        (szCode, eventTime, eventTimeString, volume)
      } else {
        val eventTimeString = transaction.nAction + transaction.nTime
        val eventTime = format.parse(eventTimeString).getTime
        (szCode, eventTime, eventTimeString, volume)
      }

    }
  }

}