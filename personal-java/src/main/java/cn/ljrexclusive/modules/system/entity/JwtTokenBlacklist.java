package cn.ljrexclusive.modules.system.entity;

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
 * JWT令牌黑名单表
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("jwt_token_blacklist")
@ApiModel(value="JwtTokenBlacklist对象", description="JWT令牌黑名单表")
public class JwtTokenBlacklist implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "JWT令牌")
    private String token;

    @ApiModelProperty(value = "令牌哈希（用于快速查找）")
    private String tokenHash;

    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expirationTime;

    @ApiModelProperty(value = "注销类型：1-用户主动注销，2-管理员强制下线，3-令牌刷新")
    private Boolean logoutType;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
