package com.bookshopweb.utils;

import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.SQLException;

public class LoggingSqlLogger implements SqlLogger {
    @Override
    public void logBeforeExecution(StatementContext context) {
        String sql = context.getRawSql();
        System.out.println("Executing SQL: " + sql);
    }

    @Override
    public void logAfterExecution(StatementContext context) {
        // Không làm gì sau khi thực thi câu truy vấn
    }

    @Override
    public void logException(StatementContext context, SQLException ex) {
        // Xử lý ngoại lệ trong quá trình thực thi câu truy vấn
    }
}
