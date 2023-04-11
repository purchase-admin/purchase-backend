package cn.luckyh.purchase.common.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import cn.luckyh.purchase.common.enums.OperationEnum;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/05/06 16:02
 */
public class BaseEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

    private T data;
    private OperationEnum operationEnum;

    public BaseEvent(Object source,T data, OperationEnum operationEnum) {
        super(source);
        this.data = data;
        this.operationEnum = operationEnum;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public OperationEnum getOperationEnum() {
        return operationEnum;
    }

    public void setOperationEnum(OperationEnum operationEnum) {
        this.operationEnum = operationEnum;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forClass(getData().getClass()));
    }
}
