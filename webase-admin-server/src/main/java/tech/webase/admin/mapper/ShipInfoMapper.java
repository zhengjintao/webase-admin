package tech.webase.admin.mapper;

import java.util.List;

import tech.webase.admin.model.entity.ShipInfo;
import tech.wetech.mybatis.mapper.BaseMapper;

public interface ShipInfoMapper extends BaseMapper<ShipInfo> {

    /**
     * 获取单个用户
     * @param username
     * @return
     */
    default List<ShipInfo> selectByUsername(String username) {
        return this.createCriteria().andEqualTo(ShipInfo::getUsername, username).selectList();
    }

}
