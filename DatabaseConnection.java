import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接管理类，统一创建和关闭MySQL数据库连接
 */
public class DatabaseConnection {
    // 已配置好student_manage_db数据库，无需修改（仅需确认密码正确）
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_manage_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true";
    private static final String DB_USER = "root"; // 你的MySQL用户名（默认root）
    private static final String DB_PASSWORD = ""; // 替换为你的MySQL root密码
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver"; // MySQL 8.0+驱动类

    /**
     * 核心方法：获取MySQL数据库连接
     * @return Connection 数据库连接对象（成功返回有效对象，失败返回null）
     */
    public static Connection getConnection() {
        Connection connection = null;

        try {
            // 加载MySQL JDBC驱动
            Class.forName(DRIVER_CLASS);
            // 建立并返回数据库连接
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ 数据库连接创建成功！");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ 错误：MySQL JDBC驱动加载失败！请检查驱动包是否正确引入。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ 错误：数据库连接创建失败！请检查连接参数是否正确。");
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * 工具方法：关闭数据库连接，释放资源
     * @param connection 需关闭的数据库连接对象
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("✅ 数据库连接已成功关闭！");
                }
            } catch (SQLException e) {
                System.err.println("❌ 错误：数据库连接关闭失败！");
                e.printStackTrace();
            }
        }
    }
}