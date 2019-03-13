package com.weqia.utils;

import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * URL提取规则
     */
    public static final String REGEX_URL = "(http:\\/\\/|https:\\/\\/)((\\w|=|\\?|\\:|\\;|\\#|\\.|\\/|&|-)+)";
    /**
     * weqia号规则
     */
    public static final String REGEX_WEQIA = "^[a-zA-Z][\\w*|-]{5,19}";

    /**
     * 符号表情
     */
    public static final String REGEX_EXPRESSION = "f0[0-9]{2}|f10[0-7]";
    /**
     * @xxx
     */
    public static final String REGEX_PEOPLE = "@[^\\@\\s]+\\s";
    /**
     * @##
     */
    public static final String REGEX_TOPIC = "#[^#]*#";

    /**
     * 检验整数,适用于正整数、负整数、0,负整数不能以-0开头, 正整数不能以0开头
     */
    public static final String REGEX_INTEGER = "^(-?)[1-9]+\\d*|0";

    /**
     * 手机号码验证,11位,不知道详细的手机号码段,只是验证开头必须是1和位数
     */
    public static final String REGEX_PHONENUM = "^[1][\\d]{10}";

    /**
     * 检验空白符
     */
    public static final String REGEX_BLANK = "(\\s|\\t|\\r)+";

    /**
     * 检查EMAIL地址 用户名和网站名称必须>=1位字符
     * 地址结尾必须是以com|cn|com|cn|net|org|gov|gov.cn|edu|edu.cn结尾
     */
    public static final String REGEX_EMAIL = "\\w+\\@\\w+\\.(com|cn|com.cn|net|org|gov|gov.cn|edu|edu.cn)";

    /**
     * 检查EMAIL地址 用户名和网站名称必须>=1位字符 地址结尾必须是2位以上,如：cn,test,com,info
     */
    public static final String REGEX_EMAIL_OTHER = "\\w+\\@\\w+\\.\\w{2,}";

    /**
     * 检查邮政编码(中国),6位,第一位必须是非0开头,其他5位数字为0-9
     */
    public static final String REGEX_POST_CODE = "^[1-9]\\d{5}";

    /**
     * 检验用户名 取值范围为a-z,A-Z,0-9,"_",汉字,不能以"_"结尾 用户名有最小长度和最大长度限制,比如用户名必须是4-20位
     */
    public static final String REGEX_USER_NAME = "[\\w\u4e00-\u9fa5]{6,16}(?<!_)";

    /**
     * 检验用户名 取值范围为a-z,A-Z,0-9,"_",汉字 最少一位字符,最大字符位数无限制,不能以"_"结尾
     */
    public static final String REGEX_USERNAME_LIMIT_ONE_CHINESE = "[\\w\u4e00-\u9fa5]+(?<!_)";

    public static final String REGEX_NICKNAME = "^([\u4E00-\u9FA5]|[a-zA-Z0-9_]{2}){2,6}$";

    /**
     * 查看IP地址是否合法
     */
    public static final String REGEX_IP = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\."
            + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\."
            + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\."
            + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";

    /**
     * 验证国内电话号码 格式：010-67676767,区号长度3-4位,必须以"0"开头,号码是7-8位
     */
    public static final String REGEX_FIX_TELEPHONE_NUM = "^[0]\\d{2,3}\\-\\d{7,8}";

    /**
     * 验证国内电话号码 格式：6767676, 号码位数必须是7-8位,头一位不能是"0"
     */
    public static final String REGEX_FIX_TELEPHONE_NUM_WITHOUT_CODE = "^[1-9]\\d{6,7}";

    /**
     * 验证国内身份证号码：15或18位,由数字组成,不能以0开头
     */
    public static final String REGEX_ID_CARD = "^[1-9](\\d{14}|\\d{17})";

    /**
     * 邮箱的正则表达式
     */
    public static final String EMAIL = "^[a-z0-9_\\-]+(\\.[_a-z0-9\\-]+)*@([_a-z0-9\\-]+\\.)+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)$";
    /**
     * 手机号的正则表达式
     */
    public static final String MOBILE = "^1[3458][0-9]{9}$";
    /**
     * IPv4的正则表达式
     */
    public static final String IP_V4 = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    /**
     * URL的正则表达式
     */
    public static final String URL = "^([a-z]+:\\/\\/)?([a-z]([a-z0-9\\-]*\\.)+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(:[0-9]{1,5})?(\\/[a-z0-9_\\-\\.~]+)*(\\/([a-z0-9_\\-\\.]*)(\\?[a-z0-9+_\\-\\.%=&amp;]*)?)?(#[a-z][a-z0-9_]*)?$";
    /**
     * 中文字符的正则表达式
     */
    public static final String CN = "^[\u4e00-\u9fa5]+$";

    /**
     * 会员帐号的正则表达式
     */
    public static final String MEMBER_ACCOUNT = "^[a-zA-Z0-9_]{5,20}$";

    /**
     * 电话的正则表达式
     */
    public static final String TEL_NUMBER = "(^[0-9]{3,4}[\\-]{0,1}[0-9]{6,9}[\\-]{0,1}[0-9]{0,5}$)";
    /**
     * 真实姓名的正则表达式
     */
    public static final String TRUE_NAME = "^[\u4e00-\u9fa5]{1,16}$";
    /**
     * 联系地址的正则表达式
     */
    public static final String LINK_ADDRESS = "^.{1,250}$";
    /**
     * qq号码
     */
    public static final String QQ = "^[0-9]{5,20}$";
    /**
     * 邮编
     */
    public static final String ZIP_CODE = "^[0-9]{6}$";
    /**
     * 银行卡号
     */
    public static final String BANK_ACCOUNT_NO = "^[0-9]{16,25}$";
    /**
     * 两位小数
     */
    public static final String TWO_POINT_FLOAT = "^([1-9][0-9]*|0)(\\.[0-9]{0,2})?$";
    /**
     * 两位正整数
     */
    public static final String TWO_BIT_INTEGER = "^[1-9]([0-9])?$";

    /**
     * 手机或邮箱
     */
    public static final String MOBLIE_OR_EMAIL = "(^1[0-9]{10}$)|(^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$)";

    /**
     * 中文字符-标题
     */
    public static final String TITLE_CN = "^[\\u4e00-\\u9fa5a-zA-Z_0-9]{1,100}$";

    /**
     * 会员名称 - 名字
     */
    public static final String MEMBER_NAME = "^[\\u4e00-\\u9fa5a-zA-Z_0-9. -]{2,20}$";

    /**
     * 企业名称
     */
    public static final String COMPANY_NAME = "^[\\u4e00-\\u9fa5a-zA-Z_0-9]{2,20}$";

    /**
     * 企业全称
     */
    public static final String COMPANY_FULL_NAME = "^[\\u4e00-\\u9fa5a-zA-Z_0-9]{2,128}$";

    /**
     * 2位正整数和2位小数
     */
    public static final String POSITIVE_INTEGER = "^([1-9][0-9]|[1-9]|0)(\\.[0-9]{0,2})?$";

    /**
     * 用户wq号的正则表达式
     */
    public static final String WEQIA_NO = "^(?!\\d+$)\\w{6,20}$";

    /**
     * 企业wq号的正则表达式
     */
    public static final String CO_WEQIA_NO = "^(?!\\d+$)\\w{4,50}$";

    /**
     * 6位数字验证码
     */
    public static final String VALID_NO = "^[0-9]{6}$";

    /**
     * 正整数
     */
    public static final String INTEGER_NUM = "^[0-9]*[1-9][0-9]*$";

    // http://www.163.com
    // public static final String REGEX_WEB_SITE =
    // "([/w-]+/.)+[/w-]+/.([^a-z])(/[/w- ./?%&=]*)?|[a-zA-Z0-9/-/.][/w-]+.([^a-z])(/[/w- ./?%&=]*)? ";

    public static boolean matchString(String from, String regex) {
        if (from != null && regex != null) {
            return Pattern.matches(regex, from);
        }
        return false;
    }

}
