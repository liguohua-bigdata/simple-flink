package code.test

import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

/**
  * 递归读取hdfs目录中的所有文件，会遍历各级子目录
  */
object Test002 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val config = new Configuration()
    config.setBoolean("recursive.file.enumeration", true)
    //开启遍历子目录的设置
    val path = "/Users/liguohua/Documents/F/code/idea/git/simple-flink/src/main/scala/code/test/*.*"
    val texLines = env.readTextFile(path).withParameters(config)
    texLines.map(l=>l).print()
//

  }

}