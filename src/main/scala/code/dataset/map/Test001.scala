package main.scala.code.dataset.map

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.streaming.api.scala._
/**
  * Created by liguohua on 23/11/2016.
  */
object Test001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //1.创建一个DataSet[(Int, Int)]
    val intPairs = env.fromElements((18,4),(19,5),(23,6),(38,3))

    //2.键值对的key+value之和生成新的dataset
    val intSums = intPairs.map { pair => pair._1 + pair._2 }

    //3.显示结果
    intSums.print()
  }

}
