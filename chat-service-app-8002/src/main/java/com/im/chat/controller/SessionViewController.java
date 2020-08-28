package com.im.chat.controller;


import com.im.chat.annotation.CurrentUser;
import com.im.chat.entity.po.SessionView;
import com.im.chat.entity.vo.SessionViewVo;
import com.im.chat.enums.CvsNotDisturbEnum;
import com.im.chat.enums.CvsStickEnum;
import com.im.chat.enums.CvsTypeEnum;
import com.im.chat.service.ISessionViewService;
import com.im.user.entity.po.User;
import com.mr.response.ServerResponse;
import com.mr.response.error.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "会话相关的api")
@RestController
@RequestMapping("/chat/cvs/")
@CrossOrigin
public class SessionViewController
{
    @Autowired
    private ISessionViewService iSessionViewService;

    //cvsType --> U单聊  G群聊
    @ApiOperation(value = "创建会话")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cvsType", value = "会话类型", required = true,dataType = "String", example = "U"),
            @ApiImplicitParam(name = "entityId", value = "会话关联的实体Id", required = true,dataType = "Long", example = "18")
    }
    )
    @PostMapping("/create")
    public ServerResponse<String> createSessionView(@CurrentUser @ApiIgnore User user, String cvsType, Long entityId) throws BusinessException {

        iSessionViewService.createSessionView(user.getId(),CvsTypeEnum.nameOf(cvsType),entityId);
        return ServerResponse.success();
    }

    @ApiOperation(value = "查找当前用户的所有会话视图")
    @GetMapping("/list")
    //cvsType 时间Long
    public ServerResponse<List<SessionViewVo>> queryMySessionViewList(@CurrentUser @ApiIgnore User user)
    {
        List<SessionView> sessionViews = iSessionViewService.queryMySessionViewList(user.getId());
        List<SessionViewVo> sessionViewVos = new ArrayList<SessionViewVo>();
        for(SessionView sessionView:sessionViews){
            SessionViewVo sessionViewVo = new SessionViewVo();
            BeanUtils.copyProperties(sessionView,sessionViewVo);
            sessionViewVo.setCvsType(CvsTypeEnum.codeOf(sessionView.getCvsType()).getName());
            sessionViewVo.setStick(CvsStickEnum.codeOf(sessionView.getStick()).getStatus());
            sessionViewVo.setNotDisturb(CvsNotDisturbEnum.codeOf(sessionView.getNotDisturb()).getStatus());
            sessionViewVo.setLastMessageTime(sessionView.getLastMessageTime().getTime());
            sessionViewVos.add(sessionViewVo);
        }
        return ServerResponse.success(sessionViewVos);
    }

    //TODO
    @ApiOperation(value = "删除会话视图")
    @GetMapping("/delete")
    public ServerResponse<String> deleteSessionView(@CurrentUser @ApiIgnore User user,Long cvsId) throws BusinessException {
        iSessionViewService.deleteSessionView(cvsId);
        return ServerResponse.success();
    }


}