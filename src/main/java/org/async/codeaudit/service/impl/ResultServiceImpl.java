package org.async.codeaudit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.async.codeaudit.common.R;
import org.async.codeaudit.entiy.Result;
import org.async.codeaudit.mapper.ResultMapper;
import org.async.codeaudit.service.ResultService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 处理后的结果集对象
 */
@Service
public class ResultServiceImpl extends ServiceImpl<ResultMapper, Result> implements ResultService {
    @Resource
    private ResultMapper resultMapper;
    public R<List<Result>> getByUId(String uid,int start){
        List<Result> resultResultByUid = resultMapper.getResultResultByUid(uid, start, start + 10);
        return R.success(resultResultByUid);
    }
}
