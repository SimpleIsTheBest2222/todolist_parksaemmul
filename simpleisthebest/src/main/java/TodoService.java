import java.util.List;

public interface TodoService {
    // 서비스단은 상속개념이 아니라 구현임
    // 이 기능은 있어야 한다만 정의하는거임
    // 신규 추가 메소드 생성
    void createTodolist(TodoVO vo) throws Exception;

    // 목록을 조회하는 메소드 생성
    List<TodoVO> searchTodolist() throws Exception;

    // 업데이트 메소드 생성
    void updateTodolist(TodoVO updatedVO) throws Exception;

    // 업데이트시 필요한 id 조회하는 메소드 생성
    TodoVO getTodolistById(int id) throws Exception;

    // 삭제시 필요한 메소드 생성
    void deleteTodolist(int id) throws Exception;

    // 키워드 조회하는 메소드 생성
    List<TodoVO> searchByKeyword(String keyword) throws Exception;

    // 필터링 하는 메소드 생성
    List<TodoVO> searchByStatus(String selectedStatus) throws Exception;
}
