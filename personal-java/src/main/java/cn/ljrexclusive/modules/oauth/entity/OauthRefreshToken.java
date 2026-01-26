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
 * 刷新令牌表
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth_refresh_token")
@ApiModel(value="OauthRefreshToken对象", description="刷新令牌表")
public class OauthRefreshToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "令牌ID")
    private String tokenId;

    @ApiModelProperty(value = "令牌")
    private Blob token;

    @ApiModelProperty(value = "认证信息")
    private Blob authentication;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
