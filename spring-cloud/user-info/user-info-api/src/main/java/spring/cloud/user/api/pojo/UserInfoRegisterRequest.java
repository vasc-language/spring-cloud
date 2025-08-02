package spring.cloud.user.api.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 注册实体类
 */
@Data
public class UserInfoRegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能超过20")
    public String userName;

    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码长度不超过20")
    public String password;

    @Length(max = 64, message = "GitHub URL长度不能超过64")
    public String githubUrl;

    @NotBlank(message = "邮箱不能为空")
    @Length(max = 20, message = "邮箱长度不能超过20")
    public String email;
}
