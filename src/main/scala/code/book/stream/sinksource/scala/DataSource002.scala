package code.book.stream.sinksource.scala

//0.引用必要的元素
import org.apache.flink.streaming.api.scala._

/**
  * def socketTextStream(hostname: String, port: Int, delimiter: Char = '\n', maxRetry: Long = 0):
  * DataStream[String]
  */
object DataSource002 {
  def main(args: Array[String]): Unit = {
    //0.创建运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //1.定义text1数据流，采用默认值,行分隔符为'\n'，失败重试0次
    val text1 = env.socketTextStream("qingcheng11", 9999)
    text1.print()

    //2.定义text2数据流,行分隔符为'|'，失败重试3次
    val text2 = env.socketTextStream("qingcheng11", 9998, delimiter = '|', maxRetry = 3)
    text2.print()
    //5.触发计算
    env.execute(this.getClass.getName)
  }
}
