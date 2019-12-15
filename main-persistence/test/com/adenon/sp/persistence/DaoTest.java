package com.adenon.sp.persistence;

import java.sql.SQLException;

public class DaoTest {

    public void test(IPersistenceService persistence) throws SQLException {
        IDbPool pool = persistence.getSystemPool(SystemPool.SYSTEM);
        //
        Dao dao = pool.newDao();
        dao.conn.createStatement().execute("drop table if exists sample");
        dao.conn.createStatement().execute("create table sample(id integer, name varchar(20))");
        dao.ps = dao.conn.prepareStatement("insert into sample values (?, 'hebe')");
        for (int i = 0; i < 100; i++) {
            dao.ps.setInt(1, i);
            dao.ps.addBatch();
        }
        dao.ps.executeBatch();
        dao.close();
    }

}
