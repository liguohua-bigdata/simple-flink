package code.book.stream.customsinkandsource.jdbc.scala

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction

class StudentSinkToMysql extends RichSinkFunction[Student] {
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
    val sql = "insert into Student(stuid,stuname,stuaddr,stusex)values(?,?,?,?);"
    //3.获得执行语句
    ps = connection.prepareStatement(sql)
  }

  /**
    * 二、每个元素的插入都要调用一次invoke()方法，这里主要进行插入操作
    */
  override def invoke(stu: Student): Unit = {
    try {
      //4.组装数据，执行插入操作
      ps.setInt(1, stu.stuid)
      ps.setString(2, stu.stuname)
      ps.setString(3, stu.stuaddr)
      ps.setString(4, stu.stusex)
      ps.executeUpdate()
    } catch {
      case e: Exception => println(e.getMessage)
    }
  }

  /**
    * 三、 程序执行完毕就可以进行，关闭连接和释放资源的动作了
    */
  override def close(): Unit = {
    super.close()
    //5.关闭连接和释放资源
    if (connection != null) {
      connection.close()
    }
    if (ps != null) {
      ps.close()
    }
  }
}


