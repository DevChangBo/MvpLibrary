package com.example.dome.dbbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * ================================================================
 * 创建时间：2018/7/13 11:12
 * 创 建 人：Mr.常
 * 文件描述：返回的UesrLogin实体，用作数据库建表，不实现Serializable接口
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@Entity
public class UesrLogin {
    @Id(autoincrement = true)
    private long id;
    private String userName;//姓名
    private String officeName;//公司名称
    private String title;//职位
    private String userLogo;
    @Generated(hash = 526444298)
    public UesrLogin(long id, String userName, String officeName, String title,
            String userLogo) {
        this.id = id;
        this.userName = userName;
        this.officeName = officeName;
        this.title = title;
        this.userLogo = userLogo;
    }
    @Generated(hash = 1380072618)
    public UesrLogin() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getOfficeName() {
        return this.officeName;
    }
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUserLogo() {
        return this.userLogo;
    }
    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }
}
