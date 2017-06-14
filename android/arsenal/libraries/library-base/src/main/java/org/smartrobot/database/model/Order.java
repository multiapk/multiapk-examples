package org.smartrobot.database.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;


@Entity
public class Order {

    @Id(autoincrement = true)
    public long id;

    @NotNull
    public Date createDate;

    @Convert(converter = OrderPayStatusConverter.class, columnType = String.class)
    public OrderPayStatus payStatus;

    @Generated(hash = 1105174599)
    public Order() {
    }

    @Generated(hash = 422555806)
    public Order(long id, @NotNull Date createDate, OrderPayStatus payStatus) {
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

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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
}