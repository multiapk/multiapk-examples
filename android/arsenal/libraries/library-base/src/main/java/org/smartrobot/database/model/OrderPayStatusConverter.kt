package org.smartrobot.database.model

import org.greenrobot.greendao.converter.PropertyConverter

class OrderPayStatusConverter : PropertyConverter<OrderPayStatus, String> {
    override fun convertToEntityProperty(databaseValue: String): OrderPayStatus {
        return OrderPayStatus.valueOf(databaseValue)
    }

    override fun convertToDatabaseValue(entityProperty: OrderPayStatus): String {
        return entityProperty.name
    }
}