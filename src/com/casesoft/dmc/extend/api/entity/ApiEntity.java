package com.casesoft.dmc.extend.api.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created john  on 2016-12-27.
 * 接口存储
 */

/*@Entity
@Table(name="api")*/
public class ApiEntity {
    private long id;
    private String path;//访问路径
    private String name;//接口名称
    private String version;//接口版本
    private String method;//访问方法
    private String description;//接口表述
    private String requestBody;//请求参数
    private String reponseBody;//返回数据

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getReponseBody() {
        return reponseBody;
    }

    public void setReponseBody(String reponseBody) {
        this.reponseBody = reponseBody;
    }
}
