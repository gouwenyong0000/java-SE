package com.g;

import java.io.File;
import java.util.Arrays;

public class Tool {
    public static void main(String[] args) {
        int[] arr = {1,5,-10,60,-70};

        merge(arr, 0, 1, 1);
        meagerSort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }

    private static void meagerSort(int[] arr, int start, int end) {

        if (end - start == 0) {
            return;
        }

        int mid = (start + end) / 2;

        meagerSort(arr, start, mid);
        meagerSort(arr, mid + 1, end);

        merge(arr, start, mid + 1, end);

    }

    /**
     * 将两个有序的数组进行合并
     *
     * @param arr   数组
     * @param start 开始位置
     * @param mid   右边起点
     * @param end   结束位置
     */
    private static void merge(int[] arr, int start, int mid, int end) {

        int lift = start;
        int right = mid;
        int i = 0;
        int[] temp = new int[end - start + 1];

        while (lift < mid && right <= end) {

//            if (arr[lift] <= arr[right]) {
//                temp[i++] = arr[lift++];
//            } else {
//                temp[i++] = arr[right++];
//            }
            //上面代码简化成如下代码
            temp[i++] = arr[lift] <= arr[right] ? arr[lift++] : arr[right++];

        }

        while (lift < mid) {
            temp[i++] = arr[lift++];
        }
        while (right <= end) {
            temp[i++] = arr[right++];
        }

        i = 0;
        for (int j = start; j <= end; j++) {
            arr[j] = temp[i++];
        }

    }

    /**
     * 对尚硅谷一系列文件进行重名名
     */
    private static void renameFile() {
        File path = new File("F:\\【Java】\\【Java】尚硅谷\\4.尚硅谷全套JAVA教程--JavaEE高级（44.51GB）\\☆☆☆尚硅谷Netty核心技术教程\\视频");
        File[] files = path.listFiles();

        for (File file : files) {
            String name = file.getName();
            String newName = name.replaceAll("尚硅谷-Netty核心技术及源码剖析-", "");
            System.out.println(name);
            System.out.println(newName);
            String path1 = file.getParent();
            System.out.println(path1);
            File dest = new File(path1 + "\\" + newName);
            file.renameTo(dest);
        }
    }

}
