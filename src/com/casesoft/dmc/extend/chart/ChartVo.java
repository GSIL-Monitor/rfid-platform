package com.casesoft.dmc.extend.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChartVo<C, N, D> implements Serializable {

  private static final long serialVersionUID = -657403301680195784L;

  private List<C> categories = new ArrayList<C>();
  private List<Serie> series = new ArrayList<Serie>();

  public List<C> getCategories() {
    return categories;
  }

  public void setCategories(List<C> categories) {
    this.categories = categories;
  }

  public void addCategory(C category) {
    this.categories.add(category);
  }

  public void addSerie(Serie s) {
    this.series.add(s);
  }

  public List<Serie> getSeries() {
    return series;
  }

  public void setSeries(List<Serie> series) {
    this.series = series;
  }

  public class Serie implements Serializable {

    private static final long serialVersionUID = 2265608497508618083L;

    private N name;
    private List<D> data = new ArrayList<D>();

    public N getName() {
      return name;
    }

    public void setName(N name) {
      this.name = name;
    }

    public List<D> getData() {
      return data;
    }

    public void setData(List<D> data) {
      this.data = data;
    }

    public void addData(D d) {
      data.add(d);
    }

  }

  public class DataComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
      long[] l1 = (long[]) o1;
      long[] l2 = (long[]) o2;

      int flag = new Long(l1[0]).compareTo(l2[0]);
      return flag;
    }

  }
}
