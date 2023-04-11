package cn.luckyh.purchase.workflow.service;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/05/07 10:28
 */
public abstract class ProcessInstanceStartService {

    private String processDefinitionKey;

    public abstract void beforeStart();

    public abstract void afterStart();

    final String start() {
        beforeStart();
        String s = null;
        try {
            s = startProcessInstance();
        } catch (Exception e) {


        }
        afterStart();
        return s;
    }

    public String startProcessInstance() {
        return "";
    }
}
