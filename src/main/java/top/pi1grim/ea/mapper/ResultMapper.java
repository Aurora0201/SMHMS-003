package top.pi1grim.ea.mapper;

import top.pi1grim.ea.dto.ResultDTO;
import top.pi1grim.ea.entity.Result;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 结果表 Mapper 接口
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
public interface ResultMapper extends BaseMapper<Result> {
    void insResult(ResultDTO dto);
}
