package com.hdsx.geohome.geocoding.vo;

import com.alibaba.fastjson.JSON;
import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;

/**
 * Created by jzh on 2017/6/24.
 */
public class Element implements Serializable {

    private static final long serialVersionUID = 2179651172415764581L;

    private String id;

    private String name;

    private String code;

    private String address;

    private String table;

    private float order;

    private String district;

    private String describe;

    private Geometry geometry;

    private String geometryType;

    private double lon;
    
    private double lat;
    
    private String fr;
    private String tel;
    private String cusccode;
    private Double iszdy;
    private Double isxkz;
    private Double isonline;
    private Double corptype;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getOrder() {
        return order;
    }

    public void setOrder(float order) {
        this.order = order;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
        if (geometry != null) {
            this.geometryType = geometry.getGeometryType();
        }
    }


    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public String getFr() {
		return fr;
	}

	public void setFr(String fr) {
		this.fr = fr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCusccode() {
		return cusccode;
	}

	public void setCusccode(String cusccode) {
		this.cusccode = cusccode;
	}

	public Double getIszdy() {
		return iszdy;
	}

	public void setIszdy(Double iszdy) {
		this.iszdy = iszdy;
	}

	public Double getIsxkz() {
		return isxkz;
	}

	public void setIsxkz(Double isxkz) {
		this.isxkz = isxkz;
	}

	public Double getIsonline() {
		return isonline;
	}

	public void setIsonline(Double isonline) {
		this.isonline = isonline;
	}

	public Double getCorptype() {
		return corptype;
	}

	public void setCorptype(Double corptype) {
		this.corptype = corptype;
	}

	public Element() {
    }

    public Element(String id, String name, String code, String address, String table, float order, String district, String fr,
     String tel,
     String cusccode,
     Double iszdy,
     Double isxkz,
     Double isonline,
     Double corptype) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.address = address;
        this.table = table;
        this.order = order;
        this.district = district;
        this.fr = fr;
        this.tel =  tel;
        this.cusccode =  cusccode;
        this.iszdy =  iszdy;
        this.isxkz =  isxkz;
        this.isonline =  isonline;
        this.corptype =  corptype;
    }

    public Element(String id, String name, String code, String address, String table, float order, String district, Geometry geometry, String fr,
    	     String tel,
    	     String cusccode,
    	     Double iszdy,
    	     Double isxkz,
    	     Double isonline,
    	     Double corptype) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.address = address;
        this.table = table;
        this.order = order;
        this.district = district;
        this.geometry = geometry;
        this.fr = fr;
        this.tel =  tel;
        this.cusccode =  cusccode;
        this.iszdy =  iszdy;
        this.isxkz =  isxkz;
        this.isonline =  isonline;
        this.corptype =  corptype;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this,new JSONValueFilter(1));
    }
}
