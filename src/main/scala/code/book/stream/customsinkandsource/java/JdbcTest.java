package code.book.stream.customsinkandsource.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 本类主要用于检测jdbc连接是否成功
 */
public class JdbcTest {
    public static void main(String[] args) throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://qingcheng11:3306/flinktest";
        String username = "root";
        String password = "qingcheng";
        Connection connection = null;
        Statement statement = null;
        try {
            //1.加载驱动
            Class.forName(driver);
            //2.创建连接
            connection = DriverManager.getConnection(url, username, password);
            //3.获得执行语句
            statement = connection.createStatement();
            //4.执行查询，获得结果集
            ResultSet resultSet = statement.executeQuery("select stuid,stuname,stuaddr,stusex from Student");
            //5.处理结果集
            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("stuid"), resultSet.getString("stuname").trim(), resultSet.getString("stuaddr").trim(), resultSet.getString("stusex").trim());
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //6.关闭连接，释放资源
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }
}
