import org.xmind.core.*;
import org.xmind.core.io.ByteArrayStorage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmindUtil {

    public static void main(String[] args) {
        String path = "/Users/mac/Documents/8.xmind";
        IWorkbookBuilder builder = Core.getWorkbookBuilder();// 初始化builder
        IWorkbook workbook = null;
        try {
            // 打开XMind文件
            workbook = builder.loadFromFile(new File(path),new ByteArrayStorage(), null);
//            workbook = builder.loadFromPath(path,new ByteArrayStorage(), null);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        ISheet defSheet = workbook.getPrimarySheet();// 获取主Sheet
        ITopic rootTopic = defSheet.getRootTopic(); // 获取根Topic
        String className = rootTopic.getTitleText();//节点TitleText
        List<ITopic> allChildren = rootTopic.getAllChildren();//获取所有子节点
        for (ITopic it:allChildren
             ) {
            for (ITopic iTopic:it.getAllChildren()
                 ) {
                System.out.println("["+className+"]["+it.getTitleText()+"]"+iTopic.getTitleText());
                for (ITopic iTopic1:iTopic.getAllChildren()
                     ) {
                    System.out.println(iTopic1.getTitleText()+":");
                    for (ITopic iTopic2:iTopic1.getAllChildren()
                         ) {
                        System.out.println(iTopic2.getTitleText());
                    }
                }
            }

        }

    }
}
