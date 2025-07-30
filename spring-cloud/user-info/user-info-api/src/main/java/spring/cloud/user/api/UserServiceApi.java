package spring.cloud.user.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.cloud.common.pojo.Result;
import spring.cloud.user.api.pojo.UserInfoRequest;
import spring.cloud.user.api.pojo.UserInfoResponse;
import spring.cloud.user.api.pojo.UserLoginResponse;

/**
 * 远程调用 user-info-service
 */
@FeignClient(value = "user-service", path = "/user")
public interface UserServiceApi {
    @RequestMapping("/login")
    Result<UserLoginResponse> login(@RequestBody UserInfoRequest user);

    @RequestMapping("/getUserInfo")
    Result<UserInfoResponse> getUserInfo(@RequestParam("userId") Integer userId);

    @RequestMapping("/getAuthorInfo")
    Result<UserInfoResponse> getAuthorInfo(@RequestParam("blogId") Integer blogId);

    // @RequestMapping("/register")
    // Result<Integer> register(@RequestBody UserInfoRegisterRequest registerRequest);
}
