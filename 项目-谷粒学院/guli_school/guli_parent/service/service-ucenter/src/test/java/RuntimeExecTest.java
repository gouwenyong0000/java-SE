import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RuntimeExecTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        test();
    }

    private static void test() {


//        String[] cmd = new String[]{"shutdown" ,"-s" ,"-t" ,"3600"};//定时关机
        String[] cmd = new String[]{"cmd" ,"-s" ,"-t" ,"3600"};//定时关机

//        String[] cmd = new String[]{"cmd.exe", "/C", "wmic process get name"};

        // 输出aaa到1.txt  然后 使用记事本打开该文件
        String command = "cmd /c echo aaa >> d:\\1.txt && notepad d:\\1.txt";// && 命令之间需连接符连接
        try {
            Process process = Runtime.getRuntime().exec(command);
            new Thread(new SerializeTask(process.getInputStream())).start();
            new Thread(new SerializeTask(process.getErrorStream())).start();
            process.getOutputStream().close();
            int exitValue = process.waitFor();
            System.out.println("返回值：" + exitValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * 打印输出线程
 */
class SerializeTask implements Runnable {
    private InputStream in;

    public SerializeTask(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}