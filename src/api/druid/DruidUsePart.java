package api.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.VarHandle;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 连接池，省去了创建连接和销毁连接的过程，大大提高connection的性能
 */

public class DruidUsePart {
    // TODO 1.创建一个druid的连接池对象
    //  2.设置连接池参数(必须|非必须)
    //  3.获取连接(通用方法，所有连接池都一样)
    //  4.回收连接(通用方法，所有连接池都一样)
    public void testHard() throws SQLException {
        // 硬编码的方式
        // 1.连接池对象
        DruidDataSource druidDataSource = new DruidDataSource();
        // 2.设置参数(必须：注册驱动，url，user，password)
        druidDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/jdbc_test");
        druidDataSource.setName("root");
        druidDataSource.setPassword("qqljhwhg416");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // 非必须
        druidDataSource.setInitialSize(5);// 初始化连接数量
        druidDataSource.setMaxActive(10); // 最大的数量

        // 3.获取连接
        DruidPooledConnection connection = druidDataSource.getConnection();
        // 数据库curd
        // 4.回收连接，但是没有释放
        connection.close();
    }
    /*
        通过外部文件对象配置
     */
    public void testSoft() throws Exception {
        // 1.获取外部配置文件properties1
        Properties properties = new Properties();
        // src下的文件，可以使用类加载器提供的方法获取配置文件
        InputStream ips = DruidUsePart.class.getClassLoader().getResourceAsStream("api/druid/druid.properties");
        properties.load(ips);
        // 2.使用连接池的工具类的工程模式，创建连接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        // 数据库curd
        connection.close();
    }
}
