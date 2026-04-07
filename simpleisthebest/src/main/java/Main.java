import javax.swing.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // 스캐너로 입력받음
        Scanner sc = new Scanner(System.in);
        // 계층 구조를 유지하기 위해서 Main -> service 흐름으로 감
        TodoService service = new TodoServiceImpl();
        // main 서버를 계속 실행시키지 않기 위해 사용함
        while (true) {
            // 조회 ui 작성
            System.out.println("[ To-Do List Manager ]");
            System.out.println("1. 할 일 목록 보기");
            System.out.println("2. 새 할 일 추가");
            System.out.println("3. 할 일 수정");
            System.out.println("4. 할 일 삭제");
            System.out.println("5. 검색");
            System.out.println("6. 종료");
            System.out.print("선택 > ");

            int menu = sc.nextInt();
            // 버퍼에 남아있는 엔터제거. 예를들면 1 + 공백 있으면 1만 읽고 넘어가는거 방지하기 위함.
            sc.nextLine();

            if (menu == 1) {
                // list 변수 정의해서 service 단에 구현한 searchTodolist 메소드를 타기 위함.
                List<TodoVO> list = service.searchTodolist();

                System.out.println("=============================================================================================");
                System.out.println(" ID |   상태   | 우선순위 |      생성 일시     |  할 일 내용");
                System.out.println("---------------------------------------------------------------------------------------------");

                // vo 에 담긴거를 list 에 하나하나씩 가져오는 작업.
                for (TodoVO vo : list) {
                    System.out.println(
                            // 직접 컬럼명을 지정해줘서 가져옴
                            vo.getId() + " ㅣ " + vo.getStatus() + " ㅣ " + vo.getPriority() + " ㅣ " + vo.getCreatedTime() + " ㅣ " + vo.getTask()
                    );
                }

                // list 변수에 담긴 데이터 총 갯수를 읽음
                System.out.println("---------------------------------------------------------------------------------------------");
                System.out.println("[ 총 " + list.size() + "개의 항목이 있습니다. ]");
                System.out.println("=============================================================================================");

            } else if (menu == 2) {
                /*
                 * 새 할 일 추가
                 * */
                System.out.println("[ 새 할 일 추가 ]");
                System.out.println("----------------------------------");

                // 할일 선언
                System.out.print("> 할 일 내용 입력: ");
                // nextLine = 엔터까지 입력한 내용을 한 줄 전체로 가져옴
                String task = sc.nextLine();


                // 우선순위 선언
                System.out.print("> 우선순위 입력 (숫자 입력): ");
                // nextInt = 엔터까지 입력한 숫자를 한 줄 전체로 가져옴
                int priority = sc.nextInt();
                // 버퍼 비워야됨. int 는 숫자만 읽고 엔터가 안됨.
                sc.nextLine();

                /*
                요구사항 정의서에 상태 입력에 대한 요구 없으므로 주석처리함.
                *
                // 상태 선언
                System.out.print("> 상태 입력(1:시작전, 2:진행중, 3:완료) : ");
                // nextLine = 엔터까지 입력한 내용을 한 줄 전체로 가져옴
                String status = sc.nextLine();
                */

                // 생성시간 넣을 함수 선언
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                // 빈 객체 하나 만들고
                TodoVO vo = new TodoVO();
                // 사용자가 입력한 값을 vo에 담음
                vo.setTask(task);
                vo.setPriority(priority);
                //  요구사항 정의서에 상태 입력에 대한 요구 없으므로 주석처리함. insert 시 null 으로 db 들어감.
                //vo.setStatus(status);
                vo.setCreatedTime(currentTime);

                // 서비스단에 createTodolist 메소드 하나 추가해서 사용자가 입력한 값을 담아서 service 단으로 넘겨줌
                service.createTodolist(vo);

                System.out.println("----------------------------------");
                System.out.println("[시스템] \"" + task + "\" 항목이 추가되었습니다!");
                System.out.println("(생성 시간: " + currentTime + ")");
                System.out.println("----------------------------------");

            } else if (menu == 3) {
                /*
                 * 할 일 수정
                 * */
                System.out.println("[할일 수정]");
                System.out.println("---------------------------------------------------------");
                System.out.println("수정할 항목의 ID를 입력하세요.");
                // id 변수에 스캐너로 숫자 입력받음
                int id = sc.nextInt();
                // 버퍼 비우는 용도. 1 + 엔터치면 공백이 다음줄에서 읽히지 않게하기 위함.
                sc.nextLine();
                // 현재 DB 에 저장되어 있는 id 검색하는 메소드임.
                TodoVO currentVo = service.getTodolistById(id);

                if (currentVo == null) {
                    System.out.println("[시스템] 해당 ID의 항목이 없습니다.]");
                    System.out.println("---------------------------------------------------------");
                } else {
                    System.out.println();
                    System.out.println("[현재 데이터]");
                    System.out.println("- 내용: " + currentVo.getTask());
                    System.out.println("- 상태: " + currentVo.getStatus());
                    System.out.println("- 우선순위: " + currentVo.getPriority());
                    System.out.println();

                    System.out.println("[변경 정보 입력]");
                    System.out.print("1. 내용 수정: ");
                    // 할일 수정
                    String task = sc.nextLine();

                    System.out.print("2. 상태 변경 (1:시작전, 2:진행중, 3:완료): ");
                    int status = sc.nextInt();
                    sc.nextLine();

                    String statusStr = "";
                    if (status == 1) {
                        statusStr = "시작전";
                    } else if (status == 2) {
                        statusStr = "진행중";
                    } else if (status == 3) {
                        statusStr = "완료";
                    }

                    // 우선순위 부분
                    System.out.print("3. 우선순위 변경 (숫자 입력): ");
                    int priority = sc.nextInt();
                    // 버퍼 비움
                    sc.nextLine();

                    // 수정된 객체를 담음.
                    TodoVO updatedVO = new TodoVO();
                    updatedVO.setId(id);
                    updatedVO.setTask(task);
                    updatedVO.setStatus(statusStr);
                    updatedVO.setPriority(priority);

                    // 업데이트 메소드 실행
                    service.updateTodolist(updatedVO);
                    System.out.println();
                    System.out.println("[시스템] ID " + id + "번 항목의 정보가 성공적으로 업데이트되었습니다.");
                    System.out.println("---------------------------------------------------------");
                }
            } else if (menu == 4) {
                /*
                 * 할 일 삭제
                 * */
                System.out.println("[ 할 일 삭제 ]");
                System.out.println("---------------------------------------------------------");

                System.out.print("> 삭제할 항목의 ID를 입력하세요: ");
                // 입력 스캐너 받음
                int id = sc.nextInt();
                // 버퍼 정리.
                sc.nextLine();

                service.deleteTodolist(id);

                System.out.println("[시스템] ID " + id + "번 항목의 정보가 성공적으로 삭제되었습니다.");
                System.out.println("---------------------------------------------------------");
            }
        }
        // 입력 닫기
    }
}






