package tech.webase.admin.model.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author tei
 */
@Table(name = "trn_shipinfo")
@Data
public class ShipInfo {

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 加密密码的盐
     */
    private String salt;
    /**
     * 拥有的角色列表
     */
    private String roleIds;

    private Integer locked;
    
    private String userId;
    
    private String shipCd;
    
    private String gshipCd;

    public String getCredentialsSalt() {
        return username + salt;
    }

}
