package tech.webase.admin.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import tech.webase.admin.model.PageWrapper;
import tech.webase.admin.model.Result;
import tech.webase.admin.model.dto.RoleDTO;
import tech.webase.admin.model.dto.UserPageDTO;
import tech.webase.admin.model.entity.User;
import tech.webase.admin.model.enumeration.CommonResultStatus;
import tech.webase.admin.model.query.UserQuery;
import tech.webase.admin.model.vo.UserBatchDeleteVO;
import tech.webase.admin.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author cjbi
 */
@RequestMapping("user")
@RestController
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @RequiresPermissions("user:view")
    public Result<PageWrapper<UserPageDTO>> queryUserList(UserQuery userQuery) {
        return Result.success(userService.queryUserPage(userQuery));
    }
    
    @GetMapping("/search")
    @RequiresPermissions("user:view")
    public Result<List<User>> queryRoleList(String userName) {
        return Result.success(userService.searchUserList(userName));
    }

    @PostMapping
    @RequiresPermissions("user:create")
    public Result create(@RequestBody User user) {
        userService.createUser(user);
        return Result.success();
    }

    @PutMapping
    @RequiresPermissions("user:update")
    public Result update(@RequestBody User user) {
        userService.updateNotNull(user);
        return Result.success();
    }

    @DeleteMapping
    @RequiresPermissions("user:delete")
    public Result deleteBatchByIds(@RequestBody @Validated UserBatchDeleteVO userDeleteVO) {
        Long[] ids = userDeleteVO.getIds();
        if (isSelf(ids)) {
            return Result.failure(CommonResultStatus.FAILED_DEL_OWN);
        }
        Arrays.stream(ids).forEach(userService::deleteById);
        return Result.success();
    }

    private boolean isSelf(Long[] ids) {
        // 当前用户
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryByUsername(username);
        return Arrays.stream(ids).anyMatch(id -> id.equals(user.getId()));
    }

    @RequiresPermissions("user:update")
    @PutMapping("{id}/lock")
    public Result lockUser(@PathVariable("id") Long id, @RequestParam("locked") @NotNull Integer locked) {
        if (isSelf(new Long[]{id})) {
            return Result.failure(CommonResultStatus.FAILED_LOCK_OWN);
        }
        User user = new User();
        user.setId(id);
        user.setLocked(locked);
        userService.updateNotNull(user);
        return Result.success();
    }

    @RequiresPermissions("user:update")
    @PostMapping("{id}/change/password")
    public Result changePassword(@PathVariable("id") Long id,@NotNull String newPassword) {
        userService.changePassword(id, newPassword);
        return Result.success();
    }

}
