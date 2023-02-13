package api.statement;

// 8+版本驱动

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

/**
 * @author 11782
 */
public class StatementQueryPart {

    /*
        DriverManager
        Connection
        Statement 2
        ResultSet
     */

    public static void main(String[] args) throws SQLException {
        // 1.注册驱动(静态方法)
        DriverManager.registerDriver(new Driver());
        // 2.获取连接
        // TODO java程序和数据库创建连接 （数据库ip 端口号3306 账号root 密码 连接数据库的名称）
        //  url: jdbc:数据库厂商名：//IP地址：port/数据库名
        //  jdbc:mysql://127.0.0.1:3306/jdbc_test
        //  user
        //  password
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "qqljhwhg416");
        // 3.创建statement
        Statement statement = connection.createStatement();
        // 4.发送sql语句，并且获取返回值
        String sql = "select * from t_user;";
        ResultSet resultSet = statement.executeQuery(sql);
        // 5.进行结果解析(一行一行的取)
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String account = resultSet.getString("account");
            String password = resultSet.getString("password");
            String nickname = resultSet.getString("nickname");
            System.out.println(id + "--" + account + "--" + password + "--" + nickname);
        }
        // 6.关闭资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
