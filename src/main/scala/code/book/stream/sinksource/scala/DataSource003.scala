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
