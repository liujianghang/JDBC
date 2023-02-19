package api.tranctionV2;


import api.utils.JdbcUtilsV2;
import org.junit.Test;


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
        Connection connection = JdbcUtilsV2.getConnection();
        //  一个转账方法，属于一个事务
        try {
            // TODO 事务的开启是在业务层开启的,必须是同一个链接
            // 关闭事务提交！
            connection.setAutoCommit(false);
            // 执行数据库动作
            bankDao.add(addAccount, money);
            System.out.println("-----------");
            bankDao.sub(subAccount, money);
            // 事务提交
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }finally {
            JdbcUtilsV2.freeConnection();
        }
    }
}
