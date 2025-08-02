package spring.cloud.user.convert;

import spring.cloud.common.utils.SecurityUtil;
import spring.cloud.user.api.pojo.UserInfoRegisterRequest;
import spring.cloud.user.dataobject.UserInfo;


public class BeanConvert {
    public static UserInfo convertUserInfoByEncrypt(UserInfoRegisterRequest registerRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(registerRequest.getUserName());
        userInfo.setPassword(SecurityUtil.encrypt(registerRequest.getPassword()));
        userInfo.setGithubUrl(registerRequest.getGithubUrl());
        userInfo.setEmail(registerRequest.getEmail());

        return userInfo;
    }
}
