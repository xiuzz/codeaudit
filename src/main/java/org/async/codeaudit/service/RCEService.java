package org.async.codeaudit.service;

import org.async.codeaudit.common.R;

public interface RCEService {
    public R<String>  pbCode(String code);
    public R<String>  runtimeCode(String code);
}
