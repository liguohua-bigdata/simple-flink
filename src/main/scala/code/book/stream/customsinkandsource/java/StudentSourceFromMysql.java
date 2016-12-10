package code.book.stream.customsinkandsource.java;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class StudentSourceFromMysql extends RichSourceFunction<Student> {
    PreparedStatement ps;
    private Connection connection;

    /**
     * 一、open()方法中建立连接，这样不用每次invoke的时候都要建立连接和释放连接。
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://qingcheng11:3306/flinktest";
        String username = "root";
        String password = "qingcheng";
        //1.加载驱动
        Class.forName(driver);
        //2.创建连接
        connection = DriverManager.getConnection(url, username, password);
        //3.获得执行语句
        String sql = "select stuid,stuname,stuaddr,stusex from Student;";
        ps = connection.prepareStatement(sql);
    }

    /**
     * 二、DataStream调用一次run()方法用来获取数据
     */
    @Override
    public void run(SourceContext<Student> sourceContext) throws Exception {
        try {
            //4.执行查询，封装数据
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("stuid"), resultSet.getString("stuname").trim(), resultSet.getString("stuaddr").trim(), resultSet.getString("stusex").trim());
                sourceContext.collect(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {

    }

    /**
     * 三、 程序执行完毕就可以进行，关闭连接和释放资源的动作了
     */
    @Override
    public void close() throws Exception {
        //5.关闭连接和释放资源
        super.close();
        if (connection != null) {
            connection.close();
        }
        if (ps != null) {
            ps.close();
        }
    }

}
