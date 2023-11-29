> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [blog.csdn.net](https://blog.csdn.net/m0_37727363/article/details/105823518)

核心基本命令：

```bash
#C:\Program Files\Beyond Compare 4\BCompare.exe @脚本文件绝对路径 日志文件PATH 比较结果PATH 右文件PATH 左文件PATH
C:\Program Files\Beyond Compare 4\BCompare.exe @G:\bcscript.txt G:\log.txt G:\result.html G:\file1.txt G:\file2.txt
```

脚本文件例：

```bat
log normal %1
criteria rules-based
load <default>
 
text-report layout:side-by-side options:display-mismatches,ignore-unimportant,line-numbers output-to:"%2" output-options:html-color %3 %4
```

使用 java 调用 BCompare.exe:

```java
public static void main(String[] args){
        String[] params = new String[] {"C:\\Program Files\\Beyond Compare 4\\BCompare.exe", 
                "@G:\\CompareTool\\resource\\bcscript.txt",
                "E:\\SVN\\05_ツール転換後ソース\\CreateUrlRequestDTO.ts",
                "E:\\SVN\\01_ソース\\CreateUrlRequestDTO.ts",
                "G:\\CompareTool\\CompareResult\\html\\CreateUrlRequestDTO.html",
                "G:\\CompareTool\\CompareResult\\html\\BCompare_log.txt"
        };
        int returnCD = CompareUtil.run(params);
        if (returnCD != 0) {
            System.err.println("[returnCD]:" + returnCD);
            return;
        }
    }
```

```java
public static int run(String ... array){
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(array);
            Process process = builder.start();
            // Process process = Runtime.getRuntime().exec("C:\\Program Files\\Beyond Compare 4\\BCompare.exe @G:\\CompareTool\\resource\\bcscript.txt");
            InputStream inSTest = process.getErrorStream();
            InputStreamReader reader = new InputStreamReader(inSTest);
            BufferedReader bfReader = new BufferedReader(reader);
 
            String strLine = "";
            while ((strLine = bfReader.readLine()) != null) {
                System.out.println(strLine);
            }
 
            bfReader.close();
            process.waitFor();
            process.destroy();
 
            return process.exitValue();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return - 10000;
    }
```