package cn.ljrexclusive.modules.user.service.impl;

import cn.ljrexclusive.modules.user.entity.Test;
import cn.ljrexclusive.modules.user.mapper.TestMapper;
import cn.ljrexclusive.modules.user.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Junery
 * @since 2026-01-15
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
