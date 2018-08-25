package controller;

public interface Executes {
    long viaJdbcPreparedStatement();
    long viaJdbcBatch();
    long viaJdbcTemplate();
    long viaMongo();
    long viaHibernate();
}
