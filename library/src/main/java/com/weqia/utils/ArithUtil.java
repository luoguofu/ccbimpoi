package com.weqia.utils;

import java.math.BigDecimal;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，
 * 包括加减乘除和四舍五入。此类来自<a>http://m.cnblogs.com/23411/886749.html?full=1</a>
 * 注意：该类不处理金钱货币，金钱货币的处理运算请使用com.weqia.common.util.Money类。
 * @author      
 * @version 1.0  
 * @created 2011-10-20 上午09:54:58
 */
public class ArithUtil {
	
	//默认除法运算精度 
    private static final int DEF_DIV_SCALE = 10; 
    
    public static final double DB_HUNDRED = 100.0;
    
    /** 

     * 提供精确的加法运算。 

     * @param v1 被加数 

     * @param v2 加数 

     * @return 两个参数的和 

     */ 

    public static double add(double v1,double v2){ 

        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 

        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 

        return b1.add(b2).doubleValue(); 

    } 
    
    /**
     * 
     * @Description 提供精确的加法运算
     * @param bs    支持多个double
     * @return  
     *
     */
    public static double add(double ... bs){ 

        BigDecimal sum = new BigDecimal(Double.toString(bs[0]));
        for(int i = 1; i < bs.length; i++){
        	BigDecimal b2 = new BigDecimal(Double.toString(bs[i])); 
        	sum = sum.add(b2);
        }

        return sum.doubleValue(); 
        
    } 

    /** 

     * 提供精确的减法运算。 

     * @param v1 被减数 

     * @param v2 减数 

     * @return 两个参数的差 

     */ 

    public static double sub(double v1,double v2){ 

        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 

        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 

        return b1.subtract(b2).doubleValue(); 

    } 

    /** 

     * 提供精确的乘法运算。 

     * @param v1 被乘数 

     * @param v2 乘数 

     * @return 两个参数的积

     */ 

    public static double mul(double v1,double v2){ 

        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 

        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 

        return b1.multiply(b2).doubleValue(); 

    } 

    /** 

     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 

     * 小数点以后10位，以后的数字四舍五入。 

     * @param v1 被除数 

     * @param v2 除数 

     * @return 两个参数的商 

     */ 

    public static double div(double v1,double v2){ 

        return div(v1,v2,DEF_DIV_SCALE); 

    } 

    /** 

     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 

     * 定精度，以后的数字四舍五入。 

     * @param v1 被除数 

     * @param v2 除数 

     * @param scale 表示表示需要精确到小数点以后几位。 

     * @return 两个参数的商 

     */ 

    public static double div(double v1,double v2,int scale){ 

        if(scale<0){ 

            throw new IllegalArgumentException( 

                "The scale must be a positive integer or zero"); 

        } 

        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 

        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 

        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 

    } 

    /** 

     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 

     * 定精度，以后的数字按roundingMode指定的舍入模式处理。 

     * @param v1 被除数 

     * @param v2 除数 

     * @param scale 表示表示需要精确到小数点以后几位。 

     * @return 两个参数的商 

     */ 
    public static double div(double v1,double v2,int scale, int roundingMode){ 

        if(scale<0){ 
            throw new IllegalArgumentException( 

                "The scale must be a positive integer or zero");
        } 

        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 

        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 

        return b1.divide(b2,scale,roundingMode).doubleValue(); 

    } 

    /** 

     * 提供精确的小数位四舍五入处理。 

     * @param v 需要四舍五入的数字 

     * @param scale 小数点后保留几位 

     * @return 四舍五入后的结果 

     */ 
    public static double round(double v,int scale){ 

        if(scale<0){ 

            throw new IllegalArgumentException( 

                "The scale must be a positive integer or zero"); 

        } 

        BigDecimal b = new BigDecimal(Double.toString(v)); 

        BigDecimal one = new BigDecimal("1"); 

        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 

    } 
    
    /** 

     * 提供精确的小数位按指定舍入模式处理。 

     * @param v 需要舍入的数字 

     * @param scale 小数点后保留几位 

     * @return 舍入后的结果 

     */ 
    public static double round(double v,int scale,int roundingMode){ 
    	if(scale<0){ 

            throw new IllegalArgumentException( 

                "The scale must be a positive integer or zero"); 

        } 

        BigDecimal b = new BigDecimal(Double.toString(v)); 

        BigDecimal one = new BigDecimal("1"); 

        return b.divide(one,scale,roundingMode).doubleValue(); 
    } 
    
    /**
     * 提供百分比除法转换。百分比除以100后，默认保留4位小数，以后的数字按ROUND_DOWN舍入模式处理。
    
     * @param v 需要四舍五入的数字

     * @return 百分比除法后的结果
     */
    public static double percentDiv(double v){
    	return div(v, DB_HUNDRED, 4, BigDecimal.ROUND_DOWN);
    }
    
    /**
     * 提供百分比乘法转换。百分比除以100后，默认保留4位小数，以后的数字按ROUND_DOWN舍入模式处理。
    
     * @param v 需要四舍五入的数字

     * @return 百分比除法后的结果
     */
    public static double percentMul(double v){
    	return round(mul(v, DB_HUNDRED), 4, BigDecimal.ROUND_DOWN);
    }

    public static boolean equals(double v1, double v2){
    	BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
        return b1.equals(b2);
    }
    
    
    public static void main(String[] args) {
    	double d1 = ArithUtil.percentDiv(20);
    	System.out.println(d1);
    	double d2 = ArithUtil.percentMul(d1);
    	System.out.println(d2);
    	
    	double sum = ArithUtil.add(70.22,20.22,10.131);
    	System.out.println(sum);
    	
//    	Double s1 = 22.22;
//    	Double s2 = 22.22;
//    	System.out.println(s1.doubleValue() == s2.doubleValue());
    	
    	System.out.println(ArithUtil.equals(100.00, 100));
    	
    	
    	System.out.println(ArithUtil.mul(21, 100));
	}

}
