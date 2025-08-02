package spring.cloud.user.service;

import spring.cloud.user.api.pojo.UserInfoRegisterRequest;
import spring.cloud.user.api.pojo.UserInfoRequest;
import spring.cloud.user.api.pojo.UserInfoResponse;
import spring.cloud.user.api.pojo.UserLoginResponse;

public interface UserService {
    UserLoginResponse login(UserInfoRequest user);

    UserInfoResponse getUserInfo(Integer userId);

    UserInfoResponse selectAuthorInfoByBlogId(Integer blogId);

    Integer register(UserInfoRegisterRequest registerRequest);
}