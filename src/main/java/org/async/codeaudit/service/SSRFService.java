package org.async.codeaudit.service;

import org.async.codeaudit.common.R;

public interface SSRFService  {
    public R<String> code(String code);
}
