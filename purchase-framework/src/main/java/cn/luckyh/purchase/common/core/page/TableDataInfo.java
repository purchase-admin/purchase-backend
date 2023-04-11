package cn.luckyh.purchase.common.core.page;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 表格分页数据对象
 */
public class TableDataInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<?> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo() {
    }

    /**
     * 分页
     *
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, long total) {
        this.rows = list;
        this.total = total;
    }

    public TableDataInfo(List<?> list, long total, HttpStatus status) {
        this.rows = list;
        this.total = total;
        this.code = status.value();
        this.msg = status.getReasonPhrase();
    }

    public TableDataInfo(int status, String msg) {
        this.code = status;
        this.msg = msg;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static TableDataInfo success(List<?> list, long total, HttpStatus status) {
        return new TableDataInfo(list, total, status);
    }

    public static TableDataInfo error(String msg) {
        return new TableDataInfo(500, msg);
    }

    public static TableDataInfo data(IPage page) {
        return success(page.getRecords(), page.getTotal(),HttpStatus.OK);
    }
}
