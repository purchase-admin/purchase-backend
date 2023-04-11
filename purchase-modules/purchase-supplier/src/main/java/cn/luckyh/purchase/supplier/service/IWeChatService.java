package cn.luckyh.purchase.supplier.service;

import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.workflow.vo.history.HistoryRecordResult;

import javax.servlet.http.HttpServletRequest;

public interface IWeChatService {

    R weChatLogin(String code, HttpServletRequest request);

    HistoryRecordResult historyProcessRecordQuery(String openId);
}
