//package code.book.stream.sessionwindow
//
//import java.text.SimpleDateFormat
//
//import org.apache.flink.streaming.api.TimeCharacteristic
//import org.apache.flink.streaming.api.scala._
//import org.apache.flink.streaming.api.scala.function.RichWindowFunction
//import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
//import org.apache.flink.streaming.api.windowing.time.Time
//import org.apache.flink.streaming.api.windowing.windows.TimeWindow
//import org.apache.flink.util.Collector
//
//object SessionWindowExample02 {
//  def main(args: Array[String]) {
//
//    //1.创建执行环境，并设置为使用EventTime
//    val env = StreamExecutionEnvironment.getExecutionEnvironment
//    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
//
//    //2.创建数据流，并进行数据转化
//    val source = env.socketTextStream("qingcheng11", 9999)
//    case class SalePrice(time: Long, boosName: String, productName: String, price: Double)
//    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    val dst1: DataStream[SalePrice] = source.filter(_.nonEmpty).map(value => {
//      val columns = value.split(",")
//      SalePrice(format.parse(columns(0).trim).getTime, columns(1).trim, columns(2).trim, columns(3).trim.toDouble)
//    })
//    //3.使用EventTime进行求最值操作
//    val dst2: DataStream[WinStatus] = dst1
//      .assignAscendingTimestamps(_.time)
//      .keyBy(_.productName)
//      .window(EventTimeSessionWindows.withGap(Time.seconds(2)))
//      .apply(new MyRichWindowFunction())
//
//
//    case class WinStatus(k:String,b:String,e:String,db:String,de:String,dc:Double)
//    class MyRichWindowFunction extends RichWindowFunction[SalePrice,WinStatus,String,TimeWindow] {
//      override def apply(key: String, window: TimeWindow, input: Iterable[SalePrice], out: Collector[WinStatus]): Unit = {
//        val list = input.toList.sortBy(_.time)
//        val window_start_time = format.format(window.getStart)
//        val window_end_time = format.format(window.getEnd)
//        val window_size = input.size
//      out.collect( new WinStatus(key,window_start_time,window_end_time,format.format(list.head.time),format.format(list.last.time),window_size))
//      }
//    }
//
//      //4.显示结果
//    dst2.print()
//
//    //5.触发流计算
//    env.execute()
//  }
//}