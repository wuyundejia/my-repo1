import java.math.BigDecimal;
import java.util.Scanner;

/**
 * 学生管理系统主类（命令行交互版）
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static StudentManager studentManager = new StudentManager();

    public static void main(String[] args) {
        // 欢迎界面
        showWelcomePage();

        // 核心交互循环
        while (true) {
            showFunctionMenu();
            String command = scanner.next().trim();

            switch (command) {
                case "1":
                    addStudentByConsole();
                    break;
                case "2":
                    queryStudentByIdByConsole();
                    break;
                case "3":
                    studentManager.showAllStudents();
                    break;
                case "4":
                    calculateAvgScoreByConsole();
                    break;
                case "0":
                    exitSystem();
                    return;
                default:
                    System.err.println("❌ 输入无效！请输入菜单中的有效数字（0-4）");
                    break;
            }

            System.out.println("\n----------------------------------------");
            System.out.println("请继续操作（或查看菜单选择功能）");
        }
    }

    // 显示欢迎界面
    private static void showWelcomePage() {
        System.out.println("========================================");
        System.out.println("          学生管理系统（命令行版）        ");
        System.out.println("          支持学生信息增/查/统计         ");
        System.out.println("========================================");
    }

    // 显示功能菜单
    private static void showFunctionMenu() {
        System.out.println("\n===== 功能菜单 =====");
        System.out.println("1. 添加学生信息");
        System.out.println("2. 根据ID查询学生");
        System.out.println("3. 显示所有学生信息");
        System.out.println("4. 计算平均分");
        System.out.println("0. 退出系统");
        System.out.println("====================");
        System.out.print("请输入您要执行的功能编号：");
    }

    // 控制台添加学生
    private static void addStudentByConsole() {
        System.out.println("\n===== 开始添加学生信息 =====");
        Student student = new Student();
        scanner.nextLine();

        System.out.print("请输入学生姓名：");
        student.setName(scanner.nextLine().trim());

        System.out.print("请输入学生性别：");
        student.setGender(scanner.nextLine().trim());

        System.out.print("请输入学生年龄：");
        while (!scanner.hasNextInt()) {
            System.err.println("❌ 年龄必须是整数！请重新输入：");
            scanner.next();
        }
        student.setAge(scanner.nextInt());
        scanner.nextLine();

        System.out.print("请输入学生学号（唯一，如2024003）：");
        student.setStudentNo(scanner.nextLine().trim());

        System.out.print("请输入学生班级：");
        student.setClassName(scanner.nextLine().trim());

        System.out.print("请输入要添加的科目数量：");
        while (!scanner.hasNextInt() || scanner.nextInt() <= 0) {
            System.err.println("❌ 科目数量必须是正整数！请重新输入：");
            scanner.next();
        }
        int subjectCount = scanner.nextInt();
        scanner.nextLine();

        String[] subjects = new String[subjectCount];
        BigDecimal[] scores = new BigDecimal[subjectCount];

        for (int i = 0; i < subjectCount; i++) {
            System.out.print("请输入第" + (i + 1) + "个科目名称：");
            subjects[i] = scanner.nextLine().trim();

            System.out.print("请输入" + subjects[i] + "的分数（如89.5）：");
            while (!scanner.hasNextBigDecimal()) {
                System.err.println("❌ 分数必须是数字（可保留小数）！请重新输入：");
                scanner.next();
            }
            scores[i] = scanner.nextBigDecimal();
            scanner.nextLine();
        }

        studentManager.addStudent(student, subjects, scores);
    }

    // 控制台查询学生
    private static void queryStudentByIdByConsole() {
        System.out.println("\n===== 开始查询学生信息 =====");
        System.out.print("请输入要查询的学生ID：");

        while (!scanner.hasNextInt()) {
            System.err.println("❌ 学生ID必须是整数！请重新输入：");
            scanner.next();
        }
        int studentId = scanner.nextInt();
        studentManager.queryStudentById(studentId);
    }

    // 控制台计算平均分
    private static void calculateAvgScoreByConsole() {
        System.out.println("\n===== 平均分计算菜单 =====");
        System.out.println("1. 计算单个学生的所有科目平均分");
        System.out.println("2. 计算所有学生某一科目的平均分");
        System.out.print("请输入子功能编号：");

        String subCommand = scanner.next().trim();

        switch (subCommand) {
            case "1":
                System.out.print("请输入学生ID：");
                while (!scanner.hasNextInt()) {
                    System.err.println("❌ 学生ID必须是整数！请重新输入：");
                    scanner.next();
                }
                studentManager.calculateStudentAvgScore(scanner.nextInt());
                break;
            case "2":
                scanner.nextLine();
                System.out.print("请输入科目名称：");
                studentManager.calculateSubjectAvgScore(scanner.nextLine().trim());
                break;
            default:
                System.err.println("❌ 输入无效！请输入子菜单中的有效数字（1-2）");
                break;
        }
    }

    // 退出系统
    private static void exitSystem() {
        scanner.close();
        System.out.println("\n========================================");
        System.out.println("          感谢使用学生管理系统          ");
        System.out.println("                下次再见！              ");
        System.out.println("========================================");
    }
}