package tech.webase.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.webase.admin.exception.BusinessException;
import tech.webase.admin.mapper.ShipInfoMapper;
import tech.webase.admin.model.PageWrapper;
import tech.webase.admin.model.dto.LoginDTO;
import tech.webase.admin.model.dto.ShipInfoPageDTO;
import tech.webase.admin.model.dto.UserTokenDTO;
import tech.webase.admin.model.entity.ShipInfo;
import tech.webase.admin.model.enumeration.CommonResultStatus;
import tech.webase.admin.model.query.PageQuery;
import tech.webase.admin.model.query.ShipInfoQuery;
import tech.webase.admin.service.PasswordHelper;
import tech.webase.admin.service.RoleService;
import tech.webase.admin.service.DashboardService;
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
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ShipInfoMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    @Transactional
    public void createUser(ShipInfo user) {  
    	user.setLocked(0);
    	user.setRoleIds("1");
        userMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
    	ShipInfo user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(newPassword);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public List<ShipInfo> queryByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public PageWrapper<ShipInfoPageDTO> queryUserPage(ShipInfoQuery userQuery) {
        Example<ShipInfo> example = buildUserExample(userQuery);
        Page<ShipInfo> users = Page.of(userQuery.getPageNo(), userQuery.getPageSize(), true).list(() -> userMapper.selectByExample(example));
        List<ShipInfoPageDTO> list = new ArrayList<>();
        for (ShipInfo user : users) {
        	ShipInfoPageDTO userDTO = new ShipInfoPageDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setRoleIds(Arrays.asList(getRoleIds(user)));
            userDTO.setRoleNames(getRoleNames(user));
            userDTO.setLocked(user.getLocked());
            userDTO.setShipCd(user.getShipCd());
            userDTO.setGshipCd(user.getGshipCd());
            list.add(userDTO);
        }
        return new PageWrapper<>(list, users.getTotal(), users.getPageNumber(), users.getPageSize());
    }

    @Override
    public void updateNotNull(ShipInfo user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    private Example<ShipInfo> buildUserExample(ShipInfoQuery userQuery) {
        Example<ShipInfo> example = Example.of(ShipInfo.class);
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
        Criteria<ShipInfo> criteria = example.createCriteria();
        if (userQuery.getId() != null) {
            criteria.andEqualTo(ShipInfo::getId, userQuery.getId());
        }
        if (userQuery.getUsername() != null) {
            criteria.andEqualTo(ShipInfo::getUsername, userQuery.getUsername());
        }
        if (userQuery.getLocked() != null) {
            criteria.andEqualTo(ShipInfo::getLocked, userQuery.getLocked());
        }
        return example;
    }

    private List<String> getRoleNames(ShipInfo user) {
        Map<String, String> roleMap = roleService.queryRoleNames(getRoleIds(user));
        return roleMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private Long[] getRoleIds(ShipInfo user) {
        return Stream.of(user.getRoleIds().split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList()).toArray(new Long[0]);
    }

}
