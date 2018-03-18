package com.casesoft.dmc.extend.third.descriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017-02-22.
 *
 * 过滤器
 */
public class FilterDescriptor {
    private String logic;
    private List<FilterDescriptor> filters;
    private String field;
    private Object value;
    private String operator;
    private boolean ignoreCase = true;

    public FilterDescriptor() {
        filters = new ArrayList<FilterDescriptor>();
    }

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public List<FilterDescriptor> getFilters() {
        return filters;
    }
}
