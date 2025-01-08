package com.atguigu.mybatis.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 菜单表
 * @TableName t_menu
 */
@Data
public class TMenu implements Serializable {
    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 类型
     */
    private Integer menuType;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 权限类型
     */
    private Integer permsType;

    /**
     * 后端权限字符串
     */
    private String apiPerms;

    /**
     * 前端权限字符串
     */
    private String webPerms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 功能点关联菜单ID
     */
    private Long contextMenuId;

    /**
     * 是否为外链
     */
    private Integer frameFlag;

    /**
     * 外链地址
     */
    private String frameUrl;

    /**
     * 是否缓存
     */
    private Integer cacheFlag;

    /**
     * 显示状态
     */
    private Integer visibleFlag;

    /**
     * 禁用状态
     */
    private Integer disabledFlag;

    /**
     * 删除状态
     */
    private Integer deletedFlag;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUserId;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TMenu other = (TMenu) that;
        return (this.getMenuId() == null ? other.getMenuId() == null : this.getMenuId().equals(other.getMenuId()))
            && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
            && (this.getMenuType() == null ? other.getMenuType() == null : this.getMenuType().equals(other.getMenuType()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getSort() == null ? other.getSort() == null : this.getSort().equals(other.getSort()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getComponent() == null ? other.getComponent() == null : this.getComponent().equals(other.getComponent()))
            && (this.getPermsType() == null ? other.getPermsType() == null : this.getPermsType().equals(other.getPermsType()))
            && (this.getApiPerms() == null ? other.getApiPerms() == null : this.getApiPerms().equals(other.getApiPerms()))
            && (this.getWebPerms() == null ? other.getWebPerms() == null : this.getWebPerms().equals(other.getWebPerms()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getContextMenuId() == null ? other.getContextMenuId() == null : this.getContextMenuId().equals(other.getContextMenuId()))
            && (this.getFrameFlag() == null ? other.getFrameFlag() == null : this.getFrameFlag().equals(other.getFrameFlag()))
            && (this.getFrameUrl() == null ? other.getFrameUrl() == null : this.getFrameUrl().equals(other.getFrameUrl()))
            && (this.getCacheFlag() == null ? other.getCacheFlag() == null : this.getCacheFlag().equals(other.getCacheFlag()))
            && (this.getVisibleFlag() == null ? other.getVisibleFlag() == null : this.getVisibleFlag().equals(other.getVisibleFlag()))
            && (this.getDisabledFlag() == null ? other.getDisabledFlag() == null : this.getDisabledFlag().equals(other.getDisabledFlag()))
            && (this.getDeletedFlag() == null ? other.getDeletedFlag() == null : this.getDeletedFlag().equals(other.getDeletedFlag()))
            && (this.getCreateUserId() == null ? other.getCreateUserId() == null : this.getCreateUserId().equals(other.getCreateUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateUserId() == null ? other.getUpdateUserId() == null : this.getUpdateUserId().equals(other.getUpdateUserId()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMenuId() == null) ? 0 : getMenuId().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getMenuType() == null) ? 0 : getMenuType().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getComponent() == null) ? 0 : getComponent().hashCode());
        result = prime * result + ((getPermsType() == null) ? 0 : getPermsType().hashCode());
        result = prime * result + ((getApiPerms() == null) ? 0 : getApiPerms().hashCode());
        result = prime * result + ((getWebPerms() == null) ? 0 : getWebPerms().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getContextMenuId() == null) ? 0 : getContextMenuId().hashCode());
        result = prime * result + ((getFrameFlag() == null) ? 0 : getFrameFlag().hashCode());
        result = prime * result + ((getFrameUrl() == null) ? 0 : getFrameUrl().hashCode());
        result = prime * result + ((getCacheFlag() == null) ? 0 : getCacheFlag().hashCode());
        result = prime * result + ((getVisibleFlag() == null) ? 0 : getVisibleFlag().hashCode());
        result = prime * result + ((getDisabledFlag() == null) ? 0 : getDisabledFlag().hashCode());
        result = prime * result + ((getDeletedFlag() == null) ? 0 : getDeletedFlag().hashCode());
        result = prime * result + ((getCreateUserId() == null) ? 0 : getCreateUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateUserId() == null) ? 0 : getUpdateUserId().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", menuId=").append(menuId);
        sb.append(", menuName=").append(menuName);
        sb.append(", menuType=").append(menuType);
        sb.append(", parentId=").append(parentId);
        sb.append(", sort=").append(sort);
        sb.append(", path=").append(path);
        sb.append(", component=").append(component);
        sb.append(", permsType=").append(permsType);
        sb.append(", apiPerms=").append(apiPerms);
        sb.append(", webPerms=").append(webPerms);
        sb.append(", icon=").append(icon);
        sb.append(", contextMenuId=").append(contextMenuId);
        sb.append(", frameFlag=").append(frameFlag);
        sb.append(", frameUrl=").append(frameUrl);
        sb.append(", cacheFlag=").append(cacheFlag);
        sb.append(", visibleFlag=").append(visibleFlag);
        sb.append(", disabledFlag=").append(disabledFlag);
        sb.append(", deletedFlag=").append(deletedFlag);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUserId=").append(updateUserId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}