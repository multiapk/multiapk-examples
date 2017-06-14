package org.smartrobot.database.model;


import org.greenrobot.greendao.converter.PropertyConverter;

public class OrderPayStatusConverter implements PropertyConverter<OrderPayStatus, String> {

    @Override
    public OrderPayStatus convertToEntityProperty(String databaseValue) {
        return OrderPayStatus.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(OrderPayStatus entityProperty) {
        return entityProperty.name();
    }
}