package com.casesoft.dmc.extend.third.request;

import com.casesoft.dmc.extend.third.descriptor.AggregateDescriptor;
import com.casesoft.dmc.extend.third.descriptor.FilterDescriptor;
import com.casesoft.dmc.extend.third.descriptor.GroupDescriptor;
import com.casesoft.dmc.extend.third.descriptor.SortDescriptor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by john on 2017-02-22.
 */
public class RequestPageData<T> {
    private int page;
    private int pageSize;
    private int take;
    private int skip;
    private List<SortDescriptor> sort;
    private List<GroupDescriptor> group;
    private List<AggregateDescriptor> aggregate;
    private HashMap<String, T> data;

    private FilterDescriptor filter;

    public RequestPageData() {
        filter = new FilterDescriptor();
        data = new HashMap<String, T>();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public List<SortDescriptor> getSort() {
        return sort;
    }

    public void setSort(List<SortDescriptor> sort) {
        this.sort = sort;
    }

    public List<GroupDescriptor> getGroup() {
        return group;
    }

    public void setGroup(List<GroupDescriptor> group) {
        this.group = group;
    }

    public List<AggregateDescriptor> getAggregate() {
        return aggregate;
    }

    public void setAggregate(List<AggregateDescriptor> aggregate) {
        this.aggregate = aggregate;
    }

    public HashMap<String, T> getData() {
        return data;
    }

    public void setData(HashMap<String, T> data) {
        this.data = data;
    }

    public FilterDescriptor getFilter() {
        return filter;
    }

    public void setFilter(FilterDescriptor filter) {
        this.filter = filter;
    }
}
