package code.book.stream.customsinkandsource.java;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by liguohua on 10/12/2016.
 */
public class StudentSinkToMysql extends RichSinkFunction<Student> {
    private Connection connection = null;
    private PreparedStatement ps = null;

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
        String sql = "insert into Student(stuid,stuname,stuaddr,stusex)values(?,?,?,?);";
        //3.获得执行语句
        ps = connection.prepareStatement(sql);
    }


    /**
     * 二、每个元素的插入都要调用一次invoke()方法，这里主要进行插入操作
     */
    @Override
    public void invoke(Student student) throws Exception {
        try {
            //4.组装数据，执行插入操作
            ps.setInt(1, student.getStuid());
            ps.setString(2, student.getStuname());
            ps.setString(3, student.getStuaddr());
            ps.setString(4, student.getStusex());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
        //5.关闭连接和释放资源
        if (connection != null) {
            connection.close();
        }
        if (ps != null) {
            ps.close();
        }
    }

}
