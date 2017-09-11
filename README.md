# MyBatis学习笔记
>链接：http://blog.csdn.net/eson_15/article/details/51592608
-------

# 第一天

## 创建项目
创建javaee项目，导入mybatis和mysql的jar包

## 创建config文件夹
1、创建SqlMapConfig.xml文件，并配置如下：
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!-- 和Spring整合后environment配置都会被干掉 -->
    <environments default="development">
        <environment id="development">
            <!-- 使用jdbc事务管理，目前由mybatis来管理 -->
            <transactionManager type="JDBC"/>
            <!-- 数据库连接池，目前由mybatis来管理 -->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="config/sqlmap/User.xml"/>
    </mappers>
</configuration>
```
　
以上这些环境配置（<environments>标签中的内容在以后和Spring整合后，都会交给Spring来管理，现在暂时交给 mybatis来管理）中，修改成自己数据库相对应的情况即可，<mapper>标签用来配置映射文件的，这些映射文件是针对不同的pojo的，这个示例中只操作一个User对象，所以只有一个配置文件，在sqlmap目录下的User.xml，在下文中可以看到。

![](https:github.com/huangdali/mybatis/blog/master/images.png)

2、创建pojo
```java
public class User {
    private String username;
    private String birthday;
    private String sex;
    private String address;
    }
```
3、创建表
```mysql
drop table if exists user;
create table user(id int primary key not null auto_increment,
	username varchar(40),
	birthday date,
	sex char(1),
	address varchar(255)
)

insert into user(username,birthday,sex,address) values("黄大力","2017-01-01","男","贵州财经大学");
insert into user(username,birthday,sex,address) values("李忠红","2017-01-02","女","财经大学");
```
4、创建sqlmap-->User.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="test">
    <!-- 需求：通过id查询用户 -->
    <select id="findUserById" parameterType="int" resultType="com.hdl.po.User">
        select * from user where id = #{id}
    </select>
</mapper>
```

- **select**标签：用于执行数据库查询的，所有关于查询的都使用该标签。

- **id属性**：标识映射文件中的sql，将sql语句封装到mappedStatement对象中，即statement的id，下面执行数据库的时候需要用到这个id。

- **\#{}**：表示一个占位符，用来接收输入参数的。

- **\#{id}**：id标识接收输入的参数，参数名称就是id，如果输入参数是简单类型，那么#{}中的参数名可以任意，可以value或者其他名称。

- **parameterType**：指定输入参数的类型，这个要和数据库中的保持一致。

- **resultType**：指定输出结果的类型，即查询结果所映射的java对象。

## 测试

```java
public class FirstTest {
    //因为接下来的测试代码中，获取sqlSession这部分都相同，所以抽取成一个方法
    public SqlSession getSeesion() throws IOException {
        String resoure = "config/SqlMapConfig.xml";
        InputStream is = Resources.getResourceAsStream(resoure);//拿到流文件
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);//获取工厂
        return factory.openSession();//打开session
    }

    @Test
    public void findUserById() throws IOException {
        SqlSession seesion = getSeesion();
        User user = seesion.selectOne("test.findUserById", 1);
        System.out.println(user);
        seesion.close();
    }
}
```