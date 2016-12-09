package code.book.batch.other

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.core.fs.FileSystem.WriteMode

/**
  * Created by liguohua on 10/12/2016.
  */
object DataSink001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val ds1: DataSet[(Int, String)] = env.fromCollection(Map(1 -> "spark", 2 -> "flink"))
    //1.写入到本地，文本文档,NO_OVERWRITE模式下如果文件已经存在，则报错，OVERWRITE模式下如果文件已经存在，则覆盖
    ds1.writeAsText("file:///Users/liguohua/Documents/F/code/idea/git/simple-flink/src/main/scala/code/book/batch/other/test", WriteMode.OVERWRITE)
    env.execute()





  }

}
