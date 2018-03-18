package com.casesoft.dmc.model.product;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_STYLECOLLOCATION")
public class StyleCollocation implements java.io.Serializable {
	private String id;
	private String style1;
	private String style2;
    private String style2Name;

	@Id
	@Column(nullable = false, length = 45)
	public String getId() {
		return id;
	}
	
	@Column( nullable = false, length = 20)
	public String getStyle1() {
		return style1;
	}

	@Column(nullable = false, length = 20)
	public String getStyle2() {
		return style2;
	}
	public void setStyle1(String style1) {
		this.style1 = style1;
	}


	public void setStyle2(String style2) {
		this.style2 = style2;
	}

	public void setId(String id) {
		this.id = id;
	}



  @Transient
  public String getStyle2Name() {
    return style2Name;
  }

  public void setStyle2Name(String style2Name) {
    this.style2Name = style2Name;
  }
}
