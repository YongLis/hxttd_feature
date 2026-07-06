package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.srv.dependency.req.DependencyQueryReq;
import com.ly.ttd.biz.admin.srv.dependency.res.DependencyQueryRes;
import com.ly.ttd.biz.admin.srv.resource.ResourceOpFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yong.li
 * @since 2026/7/1 14:51
 */

@Slf4j
@RestController
@RequestMapping("/api/dependency")
@Tag(name = "血缘管理", description = "血缘管理接口")
public class DependencyController {


    @PostMapping("/get")
    public Result<DependencyQueryRes> getDependency(@RequestBody DependencyQueryReq req) {
        try {
            DependencyQueryRes res = ResourceOpFactory.getService(req.getResourceType())
                    .queryDependency(req);
            return Result.success(res);
        } catch (Exception e) {
            log.error("血缘查询失败", e);
        }
        return Result.error("血缘查询失败");
    }


}
