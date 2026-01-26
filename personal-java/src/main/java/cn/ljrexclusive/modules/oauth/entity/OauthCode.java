package cn.ljrexclusive.modules.oauth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * OAuth2授权码表
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth_code")
@ApiModel(value="OauthCode对象", description="OAuth2授权码表")
public class OauthCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "授权码")
    @TableId(value = "code")
    private String code;

    @ApiModelProperty(value = "认证信息")
    private Blob authentication;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
