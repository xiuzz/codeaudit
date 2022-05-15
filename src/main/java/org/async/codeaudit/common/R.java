package org.async.codeaudit.common;

import lombok.Data;

/**
 * 结果集对象
 *  通用返回结果，服务器响应的数据最终都会封装成此对象
 */
@Data
public class R<T>{
    private  Integer code;//编码：1成功，0或其他数字失败
    private  T data;//数据
    private  String msg;//错误信息
    /**
     * green 完全ok

     * blue 存在一定的风险

     * yellow 存在较大风险

     * red 绝对的不建议

     * grey 无法判断
     */
    private  String color;//颜色信息

    /**
     * 成功响应，用来解决用户认证的信息，比如登录响应，修改密码响应,学习区加载响应等等
     * @param object
     * @param <T>
     * @return
     */
    public static <T> R<T>  success(T object){
        R<T> r=new R<>();
        r.data=object;
        r.code=1;
        return  r;
    }

    /**
     * 审计结果处理方法
     * @param data 对应解决方法等等
     * @param msg  对应错误信息
     * @param color 对应颜色信息
     * @param <T>
     * @return
     */
    public static <T> R<T>  result(T data,String msg,String color){
        R<T> r=new R<>();
        r.data=data;
        r.code=1;
        r.color=color;
        r.msg=msg;
        return  r;
    }

    /**
     * 错误响应
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> R<T>  error(String msg){
        R<T> r=new R<>();
        r.msg=msg;
        r.code=0;
        return r;
    }
}
