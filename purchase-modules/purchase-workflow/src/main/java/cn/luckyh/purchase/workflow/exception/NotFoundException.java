package cn.luckyh.purchase.workflow.exception;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/24 16:13
 */
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
    }

    public NotFoundException(String s) {
        super(s);
    }
}
