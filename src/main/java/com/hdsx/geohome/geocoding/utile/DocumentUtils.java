package com.hdsx.geohome.geocoding.utile;
/**
 *

 /*
 doc.add(new SortedDocValuesField("name", new BytesRef(element.getName()==null?"":element.getName())););
 doc.add(new SortedDocValuesField("code", new BytesRef(element.getCode()==null?"":element.getCode())));
 doc.add(new SortedDocValuesField("describe", new BytesRef(element.getDescribe()==null?"":element.getDescribe())));
 */



import com.hdsx.geohome.geocoding.vo.Element;
import com.hdsx.toolkit.jts.JTSTools;
import com.hdsx.toolkit.number.NumberUtile;
import com.vividsolutions.jts.geom.Point;
import org.apache.lucene.document.*;
import org.apache.lucene.spatial.SpatialStrategy;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.io.WKTReader;
import org.locationtech.spatial4j.shape.Shape;


/**
 * Created by jingzh on 2016/9/13.
 * http://www.fengxiaochuang.com/?p=160
 */
public class DocumentUtils {

    //默认分词 TextField
    //默认不分词 StringField
    //数字排序专用 NumericDocValuesField

    private DocumentUtils(){}

    public static Document element2Document(Element element){
        Document doc=new Document();//索引文档
        doc.add(new StringField("id",element.getId()==null?"":element.getId(),StringField.Store.YES));

        ITextField fName = new ITextField("name",element.getName()==null?"":element.getName(),StringField.Store.YES);
        fName.setBoost(element.getOrder());
        doc.add(fName);

        ITextField fCode = new ITextField("code",element.getCode()==null?"":element.getCode(),StringField.Store.YES);
        fCode.setBoost(element.getOrder());
        doc.add(fCode);
        ITextField fAddress = new ITextField("address",element.getAddress()==null?"":element.getAddress(),StringField.Store.YES);
        fAddress.setBoost(element.getOrder());
        doc.add(fAddress);
        doc.add(new StringField("table",element.getTable()==null?"":element.getTable(),StringField.Store.YES));
        doc.add(new StringField("district",element.getDistrict()==null?"":element.getDistrict(),StringField.Store.YES));

        doc.add(new NumericDocValuesField("order", NumberUtile.float2long(element.getOrder())));
        doc.add(new StringField("describe",Float.toString(element.getOrder()),StringField.Store.YES));
        if(element.getGeometry() != null){
            Point jtsPoint = (Point)element.getGeometry();
            try {
                WKTReader wktReader = new WKTReader(SpatialContext.GEO,new SpatialContextFactory());
                Shape shape =  wktReader.read(jtsPoint.toText());
                SpatialStrategy strategy = SpatialUtils.createStrategy();
                for (Field f : strategy.createIndexableFields(shape)) {
                    doc.add(f);
                }
                doc.add(new StoredField(strategy.getFieldName(), JTSTools.getInstance().toWKT(jtsPoint)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doc;
    }

    public static Element document2Element(Document document){
        Element element=new Element();
        element.setId(document.get("id"));
        element.setName(document.get("name"));
        element.setCode(document.get("code"));
        element.setOrder(NumberUtile.string2float(document.get("order")));
        element.setTable(document.get("table"));
        element.setAddress(document.get("address"));
        element.setDistrict(document.get("district"));
        try{
            if(document.get("shape") != null){
                element.setGeometry(JTSTools.getInstance().toGeometry(document.get("shape")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  element;
    }


}