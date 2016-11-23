package main.scala
import org.apache.flink.api.scala._

/**
  * Created by liguohua on 21/11/2016.
  */
object Test {
  def main(args: Array[String]): Unit = {
    val input: DataSet[(Int, String, Double)] = benv.fromElements((18,"zhangasn",174.5),(19,"lisi",174.5),(18,"wagnwu",194.5))
    val output = input.groupBy(1).aggregate(SUM, 0)
    output.print

  }
}
