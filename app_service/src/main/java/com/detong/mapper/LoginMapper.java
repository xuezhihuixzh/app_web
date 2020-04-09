package com.detong.mapper;

import com.detong.entity.LoginBean;
import sun.rmi.runtime.Log;

import java.util.List;
import java.util.Map;

public interface LoginMapper {
    /**
     * 按名称查找用户
     *
     * @param map
     * @return
     * @throws Exception
     */
    public LoginBean findUserByName(Map<String, Object> map) throws Exception;

    /**
     * 登录用户
     *
     * @param map
     * @return
     * @throws Exception
     */
    public LoginBean loginUser(Map<String, Object> map) throws Exception;

    /**
     * 增加用户
     *
     * @param bean
     * @throws Exception
     */
    public void addUser(LoginBean bean) throws Exception;

    /**
     * 展示用户列表
     *
     * @return
     * @throws Exception
     */
    public List<LoginBean> loadUserList() throws Exception;
}
