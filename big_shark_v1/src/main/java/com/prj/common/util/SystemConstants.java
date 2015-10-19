package com.prj.common.util;

public class SystemConstants {

    private SystemConstants() {
    }

    public static final String UPLOAD_PATH         = "\\file\\img\\icon";

    public static final long   MSG_EXPIRE_INTERVAL = 5 * 60 * 1000;

    public static final String ATTACHMENT_PATH     = "\\file\\project\\attachment";

    public static final String EQUITY_FILE_PATH    = "\\file\\project\\equity";

    public static final int    MAX_ATTACH_COUNT    = 5;

    public static final long   MAX_ATTACH_SIZE     = 5 * 1024 * 1024;

    public static final String EMAIL_TPL_WAIT      = "%s:</br>"
                                                     + "&nbsp;&nbsp;&nbsp;&nbsp; 您好！我是InnoSpace的投资经理。</br>"
                                                     + "&nbsp;&nbsp;&nbsp;&nbsp; 我与同事已经对您提交的项目进行了详细的评估，但是在部分细节问题上还需要做进一步的讨论，并在此基础上给出我们最终的答复与安排。</br>"
                                                     + "&nbsp;&nbsp;&nbsp;&nbsp; 在我们得出结论之后，会第一时间与您以及您的团队取得联系，请再耐心等待一段时间，万望海涵！</br>"
                                                     + "&nbsp;&nbsp;&nbsp;&nbsp; 祝，一切顺利！</br>"
                                                     + "Innospace官方团队</br>";
    public static final String EMAIL_TPL_REJECT    = "%s:</br>"
                                                     + "&nbsp;&nbsp;&nbsp;&nbsp; 您好！我是InnoSpace的投资经理。</br>"
                                                     + "&nbsp;&nbsp;&nbsp;&nbsp; 在对您的项目进行了详细的分析考察之后，我与几位同事也进行了多次的讨论与沟通。虽然您的项目具有相应的创新性与可操作性，但是从目前来讲，贵项目与我们的计划及资源之间并没有那么的匹配。</br>"
                                                     + "&nbsp;&nbsp;&nbsp;&nbsp; 故而很遗憾的通知您，您的项目并不适合我们现在的投资计划。当然，我们也期待未来会有其他的合作机会，届时可以再做进一步的沟通。</br>"
                                                     + "&nbsp;&nbsp;&nbsp;&nbsp; 祝，一切顺利！</br>"
                                                     + "Innospace官方团队</br>";

}
