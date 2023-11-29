package pres.lnk.jxlss;

import org.junit.Test;
import pres.lnk.jxlss.demo.Employee;
import pres.lnk.jxlss.demo.Experience;

import java.util.*;

/**
 * 测试生成excel文件
 *
 * @Author lnk
 * @Date 2018/1/25
 */
public class TestJxls {

    @Test
    public void testJxls() throws Exception {
        String outPath = "D:/out_employee.xlsx";
        String imgRoot = TestJxls.class.getClassLoader().getResource("jxls_templates/dome").getPath();
        Employee emp = getEmployee();
        List<Experience> educationList = getEducationList();
        List<Experience> workList = getWorkList();
        JxlsBuilder jxlsBuilder = JxlsBuilder
                /* -- 加载模板方式 -- */
                //使用文件流加载模板
//                .getBuilder(inputStream)
                //使用文件加载模板
//                .getBuilder(file)
                //使用路径加载模板，可以是相对路径，也可以绝对路径
                .getBuilder("dome/employee.xlsx")

                /* -- 输出文件方式 -- */
                //指定输出的文件流
//                .out(outputStream)
                //指定输出的文件
//                .out(file)
                //指定输出的路径
                .out(outPath)

                /* 添加生成的数据 */
                .putVar("emp", emp)
                .putVar("educationList", educationList)
                .putVar("workList", workList)
                //设置图片路径的根目录
                .imageRoot(imgRoot)
                //设置如果图片缺失不终止生成
                .ignoreImageMiss(true)
                //添加自定工具类
//                .addFunction("jx", new JxlsUtil())
                .build();
        System.out.println("导出成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());
    }

    @Test
    public void testEach() throws Exception {
        String outPath = "D:/out_each.xlsx";
        List<Employee> list = getEmployees();
        JxlsBuilder jxlsBuilder = JxlsBuilder
                .getBuilder("dome/each.xlsx")
                .out(outPath)
                .ignoreImageMiss(true)
                .putVar("list", list)
                .build();
        System.out.println("导出成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());
    }

    @Test
    public void testMultisheet() throws Exception {
        String outPath = "D:/out_each_multisheet.xlsx";
        List<Employee> list = getEmployees();
        List<String> sheetNames = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            sheetNames.add(list.get(i).getName());
        }
        JxlsBuilder jxlsBuilder = JxlsBuilder
                .getBuilder("dome/each_multisheet.xlsx")
                .out(outPath)
                .ignoreImageMiss(true)
                .putVar("list", list)
                //生成多表格时的属性 multisheet 必需是 List<String> 类型
                .putVar("sheetNames", sheetNames)
                .removeSheet("Sheet")
                .build();
        System.out.println("导出成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());
    }

    @Test
    public void testIf() throws Exception {
        String outPath = "D:/out_else.xlsx";
        List<Object> list = new ArrayList<>();
        //导出else区域
        JxlsBuilder jxlsBuilder = JxlsBuilder
                .getBuilder("dome/if.xlsx")
                .out(outPath)
                .putVar("list", list)
                .build();
        System.out.println("导出else区域成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());

        outPath = "D:/out_if.xlsx";
        list.add(new Object());
        jxlsBuilder = JxlsBuilder
                .getBuilder("dome/if.xlsx")
                .out(outPath)
                .putVar("list", list)
                .build();

        System.out.println("导出if区域成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());
    }

    @Test
    public void testImage() throws Exception {
        String outPath = "D:/out_image.xlsx";
        String imgRoot = TestJxls.class.getClassLoader().getResource("jxls_templates").getPath();
        String imgPath = "dome/zhangsan.jpg";

        byte[] imageData = JxlsUtil.me().getImageData(imgRoot.concat("/").concat(imgPath));
        JxlsImage jxlsImage = JxlsUtil.me().getJxlsImage(imgRoot.concat("/").concat(imgPath));

        JxlsBuilder jxlsBuilder = JxlsBuilder
                .getBuilder("dome/image.xlsx")
                .out(outPath)
                .imageRoot(imgRoot)
                .putVar("imageStr", imgPath)
                .putVar("imageData", imageData)
                .putVar("jxlsImage", jxlsImage)
                .build();

        System.out.println("导出成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());
    }

    @Test
    public void testGrid() throws Exception {
        String outPath = "D:/out_grid.xlsx";

        //列名集合
        String[] headers = {"姓名", "民族", "籍贯", "住址", "联系电话"};
        //数据
        List<Employee> list = getEmployees();
        //对应数据的字段名
        String[] props = {"name", "nation", "nativePlace", "address", "phone"};

        JxlsBuilder jxlsBuilder = JxlsBuilder
                .getBuilder("dome/grid.xlsx")
                .out(outPath)
                .putVar("headers", Arrays.asList(headers))
                .putVar("props", props)
                .putVar("list", list)
                .build();

        System.out.println("导出成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());
    }

    @Test
    public void testMerge() throws Exception {
        String outPath = "D:/out_merge.xlsx";

        List<Map<String, Object>> companies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> company = new HashMap<>();
            company.put("company", "公司" + i);
            List<Employee> list = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                list.addAll(getEmployees());
            }
            company.put("employees", list);
            companies.add(company);
        }

        JxlsBuilder jxlsBuilder = JxlsBuilder
                .getBuilder("dome/merge.xlsx")
                .out(outPath)
                .putVar("companies", companies)
                .build();

        System.out.println("导出成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());
    }

    private static Employee getEmployee() {
        return new Employee();
    }

    private static List<Experience> getEducationList() {
        List<Experience> list = new ArrayList<>();
        list.add(new Experience("xxx大学"));
        list.add(new Experience("xxx高中"));
        list.add(new Experience("xxx初中"));
        return list;
    }


    private static List<Experience> getWorkList() {
        List<Experience> list = new ArrayList<>();
        return list;
    }

    private static List<Employee> getEmployees() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee());
        Employee e = new Employee();
        e.setName("李四");
        list.add(e);
        e = new Employee();
        e.setName("王五");
        list.add(e);
        return list;
    }
}
