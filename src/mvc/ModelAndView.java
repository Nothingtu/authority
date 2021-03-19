package mvc;

import java.util.HashMap;

/**
 * 这个类没有什么特别的含义 仅用来存包裹两个值
 * 一个是返回的字符串
 * 另一个是需要携带走的参数
 */

public class ModelAndView {
    private String viewResourceName ;
    private HashMap<String ,Object> attributeMap = new HashMap<String,Object>();

    //给ModelAndView是用户可以做的事情
    public void setViewResourceName(String viewResourceName){
        this.viewResourceName = viewResourceName;
    }
    public void setAttributeMap(String key,Object obj){
        this.attributeMap.put(key,obj);
    }

    //以下是框架自己做的事情--------------------------------------------
    String getViewResourceName(){return this.viewResourceName;}
    Object getAttributeMap(String key){return this.attributeMap.get(key);}
    HashMap<String,Object> getAttributeMaps(){return this.attributeMap;}
}
