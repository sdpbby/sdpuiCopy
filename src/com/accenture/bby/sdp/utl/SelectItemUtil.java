package com.accenture.bby.sdp.utl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

public class SelectItemUtil implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 78507767953512371L;

	public static List<SelectItem> getSelectItemList(Map<String, String> map) {
		List<SelectItem> selectItems = new ArrayList<SelectItem>(map != null ? map.size() : 0);
		if (selectItems.size() == 0) {
			return selectItems;
		} else {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				selectItems.add(new SelectItem(entry.getValue(), entry.getKey()));
			}
		}
		return selectItems;
	}
	
}


