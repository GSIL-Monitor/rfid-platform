package com.casesoft.dmc.controller.sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.casesoft.dmc.core.vo.SidebarMenu;
import com.casesoft.dmc.core.vo.TreeNode;
import com.casesoft.dmc.model.sys.Resource;

public class ResourceUtil {

	public static List<TreeNode> convertToVo(List<Resource> resourceList) {
		List<TreeNode> parentNodes = new ArrayList<TreeNode>();
		for(Resource resource : resourceList) {
			if(resource.getSeqNo() != 0)
				continue;
				
			TreeNode parent = wrapNode(resource,true);
			parentNodes.add(parent);
		}
		for(TreeNode node : parentNodes) {
			for(Resource child : resourceList) {
				if(child.getOwnerId().equals(node.getId())) {//如果该孩子节点的ID为该父节点
			        TreeNode childNode = wrapNode(child,false);
			        node.addChild(childNode);
				}
			}
		}
		
		return parentNodes;
	}

	private static TreeNode wrapNode(Resource resource, boolean isParent) {
		TreeNode node = new TreeNode();
		node.setId(resource.getCode());
		node.setText(resource.getName());
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("url", resource.getUrl());
		attributes.put("parentId", resource.getOwnerId());
		node.setAttributes(attributes);
		node.setIconCls(resource.getIconCls());
		if(isParent && resource.getChildren() != null && resource.getChildren().size() > 0) {
			node.setState("closed");
			Collections.sort(resource.getChildren(), new MenuComparator());//对子菜单进行排序
			for(Resource r : resource.getChildren()) {
				TreeNode childNode = wrapNode(r,false);
				node.addChild(childNode);
			}
		}
		return node;
	}

    public static List<SidebarMenu> convertToSidebarMenuVo(List<Resource> resourceList) {
        List<SidebarMenu> parentNodes = new ArrayList<SidebarMenu>();
        for(Resource resource : resourceList) {
            if(resource.getSeqNo() != 0)
                continue;

            SidebarMenu parent = wrapSidebarMenu(resource, true);
            parentNodes.add(parent);
        }
        for(SidebarMenu node : parentNodes) {
            for(Resource child : resourceList) {
                if(child.getOwnerId().equals(node.getId())) {//如果该孩子节点的ID为该父节点
                    SidebarMenu childNode = wrapSidebarMenu(child,false);
                    node.addChild(childNode);
                }
            }
        }

        return parentNodes;
    }

    private static SidebarMenu wrapSidebarMenu(Resource resource, boolean isParent) {
        SidebarMenu node = new SidebarMenu();
        node.setId(resource.getCode());
        node.setText(resource.getName());
        node.setUrl(resource.getUrl());
        node.setIcon(resource.getIconCls());
        node.setWxUrl(resource.getWxUrl());
        node.setImage(resource.getImage());
        if(isParent && resource.getChildren() != null && resource.getChildren().size() > 0) {
            Collections.sort(resource.getChildren(), new MenuComparator());//对子菜单进行排序
            for(Resource r : resource.getChildren()) {
                SidebarMenu childNode = wrapSidebarMenu(r, false);
                node.addChild(childNode);
            }
        }
        return node;
    }
}

class MenuComparator implements Comparator<Resource> {

	@Override
	public int compare(Resource res1, Resource res2) {
		int i1 = res1.getSeqNo()!=null ? res1.getSeqNo():-1;
		int i2 = res2.getSeqNo()!=null ? res2.getSeqNo():-1;
		return i1-i2;
	}
}

class MenuIdComparator implements Comparator<Resource> {

    @Override
    public int compare(Resource res1, Resource res2) {
       return res1.getCode().compareTo(res2.getCode());

    }
}

