package org.smartrobot.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import org.smartrobot.database.model.OrderPayStatus;
import org.smartrobot.database.model.OrderPayStatusConverter;

import org.smartrobot.database.model.Order;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ORDER".
*/
public class OrderDao extends AbstractDao<Order, Long> {

    public static final String TABLENAME = "ORDER";

    /**
     * Properties of entity Order.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property CreateDate = new Property(1, java.util.Date.class, "createDate", false, "CREATE_DATE");
        public final static Property PayStatus = new Property(2, String.class, "payStatus", false, "PAY_STATUS");
    }

    private final OrderPayStatusConverter payStatusConverter = new OrderPayStatusConverter();

    public OrderDao(DaoConfig config) {
        super(config);
    }
    
    public OrderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ORDER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"CREATE_DATE\" INTEGER NOT NULL ," + // 1: createDate
                "\"PAY_STATUS\" TEXT);"); // 2: payStatus
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ORDER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Order entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getCreateDate().getTime());
 
        OrderPayStatus payStatus = entity.getPayStatus();
        if (payStatus != null) {
            stmt.bindString(3, payStatusConverter.convertToDatabaseValue(payStatus));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Order entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getCreateDate().getTime());
 
        OrderPayStatus payStatus = entity.getPayStatus();
        if (payStatus != null) {
            stmt.bindString(3, payStatusConverter.convertToDatabaseValue(payStatus));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Order readEntity(Cursor cursor, int offset) {
        Order entity = new Order( //
            cursor.getLong(offset + 0), // id
            new java.util.Date(cursor.getLong(offset + 1)), // createDate
            cursor.isNull(offset + 2) ? null : payStatusConverter.convertToEntityProperty(cursor.getString(offset + 2)) // payStatus
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Order entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setCreateDate(new java.util.Date(cursor.getLong(offset + 1)));
        entity.setPayStatus(cursor.isNull(offset + 2) ? null : payStatusConverter.convertToEntityProperty(cursor.getString(offset + 2)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Order entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Order entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Order entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
