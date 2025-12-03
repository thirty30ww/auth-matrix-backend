package com.thirty.user.model.entity.base;

import com.thirty.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasePermission extends BaseEntity {
    /**
     * 权限名称
     */
    private String name;

    /**
     * 路径名
     */
    private String path;

    /**
     * 组件路径
     */
     private String component;

     /**
      * 权限码
      */
     private String permissionCode;

     /**
      * 父节点ID(无父结点则为0)
      */
     private Integer parentId;

      /**
       * 该节点的前一个节点ID(若为第1个节点则取0)
       */
     private Integer frontId;

     /**
      * 该节点的后继节点ID
      */
     private Integer behindId;

      /**
       * 是否启用(1:启用 0:不启用)
       */
     private Boolean isValid;
}
