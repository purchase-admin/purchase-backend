package cn.luckyh.purchase.system.controller;

import cn.luckyh.purchase.common.core.domain.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.luckyh.purchase.framework.web.domain.Server;

/**
 * 服务器监控
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
    //@PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public R getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return R.success(server);
    }
}
