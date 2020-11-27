package tech.webase.admin.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.webase.admin.model.PageWrapper;
import tech.webase.admin.model.Result;
import tech.webase.admin.model.dto.PermissionTreeDTO;
import tech.webase.admin.model.entity.Permission;
import tech.webase.admin.model.vo.PermissionTreeVO;
import tech.webase.admin.service.PermissionService;
import tech.webase.admin.util.JSONUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限
 *
 * @author cjbi
 */
@RestController
@RequestMapping("permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("no-pager")
    @RequiresPermissions("permission:view")
    public Result<PageWrapper<PermissionTreeVO>> queryPermissionTreeNoPager() {
        List<PermissionTreeDTO> permissionTreeDTOS = permissionService.queryPermissionTree();
        PageWrapper<PermissionTreeVO> objectPageWrapper = new PageWrapper<>();
        objectPageWrapper.setData(permissionTreeDTOS.stream().map(PermissionTreeVO::new).collect(Collectors.toList()));
        return Result.success(objectPageWrapper);
    }

    @GetMapping("tree")
    @RequiresPermissions("permission:view")
    public Result queryPermissionTree() {
        return Result.success(permissionService.queryPermissionTree());
    }

    @PutMapping
    @RequiresPermissions("permission:update")
    public Result updatePermission(@RequestBody Permission permission) {
        permission.setConfig(generateConfig(permission, permission.getConfig()));
        permissionService.updateNotNull(permission);
        return Result.success();
    }

    @PostMapping
    @RequiresPermissions("permission:create")
    public Result createPermission(@RequestBody Permission permission) {
        permission.setConfig(generateConfig(permission, permission.getConfig()));
        permissionService.createPermission(permission);
        return Result.success();
    }

    private String generateConfig(Permission permission, String config) {
        try {
            if (config != null && !config.isEmpty()) {
                Map<String, Object> configMap = JSONUtil.toObject(config, Map.class);
                Map<String, Object> meta = (Map<String, Object>) configMap.getOrDefault("meta", Map.class);
                meta.put("icon", permission.getIcon());
                meta.put("title", permission.getName());
                return JSONUtil.toJSONString(configMap);
            }
        } catch (Exception e) {
        }
        return config;
    }

    @DeleteMapping("{id}")
    @RequiresPermissions("permission:delete")
    public Result deletePermission(@PathVariable Long id) {
        permissionService.deleteById(id);
        return Result.success();
    }

}
