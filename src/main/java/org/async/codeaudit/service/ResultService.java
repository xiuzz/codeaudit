package org.async.codeaudit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.async.codeaudit.common.R;
import org.async.codeaudit.entiy.Result;

import java.util.List;

/**
 * 处理后的结果集对象
 */
public interface ResultService extends IService<Result> {
    public R<List<Result>> getByUId(String uid, int start);
}
