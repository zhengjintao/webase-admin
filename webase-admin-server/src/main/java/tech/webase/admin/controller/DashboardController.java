package tech.webase.admin.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import tech.webase.admin.model.PageWrapper;
import tech.webase.admin.model.Result;
import tech.webase.admin.model.dto.ShipInfoPageDTO;
import tech.webase.admin.model.entity.ShipInfo;
import tech.webase.admin.model.enumeration.CommonResultStatus;
import tech.webase.admin.model.query.ShipInfoQuery;
import tech.webase.admin.model.vo.UserBatchDeleteVO;
import tech.webase.admin.service.DashboardService;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author cjbi
 */
@RequestMapping("dashboard")
@RestController
@Validated
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    @RequiresPermissions("workspace:view")
    public Result<PageWrapper<ShipInfoPageDTO>> queryUserList(ShipInfoQuery userQuery) {
        return Result.success(dashboardService.queryUserPage(userQuery));
    }
    
    @GetMapping("list")
    @RequiresPermissions("workspace:view")
    public Result<List<ShipInfo>> queryListByUserName(ShipInfoQuery userQuery) {
        return Result.success(dashboardService.queryByUsername(userQuery.getUsername()));
    }

    @PostMapping
    @RequiresPermissions("user:create")
    public Result create(@RequestBody ShipInfo user) {
    	dashboardService.createUser(user);
        return Result.success();
    }

    @PutMapping
    @RequiresPermissions("user:update")
    public Result update(@RequestBody ShipInfo user) {
    	if (user.getGshipCd() != null) {
    		user.setLocked(3);
    	}
    	dashboardService.updateNotNull(user);
        return Result.success();
    }

    @DeleteMapping
    @RequiresPermissions("user:delete")
    public Result deleteBatchByIds(@RequestBody @Validated UserBatchDeleteVO userDeleteVO) {
        Long[] ids = userDeleteVO.getIds();
        Arrays.stream(ids).forEach(dashboardService::deleteById);
        return Result.success();
    }

    @RequiresPermissions("user:update")
    @PutMapping("{id}/lock")
    public Result lockUser(@PathVariable("id") Long id, @RequestParam("locked") @NotNull Integer locked) {
        ShipInfo user = new ShipInfo();
        user.setId(id);
        user.setLocked(locked);
        dashboardService.updateNotNull(user);
        return Result.success();
    }

    @RequiresPermissions("user:update")
    @PostMapping("{id}/change/password")
    public Result changePassword(@PathVariable("id") Long id,@NotNull String newPassword) {
    	dashboardService.changePassword(id, newPassword);
        return Result.success();
    }

}
