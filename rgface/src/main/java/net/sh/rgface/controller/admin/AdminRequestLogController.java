package net.sh.rgface.controller.admin;

import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.RequestLogEntity;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.admin.AdminAccountInfoPo;
import net.sh.rgface.po.admin.AdminRequestLogListPo;
import net.sh.rgface.po.admin.AdminRequestLogPo;
import net.sh.rgface.po.base.BasePagingListPo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.serive.admin.AdminRequestLogService;
import net.sh.rgface.vo.PagingListVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESTINY on 2018/5/23.
 */

@RestController
@RequestMapping(value = "/api/admin/log", method = RequestMethod.POST)
public class AdminRequestLogController {

    @Resource
    private AdminRequestLogService adminRequestLogService;

    @RequestMapping(value = "/getRequestLogList")
    public BasePagingListPo getRequestLogList(@RequestParam("type") String type
            , @RequestParam("pageCode") String pageCode, @RequestParam("pageSize") String pageSize
            , @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
            , @RequestParam("searchParam") String searchParam, HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("已离线请重新登录");
        }

        if (type == null || pageCode == null
                || pageSize == null || startTime == null || endTime == null
                || type.length() == 0 || pageCode.length() == 0
                || pageSize.length() == 0 || startTime.length() == 0 || endTime.length() == 0) {

            throw new ControllerException("参数错误");
        }

        int typeN = Integer.parseInt(type);
        int pageCodeN = Integer.parseInt(pageCode);
        int pageSizeN = Integer.parseInt(pageSize);
        int startTimeN = Integer.parseInt(startTime);
        int endTimeN = Integer.parseInt(endTime);

        if (pageCodeN <= 0 || pageSizeN <= 0) {
            throw new ControllerException("参数错误");
        }

        if (typeN == 1 && searchParam == null && startTimeN == 0 && endTimeN == 0) {

            throw new ControllerException("请输入搜索内容");
        }

        SearchVo searchVo = new SearchVo();
        searchVo.setType(typeN);
        searchVo.setSearchParam(searchParam);
        searchVo.setStartTime(startTimeN);
        searchVo.setEndTime(endTimeN);

        BasePagingListPo pagingListPo = new BasePagingListPo();
        Page<RequestLogEntity> requestLogEntityPage = adminRequestLogService.getRequestLogList(
                Integer.parseInt(pageCode),
                Integer.parseInt(pageSize),
                searchVo
        );

        if (requestLogEntityPage != null) {

            List<AdminRequestLogListPo> adminRequestLogListPoList = new ArrayList<>();
            List<RequestLogEntity> requestLogEntities = requestLogEntityPage.getContent();

            if (requestLogEntities.size() > 0) {

                for (RequestLogEntity requestLogEntity : requestLogEntities) {

                    AdminRequestLogListPo adminRequestLogListPo = new AdminRequestLogListPo();

                    AdminRequestLogPo adminRequestLogPo = new AdminRequestLogPo();
                    adminRequestLogPo.setId(requestLogEntity.getId());
                    adminRequestLogPo.setIp(requestLogEntity.getIp());
                    adminRequestLogPo.setUrl(requestLogEntity.getUrl());
                    adminRequestLogPo.setType(requestLogEntity.getType());
                    adminRequestLogPo.setMethod(requestLogEntity.getMethod());
                    adminRequestLogPo.setClassMethod(requestLogEntity.getClassMethod());
                    adminRequestLogPo.setActionStatus(requestLogEntity.getActionStatus());
                    adminRequestLogPo.setStatusCode(requestLogEntity.getStatusCode());
                    adminRequestLogPo.setStatTime(requestLogEntity.getCreateTime());
                    adminRequestLogPo.setEndTime(requestLogEntity.getEndTime());
                    adminRequestLogPo.setCreateTime(requestLogEntity.getCreateTime());
                    adminRequestLogPo.setDeleteTag(requestLogEntity.getDeleteTag());

                    adminRequestLogListPo.setAdminRequestLogPo(adminRequestLogPo);

                    AccountEntity accountEntity = requestLogEntity.getAccountEntityByAccountId();
                    if (accountEntity != null) {

                        AdminAccountInfoPo adminAccountInfoPo = new AdminAccountInfoPo();

                        adminAccountInfoPo.setAccountId(accountEntity.getId());
                        adminAccountInfoPo.setAccountType(accountEntity.getAccountType());
                        adminAccountInfoPo.setAccountName(accountEntity.getAccountName());

                        adminRequestLogListPo.setPersonnelPo(adminAccountInfoPo);
                    }

                    adminRequestLogListPoList.add(adminRequestLogListPo);
                }

                pagingListPo.setData(adminRequestLogListPoList);
            }

            pagingListPo.setMessage("获取成功");
            pagingListPo.setPageTotal((int) requestLogEntityPage.getTotalElements());

        }

        pagingListPo.setStatus(200);
        return pagingListPo;
    }

    @RequestMapping(value = "/getNewestRequestLogList")
    public ResultPo getNewestRequestLogList() {

        List<RequestLogEntity> requestLogEntities = adminRequestLogService.getNewestRequestLogList();

        ResultPo resultPo = new ResultPo();
        if (requestLogEntities != null) {

            List<AdminRequestLogPo> adminRequestLogPos = new ArrayList<>();
            if (requestLogEntities.size() > 0) {

                for (RequestLogEntity requestLogEntity : requestLogEntities) {

                    AdminRequestLogPo adminRequestLogPo = new AdminRequestLogPo();
                    adminRequestLogPo.setIp(requestLogEntity.getIp());
                    adminRequestLogPo.setUrl(requestLogEntity.getUrl());
                    adminRequestLogPo.setMethod(requestLogEntity.getMethod());
                    adminRequestLogPo.setActionStatus(requestLogEntity.getActionStatus());
                    adminRequestLogPo.setCreateTime(requestLogEntity.getCreateTime());
                    adminRequestLogPo.setStatusCode(requestLogEntity.getStatusCode());

                    adminRequestLogPos.add(adminRequestLogPo);
                }

                resultPo.setData(adminRequestLogPos);
                resultPo.setStatus("SUCCESS");
                resultPo.setMessage("获取成功");
            }
        }

        return resultPo;
    }
}
