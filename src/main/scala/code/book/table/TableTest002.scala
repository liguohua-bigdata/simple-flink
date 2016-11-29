package code.book.table
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.table._
import org.apache.flink.api.table.TableEnvironment
/**
  * Created by liguohua on 29/11/2016.
  */
object TableTest002 {
  def main(args: Array[String]): Unit = {

    case class WC(word: String, count: Int)

    val env = ExecutionEnvironment.getExecutionEnvironment
    val tEnv = TableEnvironment.getTableEnvironment(env)

    val input = env.fromElements(WC("hello", 1), WC("hello", 1), WC("ciao", 1))
    val expr = input.toTable(tEnv)
    val result = expr
      .groupBy('word)
      .select('word, 'count.sum as 'count)
      .toDataSet[WC]
  }
}
