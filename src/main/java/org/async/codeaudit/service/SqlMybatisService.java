package org.async.codeaudit.service;

import org.async.codeaudit.common.R;

public interface SqlMybatisService {
    public R<String> code(String code);
}
