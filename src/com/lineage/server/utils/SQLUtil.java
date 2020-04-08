package com.lineage.server.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtil {

    public static SQLException close(final Connection cn) {
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (final SQLException e) {
            return e;
        }
        return null;
    }

    public static SQLException close(final Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (final SQLException e) {
            return e;
        }
        return null;
    }

    public static SQLException close(final ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (final SQLException e) {
            return e;
        }
        return null;
    }

    public static void close(final ResultSet rs, final Statement ps,
            final Connection cn) {
        close(rs);
        close(ps);
        close(cn);
    }
}
