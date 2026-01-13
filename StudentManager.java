import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 学生管理类，实现学生信息增、查、全显，以及科目平均分计算
 */
public class StudentManager {

    // 功能1：添加学生信息到数据库（包含基本信息+科目分数）
    public void addStudent(Student student, String[] subjects, BigDecimal[] scores) {
        Connection conn = null;
        PreparedStatement pstmtStudent = null;
        PreparedStatement pstmtScore = null;
        ResultSet generatedKeys = null;

        try {
            // 1. 获取数据库连接
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                System.err.println("❌ 数据库连接为空，无法执行添加操作");
                return;
            }
            conn.setAutoCommit(false);

            // 2. 插入学生基本信息
            String sqlStudent = "INSERT INTO student (name, gender, age, student_no, class_name) VALUES (?, ?, ?, ?, ?)";
            pstmtStudent = conn.prepareStatement(sqlStudent, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtStudent.setString(1, student.getName());
            pstmtStudent.setString(2, student.getGender());
            pstmtStudent.setInt(3, student.getAge());
            pstmtStudent.setString(4, student.getStudentNo());
            pstmtStudent.setString(5, student.getClassName());
            pstmtStudent.executeUpdate();

            // 3. 获取自动生成的学生ID
            generatedKeys = pstmtStudent.getGeneratedKeys();
            int studentId = -1;
            if (generatedKeys.next()) {
                studentId = generatedKeys.getInt(1);
                student.setId(studentId);
            }

            // 4. 批量插入科目分数
            if (subjects != null && scores != null && subjects.length == scores.length) {
                String sqlScore = "INSERT INTO student_score (student_id, subject_name, score) VALUES (?, ?, ?)";
                pstmtScore = conn.prepareStatement(sqlScore);
                for (int i = 0; i < subjects.length; i++) {
                    pstmtScore.setInt(1, studentId);
                    pstmtScore.setString(2, subjects[i]);
                    pstmtScore.setBigDecimal(3, scores[i]);
                    pstmtScore.addBatch();
                }
                pstmtScore.executeBatch();
            }

            // 5. 提交事务
            conn.commit();
            System.out.println("✅ 学生信息添加成功！学生ID：" + studentId);

        } catch (SQLException e) {
            // 事务回滚
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("🔙 事务已回滚，避免部分数据残留");
                }
            } catch (SQLException ex) {
                System.err.println("❌ 事务回滚失败，具体原因：" + ex.getMessage());
                ex.printStackTrace();
            }
            // 打印错误详情
            System.err.println("\n===== 添加学生失败详情 =====");
            System.err.println("错误代码：" + e.getErrorCode());
            System.err.println("SQL状态：" + e.getSQLState());
            System.err.println("错误描述：" + e.getMessage());
            System.err.println("============================");
            System.err.println("❌ 错误：添加学生信息失败！");
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmtStudent, generatedKeys);
            closeResource(null, pstmtScore, null);
        }
    }

    // 功能2：根据ID查询学生信息
    public Student queryStudentById(int studentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Student student = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return null;

            String sql = "SELECT * FROM student WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setGender(rs.getString("gender"));
                student.setAge(rs.getInt("age"));
                student.setStudentNo(rs.getString("student_no"));
                student.setClassName(rs.getString("class_name"));

                // 打印查询结果
                System.out.println("\n===== 学生查询结果 =====");
                System.out.println("学生ID：" + student.getId());
                System.out.println("姓名：" + student.getName());
                System.out.println("性别：" + student.getGender());
                System.out.println("年龄：" + student.getAge());
                System.out.println("学号：" + student.getStudentNo());
                System.out.println("班级：" + student.getClassName());

                // 查询科目分数
                queryStudentScores(conn, studentId);
            } else {
                System.out.println("❌ 未查询到ID为" + studentId + "的学生信息！");
            }

        } catch (SQLException e) {
            System.err.println("❌ 错误：查询学生信息失败！");
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return student;
    }

    // 辅助方法：查询学生科目分数
    private void queryStudentScores(Connection conn, int studentId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT subject_name, score FROM student_score WHERE student_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            System.out.println("科目分数：");
            boolean hasScore = false;
            while (rs.next()) {
                hasScore = true;
                String subject = rs.getString("subject_name");
                BigDecimal score = rs.getBigDecimal("score");
                System.out.println("  " + subject + "：" + score);
            }
            if (!hasScore) {
                System.out.println("  暂无科目分数记录！");
            }
        } finally {
            closeResource(null, pstmt, rs);
        }
    }

    // 功能3：显示所有学生信息
    public void showAllStudents() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return;

            String sql = "SELECT * FROM student";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.println("\n===== 所有学生信息列表 =====");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");
                String studentNo = rs.getString("student_no");
                String className = rs.getString("class_name");

                System.out.println("ID：" + id + " | 姓名：" + name + " | 性别：" + gender + " | 年龄：" + age + " | 学号：" + studentNo + " | 班级：" + className);
                queryStudentScores(conn, id);
                System.out.println("------------------------");
            }

            if (!hasData) {
                System.out.println("❌ 暂无学生信息！");
            }

        } catch (SQLException e) {
            System.err.println("❌ 错误：查询所有学生信息失败！");
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
    }

    // 功能4：计算单个学生平均分
    public BigDecimal calculateStudentAvgScore(int studentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal avgScore = BigDecimal.ZERO;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return avgScore;

            String sql = "SELECT AVG(score) AS avg_score FROM student_score WHERE student_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                avgScore = rs.getBigDecimal("avg_score");
                if (avgScore == null) avgScore = BigDecimal.ZERO;
                avgScore = avgScore.setScale(2, RoundingMode.HALF_UP);
                System.out.println("\n✅ 学生ID" + studentId + "的所有科目平均分：" + avgScore);
            } else {
                System.out.println("\n❌ 学生ID" + studentId + "暂无科目分数记录！");
            }

        } catch (SQLException e) {
            System.err.println("❌ 错误：计算学生平均分失败！");
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return avgScore;
    }

    // 功能4：计算某科目所有学生平均分
    public BigDecimal calculateSubjectAvgScore(String subjectName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal avgScore = BigDecimal.ZERO;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return avgScore;

            String sql = "SELECT AVG(score) AS avg_score FROM student_score WHERE subject_name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, subjectName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                avgScore = rs.getBigDecimal("avg_score");
                if (avgScore == null) avgScore = BigDecimal.ZERO;
                avgScore = avgScore.setScale(2, RoundingMode.HALF_UP);
                System.out.println("\n✅ 科目《" + subjectName + "》的所有学生平均分：" + avgScore);
            } else {
                System.out.println("\n❌ 科目《" + subjectName + "》暂无分数记录！");
            }

        } catch (SQLException e) {
            System.err.println("❌ 错误：计算科目平均分失败！");
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return avgScore;
    }

    // 工具方法：关闭数据库资源
    private void closeResource(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}