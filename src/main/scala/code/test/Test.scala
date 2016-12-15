package code.test

package code.book.batch.sinksource.scala

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

/**
  * 递归读取hdfs目录中的所有文件，会遍历各级子目录
  */
object Test {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    // create a configuration object
    val parameters = new Configuration
    // set the recursive enumeration parameter
    parameters.setBoolean("recursive.file.enumeration", true)
    // pass the configuration to the data source
    val ds1 = env.readTextFile("file:///Users/liguohua/Documents/F/code/idea/git/simple-flink/src/main/scala/code/tes'*'/ma'*'/").withParameters(parameters)
    ds1.print()
  }
}