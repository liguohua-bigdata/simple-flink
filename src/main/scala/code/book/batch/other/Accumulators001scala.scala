//package code.book.batch.other
//
//import org.apache.flink.api.common.JobExecutionResult
//import org.apache.flink.api.common.functions.RichFlatMapFunction
//import org.apache.flink.api.scala._
//import org.apache.flink.util.Collector
//
///**
//  * Created by liguohua on 25/11/2016.
//  */
//object Accumulators001scala {
//  def main(args: Array[String]): Unit = {
//    //1.创建批处理环境
//    val env = ExecutionEnvironment.getExecutionEnvironment
//
//    //2.准备数据
//    val text = env.fromElements(
//      "To be, or not to be,that is the question",
//      "Whether 'tis nobler in the mind to suffer",
//      "The slings and arrows of outrageous fortune",
//      "Or to take arms against a sea of troubles,")
//
//    //3.执行运算
//    val counts=text.flatMap(new RichFlatMapFunction[String, String] {
//      override def flatMap(in: String, collector: Collector[String]): Unit = {
//        this.getRuntimeContext.getLongCounter("lineCountAccumulator").add(1L)
//         val words=in.toUpperCase().split("\\s+")
//        for (word<-words){
//          collector.collect(word)
//        }
//      }
//    }).map((_, 1)).groupBy(0).sum(1)
//
//
//
//    val result: JobExecutionResult = env.execute()
//    val ec: Long = result.getAccumulatorResult("lineCountAccumulator")
//    print(ec)
//  }
//
//}
