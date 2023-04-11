package cn.luckyh.purchase.system.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 路由显示信息
 */
@Data
@Accessors(chain = true)
public class MetaVo {

    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    private String params;

    public MetaVo() {
    }

    public MetaVo(String title, String icon,String params) {
        this.title = title;
        this.icon = icon;
        this.params = params;
    }

    public MetaVo(String title, String icon, boolean noCache,String params) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.params=params;
    }

}
