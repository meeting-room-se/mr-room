package com.im.chat.service;

import com.im.chat.enums.CvsTypeEnum;
import com.mr.response.error.BusinessException;

public interface ISessionViewService {

    public void createSessionView(Long userId, CvsTypeEnum cvsType, Long entityId) throws BusinessException;
}
