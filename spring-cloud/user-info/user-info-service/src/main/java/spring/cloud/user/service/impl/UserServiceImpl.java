package spring.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.cloud.blog.api.BlogServiceApi;
import spring.cloud.blog.api.pojo.BlogInfoResponse;
import spring.cloud.common.exception.BlogException;
import spring.cloud.common.pojo.Result;
import spring.cloud.common.utils.*;
import spring.cloud.user.api.pojo.UserInfoRegisterRequest;
import spring.cloud.user.api.pojo.UserInfoRequest;
import spring.cloud.user.api.pojo.UserInfoResponse;
import spring.cloud.user.api.pojo.UserLoginResponse;
import spring.cloud.user.convert.BeanConvert;
import spring.cloud.user.dataobject.UserInfo;
import spring.cloud.user.mapper.UserInfoMapper;
import spring.cloud.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    // redis 存储过期时间为两周
    private static final long EXPIRE_TIME = 2 * 7 * 24 * 60 * 60;

    private static final String USER_PREFIX = "user";
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BlogServiceApi blogServiceApi;

    @Autowired
    private Redis redis; // 缓存

    @Override
    public UserLoginResponse login(UserInfoRequest user) {
        //验证账号密码是否正确
        // UserInfo userInfo = selectUserInfoByName(user.getUserName());
        UserInfo userInfo = queryUserInfo(user.getUserName()); // 直接调用 queryUserInfo
        if (userInfo == null || userInfo.getId() == null) {
            throw new BlogException("用户不存在");
        }
//        if (!user.getPassword().equals(userInfo.getPassword())){
//            throw new BlogException("用户密码不正确");
//        }
        if (!SecurityUtil.verify(user.getPassword(), userInfo.getPassword())) {
            throw new BlogException("用户密码不正确");
        }
        //账号密码正确的逻辑
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userInfo.getId());
        claims.put("name", userInfo.getUserName());

        String jwt = JWTUtils.genJwt(claims);
        return new UserLoginResponse(userInfo.getId(), jwt);
    }

    @Override
    public UserInfoResponse getUserInfo(Integer userId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = selectUserInfoById(userId);
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public UserInfoResponse selectAuthorInfoByBlogId(Integer blogId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        //1. 根据博客ID, 获取作者ID
        Result<BlogInfoResponse> blogDetail = blogServiceApi.getBlogDetail(blogId);

        //2. 根据作者ID, 获取作者信息
        if (blogDetail == null || blogDetail.getData() == null) {
            throw new BlogException("博客不存在");
        }
        UserInfo userInfo = selectUserInfoById(blogDetail.getData().getUserId());
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public Integer register(UserInfoRegisterRequest registerRequest) {
        // 校验参数
        checkUserInfo(registerRequest);
        // 插入数据库
        UserInfo userInfo = BeanConvert.convertUserInfoByEncrypt(registerRequest);
        try {
            int result = userInfoMapper.insert(userInfo);
            if (result == 1) {
                // 存储数据到 redis
                redis.set(buildKey(userInfo.getUserName()), JsonUtils.toJsonString(userInfo), EXPIRE_TIME);
                return userInfo.getId();
            } else {
                throw new BlogException("用户注册失败");
            }
        } catch (Exception e) {
            log.error("用户注册失败：" + e);
            throw new BlogException("用户注册失败");
        }
    }

    private String buildKey(String userName) {
        return redis.buildKey(USER_PREFIX, userName);
    }

    private void checkUserInfo(UserInfoRegisterRequest registerRequest) {
        // 用户名不能重复
        UserInfo userInfo = selectUserInfoByName(registerRequest.getUserName());
        if (userInfo != null) {
            throw new BlogException("用户已存在");
        }

        // 邮箱格式，URL格式需要正确输入
        if (!RegexUtil.checkMail(registerRequest.getEmail())) {
            throw new BlogException("邮箱格式不合法");
        }
        if (!RegexUtil.checkURL(registerRequest.getGithubUrl())) {
            throw new BlogException("URl格式不合法");
        }

    }

    /**
     * 通过用户名查找数据
     */
    private UserInfo queryUserInfo(String userName) {
        // 先从 Redis 中获取数据，如果 redis 中数据不存在，再从数据库中查询数据
        String key = buildKey(userName);
        boolean exist = redis.hasKey(key);
        if (exist) {
            // redis 中获取数据
            log.info("从redis中获取数据，key: {}", key);
            String userJson = redis.get(key);
            UserInfo userInfo = JsonUtils.parseJson(userJson, UserInfo.class);
            return userInfo == null ? selectUserInfoByName(userName) : userInfo;
        } else {
            // mysql 中获取数据
            log.info("从 mysql中获取数据，userName: {}", userName);
            UserInfo userInfo = selectUserInfoByName(userName);
            // 根据延迟双删，再将 MySQL 中的数据再次放到 redis 中
            redis.set(key, JsonUtils.toJsonString(userInfo), EXPIRE_TIME);
            return userInfo;
        }
    }

    public UserInfo selectUserInfoByName(String userName) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userName).eq(UserInfo::getDeleteFlag, 0));
    }

    private UserInfo selectUserInfoById(Integer userId) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId).eq(UserInfo::getDeleteFlag, 0));
    }
}
