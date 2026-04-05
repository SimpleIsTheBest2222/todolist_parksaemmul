import org.h2.tools.Server;

public class H2Server {
    public static void main(String[] args) throws Exception {
        // H2 DB UI 로 보는 환경설정
        Server.createWebServer(
                "-web",
                "-webAllowOthers",
                "-webPort", "8082"
        ).start();

        System.out.println("H2 DB 연결 완료");
    }
}