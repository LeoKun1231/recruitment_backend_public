package com.hqk.recruitment.model.base;

import java.util.List;

public interface TreeNode<T> {

        /**
         * 获取节点id
         *
         * @return 树节点id
         */
        T id();

        /**
         * 获取该节点的父节点id
         *
         * @return 父节点id
         */
        T parentId();

        /**
         * 是否是根节点
         *
         * @return true：根节点
         */
        boolean root();

        /**
         * 设置节点的子节点列表
         *
         * @param children 子节点
         */
        void setChildren(List<? extends TreeNode<T>> children);

        /**
         * 获取所有子节点
         *
         * @return 子节点列表
         */
        List<? extends TreeNode<T>> getChildren();

}