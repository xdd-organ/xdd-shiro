package com.java.xdd.shiro.pojo;

/**
 * Created by Administrator on 2017/8/19.
 */
public class Role implements java.io.Serializable{

    private static final long serialVersionUID = -6175078754197093043L;
    private Long id;
    private String code;
    private String name;

    public Role() {
    }

    public Role(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
