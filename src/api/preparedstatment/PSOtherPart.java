package api.preparedstatment;


import org.testng.annotations.Test;

import java.sql.*;

public class PSOtherPart {
    // TODO: 插入一条数据 并获取数据库自增长的主键
    //  1.创建prepareStatement时，告知返回自增长的主键
    @Test
    public void returnPrimaryKey() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "root");
        String sql = "insert into t_user(account,password,nickname) values (?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, "test3");
        preparedStatement.setObject(2, "123456312");
        preparedStatement.setObject(3, "test4");
        // DML类型
        int rows = preparedStatement.executeUpdate();
        if (rows > 0) {
            System.out.println("数据插入成功！");
            // 可以获取回显的主键 固定的：一行一列 id = 值
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            // 移动一下光标！目的是指向第一行第一列
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            System.out.println("id = " + id);
        } else {
            System.out.println("数据插入失败！");
        }
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testInsert() throws ClassNotFoundException, SQLException {
        // TODO 测试插入的效率
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "root");
        String sql = "insert into t_user(account,password,nickname) values (?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            preparedStatement.setObject(1, "test3" + i);
            preparedStatement.setObject(2, "123456312" + i);
            preparedStatement.setObject(3, "test4" + i);
            preparedStatement.executeUpdate();
        }
        long end = System.currentTimeMillis();
        System.out.println("执行的时间为:" + (end - start));
        // DML类型

        preparedStatement.close();
        connection.close();
    }
    @Test
    public void testBatchInsert() throws ClassNotFoundException, SQLException {
        // TODO 测试批量插入的效率
        Class.forName("com.mysql.cj.jdbc.Driver");
        // TODO 3.要给mysql传一个固定值 允许批量操作
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test?rewriteBatchedStatements=true", "root", "root");
        String sql = "insert into t_user(account,password,nickname) values (?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            preparedStatement.setObject(1, "test3" + i);
            preparedStatement.setObject(2, "123456312" + i);
            preparedStatement.setObject(3, "test4" + i);
            // TODO 1.不执行 追加到values的后面
            preparedStatement.addBatch();
        }
        // TODO 2.执行批量操作
        preparedStatement.executeBatch();
        long end = System.currentTimeMillis();
        System.out.println("执行的时间为:" + (end - start));
        // DML类型

        preparedStatement.close();
        connection.close();
    }
}
