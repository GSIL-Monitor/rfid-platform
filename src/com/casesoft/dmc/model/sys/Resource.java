package com.casesoft.dmc.model.sys;

// default package

import javax.persistence.*;
import java.util.List;

/**
 * Resource entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_RESOURCE")
public class Resource implements java.io.Serializable {

  private static final long serialVersionUID = -9047312945811338574L;

  // Fields

  @Override
  public String toString() {
    return "Resource{" +
            "code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", ownerId='" + ownerId + '\'' +
            ", seqNo=" + seqNo +
            ", url='" + url + '\'' +
            ", iconCls='" + iconCls + '\'' +
            ", image='" + image + '\'' +
            ", remark='" + remark + '\'' +
            ", status=" + status +
            ", wxUrl='" + wxUrl + '\'' +
            ", locked=" + locked +
            ", clientCode='" + clientCode + '\'' +
            ", clientName='" + clientName + '\'' +
            ", children=" + children +
            ", ename='" + ename + '\'' +
            ", expand=" + expand +
            ", isLeaf=" + isLeaf +
            ", level='" + level + '\'' +
            ", isChecked=" + isChecked +
            ", solution='" + solution + '\'' +
            '}';
  }

  public Resource(String code, String name, String ownerId, Integer seqNo, String url, String iconCls, String image, String remark, Integer status, String wxUrl, Integer locked, String clientCode, String clientName, List<Resource> children, String ename,  boolean expand, boolean isLeaf, String level, boolean isChecked, String solution) {
    this.code = code;
    this.name = name;
    this.ownerId = ownerId;
    this.seqNo = seqNo;
    this.url = url;
    this.iconCls = iconCls;
    this.image = image;
    this.remark = remark;
    this.status = status;
    this.wxUrl = wxUrl;
    this.locked = locked;
    this.clientCode = clientCode;
    this.clientName = clientName;
    this.children = children;
    this.ename = ename;

    this.expand = expand;
    this.isLeaf = isLeaf;
    this.level = level;
    this.isChecked = isChecked;
    this.solution = solution;
  }

  private String code;//编号
  private String name;//菜单名
  private String ownerId;//父菜单id
  private Integer seqNo;//子菜单序列号
  private String url;//页面路径
  private String iconCls;//图标名
  private String image;//图片名
  private String remark;//
  private Integer status=1;//状态
  private String wxUrl;//小程序url
  private Integer locked=0;//状态
  private String clientCode;//小程序编号
  private String clientName;//小程序名
  private List<Resource> children;
  private String ename;//英文名
  private List<ResourceButton> resourceButtonList;
  private List<ResourceButton> resourcetableList;

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }




    private boolean expand = true;
    private boolean isLeaf = true;
    private String level;
    private boolean isChecked;

    private String solution;

  @Column()
  public Integer getLocked() {
    return locked;
  }

  public void setLocked(Integer locked) {
    this.locked = locked;
  }

  // Constructors

  /** default constructor */
  public Resource() {
  }

  /** minimal constructor */
  public Resource(String name, String ownerId) {
    this.name = name;
    this.ownerId = ownerId;
  }

  public Resource(String code, String name, String clientCode, String clientName) {
    this.code = code;
    this.name = name;
    this.clientCode = clientCode;
    this.clientName = clientName;
  }

  /** full constructor */
  public Resource(String name, String ownerId, Integer seqNo, String url, String iconCls,
      String image, String remark, Integer status) {
    this.name = name;
    this.ownerId = ownerId;
    this.seqNo = seqNo;
    this.url = url;
    this.iconCls = iconCls;
    this.image = image;
    this.remark = remark;
    this.status = status;
  }



  // Property accessors
  @Id
  @Column(nullable = false, length = 30)
  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(nullable = false, length = 45)
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(nullable = false, length = 30)
  public String getOwnerId() {
    return this.ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column()
  public Integer getSeqNo() {
    return this.seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(length = 45)
  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  @Column(length = 90)
  public String getWxUrl() {
    return wxUrl;
  }

  public void setWxUrl(String wxUrl) {
    this.wxUrl = wxUrl;
  }



  @Column(length = 40)
  public String getIconCls() {
    return this.iconCls;
  }

  public void setIconCls(String iconCls) {
    this.iconCls = iconCls;
  }

  @Column(length = 30)
  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Column(length = 400)
  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Column()
  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(nullable = false, length = 40)
  public String getEname() {
    return ename;
  }

  public void setEname(String ename) {
    this.ename = ename;
  }
  @Column(length = 10)
  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }
  @Column(length = 50)
  public String getClientCode() {
    return clientCode;
  }

  public void setClientCode(String clientCode) {
    this.clientCode = clientCode;
  }
  @Column(length = 50)
  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }


  // -------------------->


  @Transient
  public List<Resource> getChildren() {
    return children;
  }

  public void setChildren(List<Resource> children) {
    this.children = children;
  }
  @Transient
  public List<ResourceButton> getResourceButtonList() {
    return resourceButtonList;
  }

  public void setResourceButtonList(List<ResourceButton> resourceButtonList) {
    this.resourceButtonList = resourceButtonList;
  }
  @Transient
  public List<ResourceButton> getResourcetableList() {
    return resourcetableList;
  }

  public void setResourcetableList(List<ResourceButton> resourcetableList) {
    this.resourcetableList = resourcetableList;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((ename == null) ? 0 : ename.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Resource other = (Resource) obj;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    if (ename == null) {
      if (other.ename != null)
        return false;
    } else if (!ename.equals(other.ename))
      return false;
    return true;
  }

    @Transient
    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
    @Transient
    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
    @Transient
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    @Transient
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}