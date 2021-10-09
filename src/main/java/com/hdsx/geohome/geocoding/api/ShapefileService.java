package com.hdsx.geohome.geocoding.api;


import com.hdsx.geohome.geocoding.vo.Element;

import java.util.List;

/**
 * Created by jzh on 2016/9/22.
 */
public interface ShapefileService {

    List<Element> read(String filepath);
}
