package org.async.codeaudit.entiy;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Result  implements Serializable {
    private  static final  long serialVersionUID =1L;
    private String uid;
    private String result;
    private LocalDateTime sendTime;
    private String remarks;
    private String color;
}
