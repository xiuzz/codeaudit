package org.async.codeaudit.controller.audit.sql;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.service.SqlJDBCService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * sql注入的问题
 * jdbc原生方面
 * 使用普通的statement就有sql注入危险
 * 对于进阶的预编译preparedStatement，如果不安规定使用，也会存在sql注入问题
 * 还有orderBy这种没办法使用预编译手段的，如果不过滤，也会产生sql注入问题
 */
@Slf4j
@RestController
@RequestMapping("/jdbc")
public class SqlJDBCController {
  @Resource
  private SqlJDBCService jdbcService;

  @Value("${spring.datasource.url}")
  private  String dbUrl;

  @Value("${spring.datasource.username}")
  private String  dbUsername;

  @Value("${spring.datasource.password}")
  private String dbPassword;

    /**
     * jdbc原生
     *检查用户代码漏洞
     * 使用Pattern类方法Pattern.matches(regex, "XXX")时，他会匹配整个输入字段的长度是否符合要求，而不会考察字串的一部分是否满足正则表达式。
     * 为此需要使用Pattern的实例化对象方法p.matcher("XXX").find(), 只要在字串中有部分满足正则表达式即可。
     * @param file
     * @return
     */
 @PostMapping("/code")
  public R<String> code(MultipartFile file) {
     if(file.isEmpty())return  R.error("文件为空！");
     BufferedReader bufferedReader= null;
        log.info(file.toString());
          String code=null;
         try {
             bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             StringBuilder temp=new StringBuilder();
             while((code=bufferedReader.readLine())!=null){
                 temp.append(code);
             }
             code=temp.toString();
             bufferedReader.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     return jdbcService.code(code);
 }
    /**
     *  前端模拟伪代码，实际给后端发送下面的拼接sql语句
     *  JDBC模式下，采用拼接的SQL语句
     *   http:///jdbc/study/native?id=1' or '1'='1
     *   模拟漏洞小子查询自己的id，但是实际查询我们数据库里面所有用户的消息这样一种情景
     * @param id
     * @return
     */
 @GetMapping("/study/native")
  public R<String> jdbcVul(String id){
     StringBuilder result= new StringBuilder();
     try {
         Class.forName("com.mysql.cj.jdbc.Driver");

         Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

         Statement statement = connection.createStatement();
         String sql="select * from test where id='"+id+"'";
         log.info("[vul]执行SQL语句："+sql);
         ResultSet rs=statement.executeQuery(sql);

         //获取查询结果
         while(rs.next()){
             String username = rs.getString("username");
             result.append(String.format("查询结果 %s ",username));
         }
         rs.close();;
         connection.close();
     } catch (Exception e) {
        e.printStackTrace();
     }
     //把查询结果返回给前台
     return R.success(result.toString());
 }
    /**
     * 采用预编译的方法，使用?占位，而不是参数拼接
     * 给出正确的方法
     *  http:///jdbc/study/pre?id=1' or '1'='1
     *  拼接的sql语句会整体使用?
     *  能有效的防止拼接
     */
   @GetMapping("/study/pre")
   public  R<String> preVul(String id){
       StringBuilder result = new StringBuilder();
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");

           Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

           String sql="select * from test where id = ?";
           PreparedStatement preparedStatement=connection.prepareStatement(sql);
           preparedStatement.setString(1,id);//通过预编译传值
           ResultSet rs = preparedStatement.executeQuery();
           while (rs.next()){
               String username = rs.getString("username");
               result.append(String.format("查询结果 %s ",username));
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return R.success(result.toString());
   }
    /**
     * 采用预编译的方法，但没使用?占位，开发者为方便会直接采取拼接的方式构造SQL语句，此时进行预编译也无法阻止SQL注入
     * http:///jdbc/study/pre?id='1' or '1'='1'
     */
  @GetMapping("/study/preErr")
  public R<String>   preErr(String id){
      StringBuilder result = new StringBuilder();
      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
          String sql = "select * from test where id = " + id;
          PreparedStatement st = connection.prepareStatement(sql);
          log.info("[preErr] 执行SQL语句：" + st);
          ResultSet rs = st.executeQuery();
          while (rs.next()){
              String username = rs.getString("username");
              result.append(String.format("查询结果 %s ",username));
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return R.success(result.toString());
  }
}
