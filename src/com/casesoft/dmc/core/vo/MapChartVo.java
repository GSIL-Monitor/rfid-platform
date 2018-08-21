package com.casesoft.dmc.core.vo;

import java.io.Serializable;
import java.util.*;


public class MapChartVo<N, D> implements Serializable {

  private static final long serialVersionUID = -657403301680195784L;


  private List<Serie> series = new ArrayList<Serie>();
 

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
    private String type;
    private String mapType;
    private boolean hoverable = false;
    private boolean roam = true;
    private Collection<MapData> data = new ArrayList<MapData>();
    private MarkPoint markPoint = new MarkPoint();
    private Map<String,Double[]> geoCoord = new TreeMap<String, Double[]>();


    public N getName() {
      return name;
    }

    public void setName(N name) {
      this.name = name;
    }

    public Collection<MapData> getData() {
      return data;
    }

    public void setData(Collection<MapData> data) {
      this.data = data;
    }

   

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public MarkPoint getMarkPoint() {
		return markPoint;
	}

	public void setMarkPoint(MarkPoint markPoint) {
		this.markPoint = markPoint;
	}

	public Map<String,Double[]> getGeoCoord() {
		return geoCoord;
	}

	public void setGeoCoord(Map<String,Double[]> geoCoords) {
		this.geoCoord = geoCoords;
	}

	public boolean isHoverable() {
		return hoverable;
	}

	public void setHoverable(boolean hoverable) {
		this.hoverable = hoverable;
	}

	public boolean isRoam() {
		return roam;
	}

	public void setRoam(boolean roam) {
		this.roam = roam;
	}

  } 
  public class MarkPoint implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int symbolSize = 5;// // 标注大小，半宽（半径）参数，当图形为方向或菱形则总宽度为symbolSize * 2
	private ItemStyle itemStyle = new ItemStyle();	
	private Emphasis emphasis = new Emphasis();
	private List<MapData> data;
	public List<MapData> getData() {
		return data;
	}
	public void setData(List<MapData> data) {
		this.data = data;
	}
	public int getSymbolSize() {
		return symbolSize;
	}
	public void setSymbolSize(int symbolSize) {
		this.symbolSize = symbolSize;
	}
	public ItemStyle getItemStyle() {
		return itemStyle;
	}
	public void setItemStyle(ItemStyle itemStyle) {
		this.itemStyle = itemStyle;
	}
	public Emphasis getEmphasis() {
		return emphasis;
	}
	public void setEmphasis(Emphasis emphasis) {
		this.emphasis = emphasis;
	}	
	  
  }
 
  public class ItemStyle  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private Normal normal = new Normal();
	public Normal getNormal() {
		return normal;
	}
	public void setNormal(Normal normal) {
		this.normal = normal;
	}
	  
  }
  public class Normal implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	private String borderColor ="#87cefa";
	private int borderWidth = 1;
	private Label label = new Label(false);
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public int getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
  }
  public class Label implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	private boolean show = false;
	public Label(boolean show){
		this.show = show;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	
  }
  public class Emphasis implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String borderColor ="#1e90ff'";
	private int borderWidth = 5;
	private Label label = new Label(true);
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public int getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}
	  
  }
  
  
  

 
}
