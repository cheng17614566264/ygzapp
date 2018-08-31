package com.cjit.vms.input.jdbcLink.service;

import java.util.List;
import java.util.Map;

public interface JdbcGetGeneralLedgerService {
	public List findGeneralLedgerUrl();

	public List findGeneralLedgerUrl(String query, Map parameters);
}
