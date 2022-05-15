package org.async.codeaudit.entiy;

import lombok.Data;

import java.sql.Date;
@Data
public class Result {
    private String uid;
    private String result;
    private Date sendTime;
    private int doTime;
    private String remarks;
    private String color;
}
