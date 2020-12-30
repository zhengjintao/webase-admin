package tech.webase.admin.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author cjbi
 */
@Data
public class ShipInfoPageDTO {
    private Long id; //编号
    private String username; //用户名
    private List<Long> roleIds; //拥有的角色列表
    private List<String> roleNames;//
    private Integer locked;
    private String shipCd;
    private String gshipCd;
}
