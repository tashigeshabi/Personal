package cn.ljrexclusive.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色权限关联表
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_permission")
@ApiModel(value="SysRolePermission对象", description="角色权限关联表")
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "权限ID")
    private Long permissionId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
