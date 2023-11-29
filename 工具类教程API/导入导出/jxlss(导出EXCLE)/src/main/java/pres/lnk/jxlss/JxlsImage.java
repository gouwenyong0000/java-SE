package pres.lnk.jxlss;

import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.ImageType;

/**
 * <p>插入excel图片处理对象</p>
 * 使用 {@link JxlsUtil#getJxlsImage(String)} 获取
 * @Author lnk
 * @Date 2018/1/24
 */
public class JxlsImage {
    public static final String IMAGE_SIZE_TYPE_AUTO = "auto";
    public static final String IMAGE_SIZE_TYPE_ORIGINAL = "original";
    private byte[] pictureData;
    private String pictureType;

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public int getWorkbookImageType() {
        switch (pictureType.toUpperCase()) {
            case "PNG":
                return Workbook.PICTURE_TYPE_PNG;
            case "EMF":
                return Workbook.PICTURE_TYPE_EMF;
            case "WMF":
                return Workbook.PICTURE_TYPE_WMF;
            case "PICT":
                return Workbook.PICTURE_TYPE_PICT;
            case "DIB":
                return Workbook.PICTURE_TYPE_DIB;
            default:
                return Workbook.PICTURE_TYPE_JPEG;
        }
    }

    public ImageType getJxlsImageType() {
        if ("jpg" .equalsIgnoreCase(pictureType)) {
            return ImageType.JPEG;
        }
        return ImageType.valueOf(pictureType);
    }
}
