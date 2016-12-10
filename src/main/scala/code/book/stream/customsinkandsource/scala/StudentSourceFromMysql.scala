package code.book.stream.customsinkandsource.scala

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.RichSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

class StudentSourceFromMysql extends RichSourceFunction[Student] {
  private var connection: Connection = null
  private var ps: PreparedStatement = null
  /**
    * 一、open()方法中建立连接，这样不用每次invoke的时候都要建立连接和释放连接。
    */
  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://qingcheng11:3306/flinktest"
    val username = "root"
    val password = "qingcheng"
    //1.加载驱动
    Class.forName(driver)
    //2.创建连接
    connection = DriverManager.getConnection(url, username, password)
    //3.获得执行语句
    val sql = "select stuid,stuname,stuaddr,stusex from Student;"
    ps = connection.prepareStatement(sql)
  }
  /**
    * 二、DataStream调用一次run()方法用来获取数据
    */
  override def run(sourceContext: SourceContext[Student]): Unit = {
    try {
      //4.执行查询，封装数据
      val resultSet = ps.executeQuery()
      while (resultSet.next()) {
        val student = Student(resultSet.getInt("stuid"), resultSet.getString("stuname").trim, resultSet.getString("stuaddr").trim, resultSet.getString("stusex").trim)
        sourceContext.collect(student)
      }
    } catch {
      case e: Exception => println(e.getMessage)
    }
  }

  override def cancel(): Unit = {
  }

  /**
    * 三、 程序执行完毕就可以进行，关闭连接和释放资源的动作了
    */
  override def close(): Unit = {
    //5.关闭连接和释放资源
    super.close()
    if (connection != null) {
      connection.close()
    }
    if (ps != null) {
      ps.close()
    }
  }


}