package org.smartrobot.database.model

import org.greenrobot.greendao.annotation.*
import java.util.*

@Entity
class Order {

    @Id(autoincrement = true)
    var id: Long? = null

    @NotNull
    var createDate: Date? = null

    @Convert(converter = OrderPayStatusConverter::class, columnType = String::class)
    var payStatus: OrderPayStatus? = null

    @Generated(hash = 1105174599)
    constructor() {
        createDate = Date()
        payStatus = OrderPayStatus.NotPaid
    }

    @Generated(hash = 1531523551)
    constructor(id: Long?, @NotNull createDate: java.util.Date,
                payStatus: OrderPayStatus) {
        this.id = id
        this.createDate = createDate
        this.payStatus = payStatus
    }

    override fun toString(): String {
        return "Order[" +
                "id=" + id +
                ", createDate=" + createDate +
                ", payStatus=" + payStatus +
                ']' + '\n'
    }
}