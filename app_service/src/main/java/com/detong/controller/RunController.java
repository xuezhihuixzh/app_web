package com.detong.controller;

import com.detong.entity.LoginBean;
import com.detong.entity.ResultBean;
import com.detong.mapper.LoginMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

/**
 * 控制层
 */
@Controller
public class RunController {


    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 获取数据库的列表 get 方式
     */
    @RequestMapping(value = "userList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> userList() {
        out.println("数据库用户列表 ");
        Map<String, Object> map = new HashMap<String, Object>();
        ResultBean result = onUserList();
        out.println("result==>" + result);
        map.put("code", result.getCode());
        map.put("reason", result.getReason());
        map.put("success", result.isSuccess());

        return map;
    }


    /**
     * App用户登录接口
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestParam("param") String param) {
        ObjectMapper objectMapper = new ObjectMapper();//转换器
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            LoginBean loginBean = objectMapper.readValue(param, LoginBean.class);
            ResultBean result = onLogin(loginBean.getName(), loginBean.getPassword());
            map.put("code", result.getCode());
            map.put("reason", result.getReason());
            map.put("success", result.isSuccess());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return map;
    }


    /**
     * 查询用户列表
     */

    private ResultBean onUserList() {
        ResultBean resultBean = new ResultBean();
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            LoginMapper loginMapper = session.getMapper(LoginMapper.class);
            List<LoginBean> data = loginMapper.loadUserList();
            if (data.size() != 0) {
                ObjectMapper mapper = new ObjectMapper();
                resultBean.setCode("200");
                resultBean.setSuccess(true);
                //将集合转换为json
                resultBean.setRecords(mapper.writeValueAsString(data));
                resultBean.setReason("查询成功" + data.size());

            } else {
                resultBean.setCode("001");
                resultBean.setSuccess(false);
                resultBean.setReason("查询错误");

            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("查询异常==>" + e.getMessage());
            resultBean.setCode("001");
            resultBean.setSuccess(false);
            resultBean.setReason("查询异常");

        } finally {
            session.close();
        }
        return resultBean;
    }

    private ResultBean onLogin(String username, String password) {
        ResultBean resultBean = new ResultBean();
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            LoginMapper loginMapper = session.getMapper(LoginMapper.class);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", username);
            map.put("password", password);
            LoginBean userExist = loginMapper.findUserByName(map);
            if (userExist != null) {
                LoginBean data = loginMapper.loginUser(map);
                if (data != null) {
                    resultBean.setCode("200");
                    resultBean.setSuccess(true);
                    resultBean.setReason("登录成功");
                } else {
                    resultBean.setCode("001");
                    resultBean.setSuccess(false);
                    resultBean.setReason("密码错误");

                }

            } else {
                resultBean.setCode("001");
                resultBean.setSuccess(false);
                resultBean.setReason("用户不存在");

            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("登录异常" + e.getMessage());
            resultBean.setCode("001");
            resultBean.setSuccess(false);
            resultBean.setReason("登录异常");

        } finally {
            session.close();

        }
        return resultBean;
    }
}
