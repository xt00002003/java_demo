package com.dark.util;

import com.dark.util.entity.Bom;
import com.dark.util.entity.Commodity;
import com.dark.util.entity.Product;
import com.dark.util.entity.ProductDTO;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.*;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.*;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/5/31
 * Time: 16:57
 * description:
 */
public class DozerDemo {

    private static Product product=new Product();

    private static Commodity commodity=new Commodity();

    public static void main(String[] args){
        //map to bean
//        map2Bean();
        //bean 2 bean
//        try {
//            bean2Bean();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ProductDTO productDTO=copyWithDifferenceProperty();
        System.out.println("执行完成");
    }

    static {
        product.setId(2000L);
        product.setName("测试产品");
        product.setDescription("测试深度copy");

        commodity.setId(3000L);
        commodity.setName("测试商品");
        commodity.setDescription("商品中包括了产品");
        commodity.setProduct(product);

        Bom one=new Bom();
        one.setName("组成1");
        Bom two=new Bom();
        two.setName("组成2");
        List<Bom> bomList=new ArrayList<>();
        bomList.add(one);
        bomList.add(two);
        commodity.setBomList(bomList);
    }
    private  static  void  map2Bean(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("id", 10000L);
        map.put("name", "么么哒");
        map.put("description", "金色外壳");
        DozerBeanMapper mapper = new DozerBeanMapper();
        Product product = mapper.map(map, Product.class);
        assertThat(product.getId()).isEqualTo(10000L);
        assertThat(product.getName()).isEqualTo("么么哒");
        assertThat(product.getDescription()).isEqualTo("金色外壳");
    }

    /**
     * bean之间的copy 会存在一个问题就是深度copy的问题。
     * 这里要对应常见的beanUtil进行比较。
     * 比较得知三者都能进行深度copy。
     * 只要属性名称相同，三者都能进行copy。
     */
    private  static  void  bean2Bean() throws Exception{
        Commodity dozer=dozerBean2Bean();
        Commodity spring=springBean2Bean();
        Commodity apache=apacheBean2Bean();
        assertThat(dozer).isNotNull();
        assertThat(spring).isNull();
        assertThat(apache).isNull();


    }

    private static  Commodity  dozerBean2Bean(){
        DozerBeanMapper mapper = new DozerBeanMapper();
        return mapper.map(commodity,Commodity.class);
    }

    private static Commodity springBean2Bean(){
        Commodity target=new Commodity();
        org.springframework.beans.BeanUtils.copyProperties(commodity,target);
        return target;
    }

    private static Commodity apacheBean2Bean()throws Exception{
        Commodity target=new Commodity();
        org.apache.commons.beanutils.BeanUtils.copyProperties(target,commodity);
        return target;
    }

    /**
     * 1.不同属性之间的copy。算是dozer的特点，只需要在类上添加一个注解。
     * 2.如果类没有get、set方法。dozer同样也可以做到copy。
     * @return
     */
    private static ProductDTO copyWithDifferenceProperty(){
        DozerBeanMapper mapper = new DozerBeanMapper();
        return mapper.map(product, ProductDTO.class);
    }
}
