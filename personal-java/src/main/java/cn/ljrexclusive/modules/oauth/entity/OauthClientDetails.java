package cn.ljrexclusive.modules.oauth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * OAuth2客户端表
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth_client_details")
@ApiModel(value="OauthClientDetails对象", description="OAuth2客户端表")
public class OauthClientDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端ID")
    @TableId(value = "client_id")
    private String clientId;

    @ApiModelProperty(value = "资源ID集合")
    private String resourceIds;

    @ApiModelProperty(value = "客户端密钥")
    private String clientSecret;

    @ApiModelProperty(value = "授权范围")
    private String scope;

    @ApiModelProperty(value = "授权类型")
    private String authorizedGrantTypes;

    @ApiModelProperty(value = "重定向URI")
    private String webServerRedirectUri;

    @ApiModelProperty(value = "权限")
    private String authorities;

    @ApiModelProperty(value = "访问令牌有效期(秒)")
    private Integer accessTokenValidity;

    @ApiModelProperty(value = "刷新令牌有效期(秒)")
    private Integer refreshTokenValidity;

    @ApiModelProperty(value = "附加信息")
    private String additionalInformation;

    @ApiModelProperty(value = "自动批准")
    private String autoapprove;

    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
