package com.casesoft.dmc.extend.third.descriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017-02-22.
 * kendo ui 分组
 */
public class GroupDescriptor extends SortDescriptor {
    private List<AggregateDescriptor> aggregates;

    public GroupDescriptor() {
        aggregates = new ArrayList<AggregateDescriptor>();
    }

    public List<AggregateDescriptor> getAggregates() {
        return aggregates;
    }
}
