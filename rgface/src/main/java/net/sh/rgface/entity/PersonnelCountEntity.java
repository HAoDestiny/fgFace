package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/6/14.
 */
@Entity
@Table(name = "personnel_count", schema = "rgface", catalog = "")
public class PersonnelCountEntity {
    private int id;
    private int normalCount;
    private int goOutCount;
    private int leaveCount;
    private int otherCount;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "normal_count")
    public int getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(int normalCount) {
        this.normalCount = normalCount;
    }

    @Basic
    @Column(name = "go_out_count")
    public int getGoOutCount() {
        return goOutCount;
    }

    public void setGoOutCount(int goOutCount) {
        this.goOutCount = goOutCount;
    }

    @Basic
    @Column(name = "leave_count")
    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    @Basic
    @Column(name = "other_count")
    public int getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(int otherCount) {
        this.otherCount = otherCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonnelCountEntity that = (PersonnelCountEntity) o;
        return id == that.id &&
                normalCount == that.normalCount &&
                goOutCount == that.goOutCount &&
                leaveCount == that.leaveCount &&
                otherCount == that.otherCount;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, normalCount, goOutCount, leaveCount, otherCount);
    }
}
