package com.hdl.test;

import com.hdl.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by HDL on 2017/9/11.
 */
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
