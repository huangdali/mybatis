package com.hdl.test;

import com.hdl.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 通过用户id查询用户信息
     *
     * @throws IOException
     */
    @Test
    public void findUserById() throws IOException {
        SqlSession seesion = getSeesion();
        User user = seesion.selectOne("test.findUserById", 15);
        System.out.println(user);
        seesion.close();
    }

    /**
     * 查询所有用户
     *
     * @throws IOException
     */
    @Test
    public void findUser() throws IOException {
        SqlSession seesion = getSeesion();
        List<User> users = seesion.selectList("test.findUser");
        for (User user : users) {
            System.out.println(user);
        }
        seesion.close();
    }

    /**
     * 添加用户
     *
     * @throws IOException
     */
    @Test
    public void addUser() throws IOException {
        SqlSession seesion = getSeesion();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setAddress("中国大陆");
            user.setUsername(i + "大" + (i + 1) + "力" + (i + 2) + "哥" + (i + 3));
            user.setBirthday("2017-09-" + i);
            user.setSex("男");
            seesion.insert("test.addUser", user);
        }
        seesion.commit();//除了查询操作以外的操作都需要commit
        seesion.close();//即使关闭资源
    }

    /**
     * 更新用户
     */
    @Test
    public void updateUserByid() {
        SqlSession sqlSession = null;
        try {
            User user = new User();
            user.setId(51);
            user.setAddress("贵州省贞丰县");
            user.setUsername("大力哥");
            user.setBirthday("2017-09-24");
            user.setSex("男");
            sqlSession = getSeesion();
            sqlSession.update("test.updateUserById", user);
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    /**
     * 删除用户
     */
    @Test
    public void deleteUserById() {
        try {
            SqlSession seesion = getSeesion();
            seesion.delete("test.deleteUserById", 50);
            seesion.commit();
            seesion.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
