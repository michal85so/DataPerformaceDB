package controller;

/**
 * Created by michael on 21.08.18.
 */
public interface Executes {
    long viaJdbcPreparedStatement();
    long viaJdbcBatch();
    long viaJdbcTemplate();
    long viaMongo();
    long viaHibernate();
}
