package org.async.codeaudit.entiy;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Result  implements Serializable {
    private  static final  long serialVersionUID =1L;
    private Long id;
    private Long uid;
    private String result;//data
    private LocalDateTime sendTime;
    private String remarks;//msg
    private String color;
    private String type;
}
