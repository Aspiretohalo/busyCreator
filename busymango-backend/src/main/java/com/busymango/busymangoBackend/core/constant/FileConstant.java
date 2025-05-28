package com.busymango.busymangoBackend.core.constant;

/**
 * 文件常量
 *
 * @author caoyanghalo@qq.com
 */
public interface FileConstant {

    /**
     * COS 访问地址
     * todo 需替换配置
     */
    String COS_HOST = "";
    double EMU_TO_INCH = 914400.0;

    /**
     * word尺寸单位emu到pt的比例,也就是word尺寸，需要将其转化成真实文档对应的html尺寸
     */
    double RATIO_WORD_HTML = convertWordToHtml();

    static double convertWordToHtml() {
        // 获取到的a4尺寸对应的emu
        final long emuValue = 16838;
        // 1英寸 = 914400 EMU
        // 将EMU转换为英寸
        double inchesFromEmu = (double) emuValue / EMU_TO_INCH;
        // A4的实际尺寸（英寸）
        final double actualA4HeightInInches = 11.69;
        // 计算比例
        double ratio = actualA4HeightInInches / inchesFromEmu;

        return ratio;
    }
}
