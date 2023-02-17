package api.transaction;


import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BankService {
    @Test
    public void start() throws SQLException, ClassNotFoundException {
        transfer("lvdandan", "ergouzi", 500);
    }

    public void transfer(String addAccount, String subAccount, int money) throws SQLException, ClassNotFoundException {
        BankDao bankDao = new BankDao();
        // TODO 一个事务的基本要求，必须是同一个连接对象connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "root");
        //  一个转账方法，属于一个事务
        try {
            // TODO 事务的开启是在业务层开启的,必须是同一个链接
            // 关闭事务提交！
            connection.setAutoCommit(false);
            // 执行数据库动作
            bankDao.add(addAccount, money, connection);
            System.out.println("-----------");
            bankDao.sub(subAccount, money, connection);
            // 事务提交
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }finally {
            connection.close();
        }
    }
}
