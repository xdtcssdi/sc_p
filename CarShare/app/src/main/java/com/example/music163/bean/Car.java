package com.example.music163.bean;

public class Car {


    /**
     * cid : 1
     * cname : Polo
     * detail : 手动 自动 手自一体 双离合
     * bname : 大众
     * typename : 小型车
     * mainimg : https://qncar2.autoimg.cn/cardfs/product/g2/M03/31/11/autohomecar__ChcCRFsD-EqAL9QXAAmfEH8T-X0511.jpg
     * video : https://n5-pl-agv.autohome.com.cn/video-33/0A33363922E51BDE/2019-03-05/38B8E11BE27EBA37-300.mp4
     */

    private int cid;
    private String cname;
    private String detail;
    private String bname;
    private String typename;
    private String mainimg;
    private String video;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getMainimg() {
        return mainimg;
    }

    public void setMainimg(String mainimg) {
        this.mainimg = mainimg;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
