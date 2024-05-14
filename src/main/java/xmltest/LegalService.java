package xmltest;

import cn.hutool.core.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.ajax.JSON;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class LegalService {
    static List<LegalModel> getLegalEvents(Document document, String openNumber) {
        if (openNumber.startsWith("CN")) {
            return LegalService_CN.getLegalEvents_cn(document, openNumber);
        }
        if (openNumber.startsWith("US")) {
            //return getLegalEvents_us(document, openNumber);
        }
        throw new RuntimeException("not support country");
    }


}
