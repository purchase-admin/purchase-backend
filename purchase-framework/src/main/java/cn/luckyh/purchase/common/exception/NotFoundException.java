package cn.luckyh.purchase.common.exception;

/**
 * NotfoundException.
 *
 * @author heng.wang
 * @since 2021/05/07 15:01
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException() {
    }
}
