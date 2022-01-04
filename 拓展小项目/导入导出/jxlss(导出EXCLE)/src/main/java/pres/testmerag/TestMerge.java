package pres.testmerag;

import pres.lnk.jxlss.JxlsBuilder;
import pres.lnk.jxlss.JxlsUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 测试合并单元格
 */
public class TestMerge {

    public static void main(String[] args) throws Exception {

        OutputStream outPath = new FileOutputStream("C:/Users/gouwenyong0000/Desktop" + "/demo.xlsx");

        Map<String, List<CompanyPojo>> data = getData();//数据
        System.out.println(data);
        InputStream in = TestMerge.class.getResourceAsStream("/templates/merge.xlsx");


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
                .out(outPath)

                /* 添加生成的数据 */
                .putVar("companies", data.get("companies"))
                // 设置图片路径的根目录
                .imageRoot("imgRoot")
                // 设置如果图片缺失不抛异常继续生成文件
                .ignoreImageMiss(true)
                // 添加自定工具类
                .addFunction("jx", JxlsUtil.me())
                .build();


        System.out.println("end=======");
    }

    public static Map<String, List<CompanyPojo>> getData() {

        Map<String, List<CompanyPojo>> data = new HashMap<>();

        List<CompanyPojo> list = new LinkedList<>();

        for (int i = 0; i < 5; i++) {

            LinkedList<PersonPojo> temp = new LinkedList<>();
            int j = new Random().nextInt(9) + 1;
            for (int i1 = 0; i1 < j; i1++) {
                ArrayList<String> addrList = new ArrayList<>();
                addrList.add("地址1");
                addrList.add("地址2");
                addrList.add("地址3");
                PersonPojo personPojo = new PersonPojo();
                personPojo.setName("姓名" + j);
                personPojo.setAddress(addrList);
                personPojo.setNativePlace("省份" + j);
                personPojo.setPhone("电话" + j);
                temp.add(personPojo);
            }


            CompanyPojo companyPojo = new CompanyPojo("公司" + i + "---", temp);
            list.add(companyPojo);
        }

        data.put("companies", list);
        return data;
    }
}
