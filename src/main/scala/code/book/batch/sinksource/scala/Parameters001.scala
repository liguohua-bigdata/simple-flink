package code.book.batch.sinksource.scala

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
  * Passing Parameters to Functions
  */
object Parameters001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    //1.准备工资数据
    case class Worker(name: String, salaryPerMonth: Double)
    val salary = env.fromElements(2123.5, 4345.2, 5987.3, 7991.2)

    //2.准备补助数据
    val bouns = 38.111

    //3.计算工资和补助之和
    salary.map(new SalarySumMap(bouns)).print()
    class SalarySumMap(b: Double) extends MapFunction[Double, Double] {
      override def map(s: Double): Double = {
        //工资+补助
        s + b
      }
    }
  }
}
