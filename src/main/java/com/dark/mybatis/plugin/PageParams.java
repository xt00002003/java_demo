package com.dark.mybatis.plugin;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/5/6
 * Time: 14:15
 * description:
 */
public class PageParams {
    //当前页码
    private Integer page;
    //每页条数
    private Integer pageSize;
    //是否启动插件
    private Boolean useFlag;
    //是否检测当前页码有效性
    private Boolean checkFlag;
    //当前sql返回的总数，插件回填
    private Integer total;
    //sql以当前分页的总页数
    private Integer totalPage;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(Boolean useFlag) {
        this.useFlag = useFlag;
    }

    public Boolean getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(Boolean checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "PageParams{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", useFlag=" + useFlag +
                ", checkFlag=" + checkFlag +
                ", total=" + total +
                ", totalPage=" + totalPage +
                '}';
    }
}
