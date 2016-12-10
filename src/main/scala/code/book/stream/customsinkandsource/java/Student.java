package code.book.stream.customsinkandsource.java;

/**
 * 用于存储数据库中的数据，作为bean使用
 */
public class Student {
    int stuid;
    String stuname;
    String stuaddr;
    String stusex;

    public Student(int stuid, String stuname, String stuaddr, String stusex) {
        this.stuid = stuid;
        this.stuname = stuname;
        this.stuaddr = stuaddr;
        this.stusex = stusex;
    }

    public int getStuid() {
        return stuid;
    }

    public void setStuid(int stuid) {
        this.stuid = stuid;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getStuaddr() {
        return stuaddr;
    }

    public void setStuaddr(String stuaddr) {
        this.stuaddr = stuaddr;
    }

    public String getStusex() {
        return stusex;
    }

    public void setStusex(String stusex) {
        this.stusex = stusex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuid=" + stuid +
                ", stuname='" + stuname + '\'' +
                ", stuaddr='" + stuaddr + '\'' +
                ", stusex='" + stusex + '\'' +
                '}';
    }
}
