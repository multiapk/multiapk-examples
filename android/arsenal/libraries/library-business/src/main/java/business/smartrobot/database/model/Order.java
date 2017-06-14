package business.smartrobot.database.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;


@Entity
public class Order {

    @Id(autoincrement = true)
    public Long id;

    @NotNull
    public Date createDate = new Date();

    @Convert(converter = OrderPayStatusConverter.class, columnType = String.class)
    public OrderPayStatus payStatus = OrderPayStatus.NotPaid;

    @Generated(hash = 1105174599)
    public Order() {
    }

    @Generated(hash = 1288604228)
    public Order(Long id, @NotNull Date createDate, OrderPayStatus payStatus) {
        this.id = id;
        this.createDate = createDate;
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", payStatus=" + payStatus +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public OrderPayStatus getPayStatus() {
        return this.payStatus;
    }

    public void setPayStatus(OrderPayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }
}