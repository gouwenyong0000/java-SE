package pres.lnk.jxlss.command;

import org.apache.commons.lang3.StringUtils;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;

/**
 * @Author lnk
 * @Date 2018/4/26
 */
public class GridCommand extends org.jxls.command.GridCommand {

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        String props = getProps();
        if(!StringUtils.contains(props, ",")){
            Object value = getTransformationConfig().getExpressionEvaluator().evaluate(props, context.toMap());
            if(value instanceof String){
                props = (String) value;
            } else if (value instanceof String[]){
                props = StringUtils.join((String[]) value, ",");
            } else {
                throw new IllegalArgumentException("props 属性只支持 String 和 String[]");
            }
            setProps(props);
        }
        return super.applyAt(cellRef, context);
    }
}
