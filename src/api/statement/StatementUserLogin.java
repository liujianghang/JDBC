package api.statement;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author 11782
 */
public class StatementUserLogin {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // 1.获取用户输入信息

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号：");
        String account = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();
        // 2.注册驱动(8+版本)
        // 问题：会注册两次驱动
        //  1.DriverManager.registerDriver
        //  2.Driver的静态代码块（类加载的时候就会触发）
        // 如何触发类加载：
        //  1.new 关键字
        //  2.调用静态代码
        //  3.调用静态属性
        //  4.接口1.8 default默认实现
        //  5.反射
        //  6.子类触发父类
        //  7.程序的入口main方法
//        DriverManager.registerDriver(new Driver());
        // 反射的方式是比较好的注册方式 可以返回一个类对象
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 3.获取数据库连接
        //  getConnection(1,2,3)是一个重载方法
        //  允许开发者以不同的形式传入连接数据库的核心参数
        //  TODO：三个参数：
        //      String url ： jdbc:数据库管理软件名称[mysql,oracle]://ip:port/database?key=value
        //      ?key=value 可以填入一些可选的信息
        //      jdbc:mysql://127.0.0.1:3306/jdbc_test
        //      本机上的简略写法(必须是本机且端口是3306 ): jdbc:mysql:///jdbc_test
        //      String root
        //      String password
        //  TODO：两个参数：
        //      String url 同上
        //      Properties 类似map key=value 不过账号和密码都是字符串
        //  TODO：一个参数：
        //      String url 同上 账号密码传到可选信息
        //      jdbc:mysql://localhost:3306/jdbc_test?user=root&password=qqljhwhg416
        //      (8版本后全部都可以省略)其他可选信息:1.serverTimezone 2.useUnicode
        // 1.
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "qqljhwhg416");
        // 2.
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "qqljhwhg416");
        Connection connection1 = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", info);
        // 3.
        Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_test?user=root&password=qqljhwhg416");
        // 3.创建发送sql语句的statement对象
        Statement statement = connection2.createStatement();
        // 4.发送sql语句，并且获取返回值(statement这种形式麻烦)
        String sql = "select * from t_user WHERE account = '" + account + "' AND PASSWORD = '" + password + "';";
        // 上面的sql类型有问题
        //  TODO 1.要拼接字符串，麻烦
        //       2.只能拼接字符串类型，其他数据库的类型无法处理
        //       3.可能发生注入攻击（动态值充当了sql语句）
        // sql分类 DDL(容器创建，修改，删除) DML(插入，修改，删除) DQL(查询) DCL(权限控制) TPL(事务控制语句)
        //  TODO executeUpdate(sql):参数: sql 非DQL 返回: int
        //      情况1∶DML返回影响的行数，例如:删除了三条数据return 3;插入了两条return 2;修改了0条 return 0;
        //      情况2:非DML return 0 ;
        //  TODO executeQuery(sql):参数: sql DQL 返回: ResultSet 即结果封装对象
//        int i = statement.executeUpdate(sql);
        ResultSet resultSet = statement.executeQuery(sql);

        // 5.查询结果解析 resultSet
         /*
        A ResultSet object maintains a cursor pointing to its current row of data.
        Initially the cursor is positioned before the first row. The next method moves the cursor to the next row,
         and because it returns false when there are no more rows in the ResultSet object,
         it can be used in a while loop to iterate through the result set
         */
        // boolean = next() 有很多移动光标的方法
        // 逐行获取数据
        // 1.移动游标到某一行(默认在第一行之前)
        // 2.获取行内里面的列 resultSet.get类型（String colName|int cloIndex） cloIndex从1开始
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            String account1 = resultSet.getString("account");
            String password1 = resultSet.getString("password");
            String nickname = resultSet.getString("nickname");
            System.out.println(id + "--" + account + "--" + password + "--" + nickname);
        }else {
            System.out.println("登陆失败！");
        }
        // 6.关闭资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
