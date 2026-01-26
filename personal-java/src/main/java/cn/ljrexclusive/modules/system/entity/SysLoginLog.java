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
 * 登录日志表
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_login_log")
@ApiModel(value="SysLoginLog对象", description="登录日志表")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "登录类型：PASSWORD-密码，OAUTH-OAuth2，SMS-短信")
    private String loginType;

    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "登录地点")
    private String location;

    @ApiModelProperty(value = "状态：0-失败，1-成功")
    private Boolean status;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;


}
