package com.casesoft.dmc.controller.sys;

import java.util.List;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.Role;
import com.casesoft.dmc.model.sys.RoleRes;

public class RoleUtil {

	public static void convertToVo(List<Role> roleList) {
		for(Role r : roleList) {
			List<RoleRes> rrList = CacheManager.getAuthByRoleId(r.getId());
			String authIds = "";
			String authNames = "";
			if(CommonUtil.isNotBlank(rrList)) {
				for (RoleRes rr : rrList) {
					authIds += "," + rr.getResId();
					if (!CommonUtil.isBlank(CacheManager.getResourceById(rr.getResId()))) {
						authNames += "," + (CacheManager.getResourceById(rr.getResId())).getName();
					}
				}

				r.setAuthIds(authIds.substring(1));
				if(!CommonUtil.isBlank(authNames)){
					r.setAuthNames(authNames.substring(1));
				}
			}
		}
	}

    public static List<Resource> convertToTreeGrid(List<Resource> resourceList) {
        for(Resource r : resourceList) {
            if(r.getOwnerId().equals("01")) {
                r.setLeaf(false);
                r.setLevel("0");
            } else {
                r.setExpand(false);
                r.setLevel("1");
            }
        }
        return resourceList;
    }
}
