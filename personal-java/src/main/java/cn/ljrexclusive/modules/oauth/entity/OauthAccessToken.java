package cn.ljrexclusive.modules.oauth.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.sql.Blob;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 访问令牌表
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth_access_token")
@ApiModel(value="OauthAccessToken对象", description="访问令牌表")
public class OauthAccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "令牌ID")
    private String tokenId;

    @ApiModelProperty(value = "令牌")
    private Blob token;

    @ApiModelProperty(value = "认证ID")
    private String authenticationId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @ApiModelProperty(value = "认证信息")
    private Blob authentication;

    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
