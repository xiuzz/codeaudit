package org.async.codeaudit.service;

import org.async.codeaudit.common.R;

public interface SqlJDBCService {
    public R<String> code(String code);
}
