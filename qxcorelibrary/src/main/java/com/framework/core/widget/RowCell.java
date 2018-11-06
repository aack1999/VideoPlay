package com.framework.core.widget;

/**
 * 设置布局的属性
 * Created by quxiang on 2017/3/28.
 */
public class RowCell {

    String label;

    int resid;

    boolean isAction=true;//是否有动作

    boolean isEnableSwicth=false;//是否是开关 和 isAcion 互斥

    boolean isSwicth=false;

    String desc;

    int viewid;

    public String getLabel() {
        return label;
    }

    public RowCell setLabel(String label) {
        this.label = label;
        return this;
    }

    public int getResid() {
        return resid;
    }

    public RowCell setResid(int resid) {
        this.resid = resid;
        return this;
    }

    public boolean isAction() {
        return isAction;
    }

    public RowCell setAction(boolean action) {
        isAction = action;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public RowCell setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public boolean isEnableSwicth() {
        return isEnableSwicth;
    }

    public RowCell setEnableSwicth(boolean enableSwicth) {
        isEnableSwicth = enableSwicth;
        return this;
    }

    public boolean isSwicth() {
        return isSwicth;
    }

    public RowCell setSwicth(boolean swicth) {
        isSwicth = swicth;
        return this;
    }

    public int getViewid() {
        return viewid;
    }

    public RowCell setViewid(int viewid) {
        this.viewid = viewid;
        return this;
    }
}
