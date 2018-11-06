package com.aution.paidd.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class AreaInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6550045442456228986L;
	public String code;
	public String name;
	public ArrayList<AreaInfo> areaList;
}