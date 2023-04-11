package cn.luckyh.purchase.common.core.domain;

/**
 * @author heng.wang
 * @since 2019/11/7 18:47
 */
public class ResultUtil<T> {

  private Result<T> result;

  //查询失败,请稍后重试
  private static final String ERROR_QUERY = "\u67e5\u8be2\u5931\u8d25,\u8bf7\u7a0d\u540e\u91cd\u8bd5";

    //POST

    //保存成功!
    private static final String SUCCESS_SAVE = "\u4fdd\u5b58\u6210\u529f";
  //保存失败,请稍后重试
  private static final String ERROR_SAVE = "\u4fdd\u5b58\u5931\u8d25,\u8bf7\u7a0d\u540e\u91cd\u8bd5";

    //PUT
    //更新成功!
    private static final String SUCCESS_UPDATE = "\u4fee\u6539\u6210\u529f";
    //更新失败,请稍后重试
    private static final String ERROR_UPDATE = "\u4fee\u6539\u5931\u8d25,\u8bf7\u7a0d\u540e\u91cd\u8bd5";


  public ResultUtil() {
    result = new Result<>();
    result.setSuccess(true);
    result.setMsg("success");
    result.setCode(200);
  }


  public Result<T> setData(T t) {
    this.result.setData(t);
    this.result.setCode(200);
    return this.result;
  }

  public Result<T> setData(Integer code, T t) {
    this.result.setData(t);
    this.result.setCode(code);
    return this.result;
  }

  public Result<T> setData(Integer code, T t, String msg) {
    this.result.setData(t);
    this.result.setCode(code);
    this.result.setMsg(msg);
    return this.result;
  }

  public Result<T> setSuccessMsg(String msg) {
    this.result.setSuccess(true);
    this.result.setMsg(msg);
    this.result.setCode(200);
    this.result.setData(null);
    return this.result;
  }

  public Result<T> setData(T t, String msg) {
    this.result.setData(t);
    this.result.setCode(200);
    this.result.setMsg(msg);
    return this.result;
  }

  public Result<T> setErrorMsg(String msg) {
    this.result.setSuccess(false);
    this.result.setMsg(msg);
    this.result.setCode(500);
    return this.result;
  }

  public Result<T> setErrorMsg(Integer code, String msg) {
    this.result.setSuccess(false);
    this.result.setMsg(msg);
    this.result.setCode(code);
    return this.result;
  }

  public static <T> Result<T> data(T t) {
    return new ResultUtil<T>().setData(t);
  }

  public static <T> Result<T> data(T t, String msg) {
    return new ResultUtil<T>().setData(t, msg);
  }

  public static <T> Result<T> success(String msg) {
    return new ResultUtil<T>().setSuccessMsg(msg);
  }

    public static <T> Result<T> successSave(T t) {
        return data(t,SUCCESS_SAVE);
    }

  public static <T> Result<T> error(String msg) {
    return new ResultUtil<T>().setErrorMsg(msg);
  }

  public static <T> Result<T> errorQuery() {
    return new ResultUtil<T>().setErrorMsg(ERROR_QUERY);
  }

  public static <T> Result<T> errorQuery(String msg) {
    return new ResultUtil<T>().setErrorMsg(String.format("%s:%s", ERROR_QUERY, msg));
  }

  public static <T> Result<T> errorSave() {
    return new ResultUtil<T>().setErrorMsg(ERROR_SAVE);
  }

  public static <T> Result<T> errorSave(String msg) {
    return new ResultUtil<T>().setErrorMsg(String.format("%s:%s", ERROR_SAVE, msg));
  }

  public static <T> Result<T> error(Integer code, String msg) {
    return new ResultUtil<T>().setErrorMsg(code, msg);
  }
}
