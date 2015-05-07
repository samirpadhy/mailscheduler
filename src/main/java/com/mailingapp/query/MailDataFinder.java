package com.mailingapp.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Samir
 * @since 1.0 06/03/2015
 */
@Service
public class MailDataFinder {


    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public static final String BILLING_SUMMARY_STORE_PROCEDURE = "CALL repbillingpaymentsummary(:ptype,:pAccountCodes,:pFromDOS,:pToDOS, :pFromDOP,:pToDOP, :pFromDOE,:pToDOE)";


    public List<Map<String, Object>> getBillingPaymentSummary(String accountCode, Date fromDate, Date thruDate) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource = sqlParameterSource.addValue("ptype", "accountCode").addValue("pAccountCodes", null).addValue("pFromDOS", null).addValue("pToDOS", null);
        sqlParameterSource = sqlParameterSource.addValue("pFromDOP", fromDate).addValue("pToDOP", thruDate).addValue("pFromDOE", null).addValue("pToDOE", null);
        return namedParameterJdbcTemplate.query(BILLING_SUMMARY_STORE_PROCEDURE, sqlParameterSource, new ColumnMapRowMapper());
    }

}
