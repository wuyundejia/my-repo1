/**
 * 学生实体类，封装学生基本信息，提供set/get方法访问和修改属性
 */
public class Student {
    // 学生信息成员属性（私有化，符合封装特性）
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
    private String studentNo;
    private String className;

    // 无参构造方法
    public Student() {
    }

    // 有参构造方法
    public Student(Integer id, String name, String gender, Integer age, String studentNo, String className) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.studentNo = studentNo;
        this.className = className;
    }

    // setter方法（赋值）
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    // getter方法（取值）
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public String getClassName() {
        return className;
    }
}