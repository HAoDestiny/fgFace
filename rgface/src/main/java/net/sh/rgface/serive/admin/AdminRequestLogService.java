package net.sh.rgface.serive.admin;

import net.sh.rgface.entity.RequestLogEntity;
import net.sh.rgface.repository.RequestLogRepository;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by DESTINY on 2018/5/23.
 */

@Service
public class AdminRequestLogService {

    @Resource
    private RequestLogRepository requestLogRepository;

    public void addRequestLog(RequestLogEntity requestLogEntity) {
        if (requestLogEntity != null) {
            requestLogRepository.saveAndFlush(requestLogEntity);
        }
    }

    public Page<RequestLogEntity> getRequestLogList(int pageCode, int pageSize, SearchVo searchVo) {

        Pageable pageable = null;

        Page<RequestLogEntity> page = null;

        if (searchVo.getType() == 0) {
            pageable = PageRequest.of(pageCode - 1, pageSize, new Sort(Sort.Direction.DESC, "create_time"));

            page = requestLogRepository.findByDeleteTag(0, pageable);
        }

        if (searchVo.getType() == 1) {

            pageable = PageRequest.of(pageCode - 1, pageSize);

            //searchParam
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() == 0 || searchVo.getEndTime() == 0)) {

                page = requestLogRepository.searchRequestLogByNameOrStatus("%" + searchVo.getSearchParam() + "%", pageable);
            }

            //searchParam Or searchTime
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() != 0 && searchVo.getEndTime() != 0)) {

                page = requestLogRepository.searchRequestLogByAll(
                        "%" + searchVo.getSearchParam() + "%",
                        searchVo.getStartTime(),
                        searchVo.getEndTime(),
                        pageable
                );
            }

            //searchTime
            if ((searchVo.getSearchParam() == null || searchVo.getSearchParam().length() == 0) &&
                    searchVo.getStartTime() != 0 && searchVo.getEndTime() != 0) {

                page = requestLogRepository.searchRequestLogByCreateTime(
                        searchVo.getStartTime(),
                        searchVo.getEndTime(),
                        pageable
                );
            }

        }

        return page;
    }

    public List<RequestLogEntity> getNewestRequestLogList() {

        return requestLogRepository.findByCreateTime();
    }
}
