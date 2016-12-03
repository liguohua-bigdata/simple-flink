package code.uitls

import org.apache.flink.api.scala._
object ReadHdfsFile {
  def main(args: Array[String]) {
    val filePath="hdfs:///input/mahout/pa3/raw/"
    // 1.设置运行环境
    val env = ExecutionEnvironment.getExecutionEnvironment
    //2.创造测试数据
    val text2 = env.readTextFile(filePath)
    //3.进行测试运算
    text2.print()
  }

}
