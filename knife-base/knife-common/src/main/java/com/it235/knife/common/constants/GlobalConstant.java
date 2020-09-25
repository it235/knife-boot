package com.it235.knife.common.constants;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 22:14
 */
public interface GlobalConstant {

    /**
     * 微服务之间传递的唯一标识
     */
    String KNIFE_TRACE_ID = "knife-trace-id";

    /**
     * 日志链路追踪id日志标志
     */
    String LOG_TRACE_ID = "traceId";


    String TRACE_ID_HEADER = "x-trace-header";

    String USER_AGENT_HEADER = "user-agent";

    /**
     * Java默认临时目录
     */
    String JAVA_TEMP_DIR = "java.io.tmpdir";

    /**
     * 版本
     */
    String VERSION = "version";

    /**
     * 灰度发布版本控制
     */
    String GRAY_VERSION = "grayversion";
}
