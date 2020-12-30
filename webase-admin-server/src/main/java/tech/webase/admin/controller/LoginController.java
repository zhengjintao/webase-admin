package tech.webase.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.webase.admin.model.Result;
import tech.webase.admin.model.dto.LoginDTO;
import tech.webase.admin.model.dto.RegisterDTO;
import tech.webase.admin.model.dto.UserTokenDTO;
import tech.webase.admin.service.UserService;

/**
 * @author cjbi
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("auth/register")
    public Result<UserTokenDTO> register(@Validated @RequestBody RegisterDTO registerDto) {
        UserTokenDTO  userInfoDTO = userService.register(registerDto);
        return Result.success(userInfoDTO);
    }
    
    @PostMapping("auth/login")
    public Result<UserTokenDTO> login(@Validated @RequestBody LoginDTO loginDTO) {
        UserTokenDTO  userInfoDTO = userService.login(loginDTO);
        return Result.success(userInfoDTO);
    }

    @PostMapping("auth/logout")
    public Result logout() {
        //清除缓存
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            SecurityUtils.getSecurityManager().logout(subject);
        }
        return Result.success();
    }

}
