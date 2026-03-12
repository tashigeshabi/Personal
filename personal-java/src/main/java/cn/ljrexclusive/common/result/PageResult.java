package cn.ljrexclusive.common.result;

import cn.ljrexclusive.common.enums.ResultCode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 分页返回结果（字符类型状态码）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<List<T>> {
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 每页显示条数
     */
    private Long size;

    /**
     * 当前页
     */
    private Long current;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    public PageResult() {
        super(ResultCode.SUCCESS);
    }

    public PageResult(IPage<T> page) {
        super(ResultCode.SUCCESS);
        this.setData(page.getRecords());
        this.setTotal(page.getTotal());
        this.setSize(page.getSize());
        this.setCurrent(page.getCurrent());
        this.setPages(page.getPages());
        this.setHasPrevious(page.getCurrent() > 1);
        this.setHasNext(page.getCurrent() < page.getPages());
    }

    public PageResult(List<T> data, Long total, Long size, Long current) {
        super(ResultCode.SUCCESS);
        this.setData(data);
        this.setTotal(total);
        this.setSize(size);
        this.setCurrent(current);
        this.setPages((total + size - 1) / size);
        this.setHasPrevious(current > 1);
        this.setHasNext(current < this.pages);
    }

    public static <T> PageResult<T> success(IPage<T> page) {
        return new PageResult<>(page);
    }

    public static <T> PageResult<T> success(List<T> data, Long total, Long size, Long current) {
        return new PageResult<>(data, total, size, current);
    }

    public static <T> PageResult<T> success(List<T> data, Long total, Long size, Long current, Long pages) {
        PageResult<T> result = new PageResult<>();
        result.setData(data);
        result.setTotal(total);
        result.setSize(size);
        result.setCurrent(current);
        result.setPages(pages);
        result.setHasPrevious(current > 1);
        result.setHasNext(current < pages);
        return result;
    }
}