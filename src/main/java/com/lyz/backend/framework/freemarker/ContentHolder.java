package com.lyz.backend.framework.freemarker;


import com.lyz.backend.framework.util.ThreadLocalContext;

public class ContentHolder {
    public static final String SCRIPT = "SCRIPT";
    public static final String SCRIPT_RESOURCE = "SCRIPT_RESOURCE";

    public static void pushScript(String content) {
        push(SCRIPT, content);
    }

    public static String renderScript() {
        return render(SCRIPT);
    }

    public static void push(String queue, String content) {
        StringBuilder list = (StringBuilder) ThreadLocalContext.get(queue);
        if (list == null) {
            list = new StringBuilder();
            ThreadLocalContext.put(queue, list);
        }
        list.append(content);
    }

    public static String render(String queue) {
        try {
            StringBuilder list = (StringBuilder) ThreadLocalContext.get(queue);
            if (list == null) {
                return null;
            } else {
                return list.toString();
            }
        } finally {
            ThreadLocalContext.remove(queue);
        }
    }
}
