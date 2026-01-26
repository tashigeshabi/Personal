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
 * 操作日志表
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_operation_log")
@ApiModel(value="SysOperationLog对象", description="操作日志表")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "操作")
    private String operation;

    @ApiModelProperty(value = "方法")
    private String method;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "耗时(毫秒)")
    private Long duration;

    @ApiModelProperty(value = "状态：0-失败，1-成功")
    private Boolean status;

    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
