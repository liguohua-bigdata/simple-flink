//package code.book.stream.outputformat.scala
//
//import code.book.batch.outputformat.scala.MultipleTextOutputFormat001
//import org.apache.flink.api.scala.hadoop.mapred.HadoopOutputFormat
//import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
//import org.apache.hadoop.fs.Path
//import org.apache.hadoop.mapred.{FileOutputFormat, JobConf}
//
///**
//  * Created by liguohua on 11/12/2016.
//  */
//object Test {
//  def main(args: Array[String]): Unit = {
//    //1.创建流执行环境
//    val env = StreamExecutionEnvironment.getExecutionEnvironment
//
//    //2.准备数据
//    val dataStream:DataStream[(String,String)] = env.fromElements(
//      ("zhangsan","12"),
//      ("lisi","13"),
//      ("lisi","14"),
//      ("lisi","15")
//    )
//    //3.多路径输出的HadoopOutputFormat
//    val multipleTextOutputFormat = new MultipleTextOutputFormat001[String, String]()
//    val jobConf = new JobConf()
//    val filePath = "hdfs://qingcheng12:9000/output/flink/MultipleTextOutputFormat/scala/004"
//    FileOutputFormat.setOutputPath(jobConf, new Path(filePath))
//    val format = new HadoopOutputFormat[String, String](multipleTextOutputFormat, jobConf)
//    dataStream.setParallelism(1).writeUsingOutputFormat(format)
//
//    env.execute()
//
//
//  }
//
//}
