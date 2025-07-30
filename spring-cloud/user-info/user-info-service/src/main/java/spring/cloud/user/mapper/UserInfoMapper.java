package spring.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import spring.cloud.user.dataobject.UserInfo;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}