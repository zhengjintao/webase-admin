package tech.webase.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.webase.admin.exception.BusinessException;
import tech.webase.admin.mapper.UserMapper;
import tech.webase.admin.model.PageWrapper;
import tech.webase.admin.model.constant.Constants;
import tech.webase.admin.model.dto.LoginDTO;
import tech.webase.admin.model.dto.RegisterDTO;
import tech.webase.admin.model.dto.RoleDTO;
import tech.webase.admin.model.dto.UserPageDTO;
import tech.webase.admin.model.dto.UserTokenDTO;
import tech.webase.admin.model.entity.Role;
import tech.webase.admin.model.entity.User;
import tech.webase.admin.model.enumeration.CommonResultStatus;
import tech.webase.admin.model.query.PageQuery;
import tech.webase.admin.model.query.UserQuery;
import tech.webase.admin.service.PasswordHelper;
import tech.webase.admin.service.RoleService;
import tech.webase.admin.service.UserService;
import tech.webase.admin.shiro.JwtUtil;
import tech.wetech.mybatis.domain.Page;
import tech.wetech.mybatis.domain.Sort;
import tech.wetech.mybatis.example.Criteria;
import tech.wetech.mybatis.example.Example;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cjbi
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    @Transactional
    public void createUser(User user) {
        User u = userMapper.selectByUsername(user.getUsername());
        if (u != null) {
            throw new BusinessException(CommonResultStatus.FAILED_USER_ALREADY_EXIST);
        }
        
        if(user.getPassword() == null) {
            //设置默认密码
            user.setPassword(Constants.DEFAULT_PASSWORD);
        }
        
        if(user.getRoleIds() == null) {
        	user.setRoleIds("5");
        }
        // 加密密码
        passwordHelper.encryptPassword(user);
        userMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public Set<String> queryRoles(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.queryRoles(getRoleIds(user));
    }

    @Override
    public Set<String> queryPermissions(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.queryPermissions(getRoleIds(user));
    }

    @Override
    public User queryByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public UserTokenDTO login(LoginDTO loginDTO) {
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(CommonResultStatus.LOGIN_ERROR, "用户不存在");
        }
        if (!passwordHelper.verifyPassword(user, loginDTO.getPassword())) {
            throw new BusinessException(CommonResultStatus.LOGIN_ERROR, "密码错误");
        }
        UserTokenDTO userInfoDTO = new UserTokenDTO();
        userInfoDTO.setUsername(user.getUsername());
        userInfoDTO.setToken(JwtUtil.sign(user.getUsername(), String.valueOf(System.currentTimeMillis())));
        return userInfoDTO;
    }
    
    @Override
    public UserTokenDTO register(RegisterDTO registerDTO) {
    	User user = userMapper.selectByUsername(registerDTO.getUsername());
        if (user != null) {
            throw new BusinessException(CommonResultStatus.LOGIN_ERROR, "用户已经存在");
        }
        
        if (!registerDTO.getPassword().equals(registerDTO.getPassword2())) {
            throw new BusinessException(CommonResultStatus.LOGIN_ERROR, "密码不一致");
        }
        
        user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        
        createUser(user);
        UserTokenDTO userInfoDTO = new UserTokenDTO();
        userInfoDTO.setUsername(user.getUsername());
        userInfoDTO.setToken(JwtUtil.sign(user.getUsername(), String.valueOf(System.currentTimeMillis())));
        return userInfoDTO;
    }

    @Override
    public PageWrapper<UserPageDTO> queryUserPage(UserQuery userQuery) {
        Example<User> example = buildUserExample(userQuery);
        Page<User> users = Page.of(userQuery.getPageNo(), userQuery.getPageSize(), true).list(() -> userMapper.selectByExample(example));
        List<UserPageDTO> list = new ArrayList<>();
        for (User user : users) {
            UserPageDTO userDTO = new UserPageDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setRoleIds(Arrays.asList(getRoleIds(user)));
            userDTO.setRoleNames(getRoleNames(user));
            userDTO.setLocked(user.getLocked());
            list.add(userDTO);
        }
        return new PageWrapper<>(list, users.getTotal(), users.getPageNumber(), users.getPageSize());
    }

    @Override
    public void updateNotNull(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    private Example<User> buildUserExample(UserQuery userQuery) {
        Example<User> example = Example.of(User.class);
        if (userQuery.getSortField() != null && userQuery.getSortOrder() != null) {
            if (userQuery.getSortOrder() == PageQuery.SortOrder.ascend) {
                example.setSort(Sort.by(userQuery.getSortField()).asc());
            }
            if (userQuery.getSortOrder() == PageQuery.SortOrder.descend) {
                example.setSort(Sort.by(userQuery.getSortField()).desc());
            }
        } else {
            example.setSort(Sort.by("id").desc());
        }
        Criteria<User> criteria = example.createCriteria();
        if (userQuery.getId() != null) {
            criteria.andEqualTo(User::getId, userQuery.getId());
        }
        if (userQuery.getUsername() != null) {
            criteria.andEqualTo(User::getUsername, userQuery.getUsername());
        }
        if (userQuery.getLocked() != null) {
            criteria.andEqualTo(User::getLocked, userQuery.getLocked());
        }
        return example;
    }

    private List<String> getRoleNames(User user) {
        Map<String, String> roleMap = roleService.queryRoleNames(getRoleIds(user));
        return roleMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private Long[] getRoleIds(User user) {
        return Stream.of(user.getRoleIds().split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList()).toArray(new Long[0]);
    }

	@Override
	public List<User> searchUserList(String userName) {
		userName = userName == null ? "" : "%" + userName + "%";
		return  userMapper.createCriteria().andLike(User::getUsername, userName).selectList();
	}

}
