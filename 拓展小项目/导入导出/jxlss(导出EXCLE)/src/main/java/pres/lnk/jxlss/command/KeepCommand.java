package pres.lnk.jxlss.command;

import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;

/**
 * <p>保持单元格样式</p>
 * jx:keep(lastCell="结束单元格")<br>
 * 如果生成的单元格样式被改变了，可以试用该指定keep恢复
 * @Author lnk
 * @Date 2018/1/23
 */
public class KeepCommand extends AbstractCommand {

    private Area area;

    public KeepCommand() {
    }

    @Override
    public Command addArea(Area area) {
        if( super.getAreaList().size() >= 1){
            throw new IllegalArgumentException("You can add only a single area to 'keep' command");
        }
        this.area = area;
        return super.addArea(area);
    }

    @Override
    public String getName() {
        return "keep";
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        if( area == null ){
            throw new IllegalArgumentException("No area is defined for keep command");
        }
        //恢复原有的样式
        area.applyAt(cellRef, context);
        return area.getSize();
    }
}
