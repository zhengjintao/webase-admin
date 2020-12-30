package tech.webase.admin.model.query;

import lombok.Data;

/**
 * @author cjbi
 */
@Data
public class ShipInfoQuery extends PageQuery {
    private Long id;
    private String username;
    private Integer locked;

}
