package pres.testimage;

import pres.lnk.jxlss.JxlsBuilder;
import pres.lnk.jxlss.JxlsImage;
import pres.lnk.jxlss.JxlsUtil;
import pres.testmerag.CompanyPojo;
import pres.testmerag.TestMerge;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * 测试打出图片
 */
public class TestImag {
    public static void main(String[] args) throws Exception {




        OutputStream out = new FileOutputStream("C:/Users/gouwenyong0000/Desktop" + "/demo.xlsx");
        InputStream in = TestMerge.class.getResourceAsStream("/templates/image.xlsx");
        String classPath1 = TestImag.class.getResource("/").getPath();
        String classPath = classPath1.substring(1);
        String inamgPath = classPath + "templates/zhangsan.jpg";


        Map<String, Object> map = new HashMap<>();

        //imageStr
        map.put("imageStr", inamgPath);


        //imageData(传入byte[])

        JxlsImage jxlsImage1 = JxlsUtil.me().getJxlsImage(inamgPath);
        byte[] imageData = jxlsImage1.getPictureData();

        map.put("imageData", imageData);

        //jxlsImage

        map.put("jxlsImage", jxlsImage1);

        JxlsBuilder
                /* -- 加载模板方式 -- */
                // 使用文件流加载模板
                // .getBuilder(inputStream)
                // 使用文件加载模板
                // .getBuilder(file)
                // 使用路径加载模板，可以是相对路径，也可以绝对路径
                .getBuilder(in)

                /* -- 输出文件方式 -- */
                // 指定输出的文件流
                // .out(outputStream)
                // 指定输出的文件
                // .out(file)
                // 指定输出的路径
                .out(out)

                /* 添加生成的数据 */
                .putAll(map)
                // 设置图片路径的根目录
                // .imageRoot("imgRoot")
                // 设置如果图片缺失不抛异常继续生成文件
                .ignoreImageMiss(true)
                // 添加自定工具类
                // .addFunction("jx", new JxlsUtil())
                .build();
        System.out.println("end========================");
    }


}
