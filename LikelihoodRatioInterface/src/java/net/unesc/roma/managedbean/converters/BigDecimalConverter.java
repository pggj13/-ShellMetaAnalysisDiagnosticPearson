package net.unesc.roma.managedbean.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter("bigDecimalConverter")
public class BigDecimalConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {

        return new BigDecimal(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        StringBuilder sb = new StringBuilder("#0.");
        BigDecimal bigDecimal = (BigDecimal) value;

        int scale = bigDecimal.scale();

        int i = 0;
        do {
            sb.append(i == 0 ? "0" : "#");
            i++;
        } while (i < scale);
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(value);
    }
}
