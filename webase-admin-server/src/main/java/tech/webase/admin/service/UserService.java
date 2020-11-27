package tech.webase.admin.service;

import tech.webase.admin.exception.BusinessException;
import tech.webase.admin.model.PageWrapper;
import tech.webase.admin.model.dto.LoginDTO;
import tech.webase.admin.model.dto.UserPageDTO;
import tech.webase.admin.model.dto.UserTokenDTO;
import tech.webase.admin.model.entity.User;
import tech.webase.admin.model.query.UserQuery;

import java.util.Set;

public interface UserService {

    /**
     * 创建用户
     *
     * @param user
     */
    void createUser(User user) throws BusinessException;

    /**
     * 修改密码
     *
     * @param userId
     * @param newPassword
     */
    void changePassword(Long userId, String newPassword);

    /**
     * 根据用户名查找其角色
     *
     * @param username
     * @return
     */
    Set<String> queryRoles(String username);

    /**
     * 根据用户名查找其权限
     *
     * @param username
     * @return
     */
    Set<String> queryPermissions(String username);

    User queryByUsername(String username);

    UserTokenDTO login(LoginDTO loginDTO);

    PageWrapper<UserPageDTO> queryUserPage(UserQuery userQuery);

    void updateNotNull(User user);

    void deleteById(Long id);
}
