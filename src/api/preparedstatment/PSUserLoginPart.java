package api.preparedstatment;

import java.sql.*;
import java.util.Scanner;

/*
 防止注入攻击，演示ps的使用流程
 */
public class PSUserLoginPart {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // 1.获取用户输入信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号：");
        String account = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();
        // 2.注册驱动(8+版本)
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 3.获取数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_test?user=root&password=root");
        // 4.preparedstatement
        // TODO 1.编写sql语句结果，不包含动态值的部分，动态值用占位符?代替 注意： ?只能替代动态值
        //      2.创建preparedStatement，传入动态值
        //      3.动态值 占位符 赋值?单独赋值即可
        //      4.发送SQL语句
        String sql = "select * from t_user where account = ? and password = ? ; ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // TODO 参数1：index 占位符的位置从左向右数 从1开始
        //      参数2：object 占位符的值，可以设置任何类型的值
        preparedStatement.setObject(1, account);
        preparedStatement.setObject(2, password);
        // 5. 发送sql语句(不需要参数，因为已经知道了sql)
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            String account1 = resultSet.getString("account");
            String password1 = resultSet.getString("password");
            String nickname = resultSet.getString("nickname");
            System.out.println(id + "--" + account + "--" + password + "--" + nickname);
        } else {
            System.out.println("登陆失败！");
        }
        // 6.关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }
}
