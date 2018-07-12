package net.sh.rgface.vo.search;

/**
 * Created by DESTINY on 2018/6/7.
 */

public class SearchVo {

    private int type = 0; //0获取列表 1搜索
    private String searchParam; //姓名 学号 身份证后位
    private int startTime = 0; //时间段(精确到秒)
    private int endTime = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
