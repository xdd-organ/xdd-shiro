package com.java.xdd.shiro.pojo;

/**
 * Created by Administrator on 2017/8/19.
 */
public class Permission implements java.io.Serializable{

    private static final long serialVersionUID = -7128864678063514625L;
    private Long id;
    private String code;
    private String name;
    private String url;
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
