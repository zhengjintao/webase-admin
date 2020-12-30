package tech.webase.admin.service;

import tech.webase.admin.exception.BusinessException;
import tech.webase.admin.model.PageWrapper;
import tech.webase.admin.model.dto.LoginDTO;
import tech.webase.admin.model.dto.ShipInfoPageDTO;
import tech.webase.admin.model.dto.UserTokenDTO;
import tech.webase.admin.model.entity.ShipInfo;
import tech.webase.admin.model.query.ShipInfoQuery;

import java.util.List;
import java.util.Set;

public interface DashboardService {

    /**
     * 创建用户
     *
     * @param user
     */
    void createUser(ShipInfo user) throws BusinessException;

    /**
     * 修改密码
     *
     * @param userId
     * @param newPassword
     */
    void changePassword(Long userId, String newPassword);

    List<ShipInfo> queryByUsername(String username);

    PageWrapper<ShipInfoPageDTO> queryUserPage(ShipInfoQuery userQuery);

    void updateNotNull(ShipInfo user);

    void deleteById(Long id);
}
