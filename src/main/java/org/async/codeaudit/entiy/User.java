package org.async.codeaudit.entiy;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
public class User implements Serializable {
    private  static final  long serialVersionUID =1L;
    private  Long uid;
    private String userName;
    private String password;
    private LocalDateTime createTime;
    private String socialAddress;
}
