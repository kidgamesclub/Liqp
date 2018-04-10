package liqp.tags;

import java.io.File;
import liqp.Template;
import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

public class Include extends Tag {

  public static final String INCLUDES_DIRECTORY_KEY = "liqp@includes_directory";
  public static String DEFAULT_EXTENSION = ".liquid";

  @Override
  public Object render(RenderContext context, LNode... nodes) {

    boolean entered = false;
    try {
      String includeResource = super.asString(nodes[0].render(context));
      String extension = DEFAULT_EXTENSION;
      if (includeResource.indexOf('.') > 0) {
        extension = "";
      }
      File includeResourceFile;
      File includesDirectory = (File) context.get(INCLUDES_DIRECTORY_KEY);
      if (includesDirectory != null) {
        includeResourceFile = new File(includesDirectory, includeResource + extension);
      } else {
        includeResourceFile = new File(context.getTemplateFactory().getFlavor().snippetsFolderName, includeResource +
              extension);
      }
      Template template = context.getTemplateFactory().parseFile(includeResourceFile);

      // check if there's a optional "with expression"
      if (nodes.length > 1) {
        Object value = nodes[1].render(context);
        context.set(includeResource, value);
      }

      context.addFrame();
      entered = true;

      return context.render(template);
    } catch (Exception e) {
      return "";
    } finally {
      if (entered) {
        context.popFrame();
      }
    }
  }
}
