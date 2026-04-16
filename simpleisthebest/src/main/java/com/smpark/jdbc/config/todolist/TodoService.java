package com.smpark.jdbc.config.todolist;

import java.util.List;

public interface TodoService {
    // 서비스단은 상속개념이 아니라 구현임
    // 이 기능은 있어야 한다만 정의하는거임
    // 신규 추가 메소드 생성
    // 필요한 데이터는 이미 vo 에 set 메서드 써서 넣음. db 로 전달가능한상태.
    void createTodolist(TodoVO vo) throws Exception;

    // 목록을 조회하는 메소드 생성
    List<TodoVO> searchTodolist() throws Exception;

    // 업데이트 메소드 생성
    // 사용자가 넣은값을 수기로 vo에 담음.
    // public void update(@RequestBody TodoVO vo)
    // 필요한 데이터는 이미 vo 에 set 메서드 써서 넣음. db 로 전달가능한상태.
    // 수정/삭제는 결과값이 필요없음. 성공하면 끝. 실패하면 exception.
    // 결과값이 필요없다는거지 응답값이 필요없다는 얘기는 아님.
    // DAO / Service 레벨에서 return 값이 없어도 된다는 뜻임.
    // DB 입장에서는 "UPDATE 실행만 되면 끝" 이라서 그럼.
    void updateTodolist(TodoVO updatedVO) throws Exception;

    // 업데이트시 필요한 id 조회하는 메소드 생성
    // 쿼리 결과가 VO에 담기면 vo.getId(), vo.getStatus() 이런 식으로 꺼낼 수 있음. DAO 단에 꺼낼값 쿼리로 정의되어있음. 
    TodoVO getTodolistById(int id) throws Exception;

    // 삭제시 필요한 메소드 생성
    void deleteTodolist(int id) throws Exception;

    // 키워드 조회하는 메소드 생성
    List<TodoVO> searchByKeyword(String keyword) throws Exception;

    // 진행상태로 필터링 하는 메소드 생성
    List<TodoVO> searchByStatus(String selectedStatus) throws Exception;

    // 우선순위로 조회하는 메소드 생성
    List<TodoVO> searchByPriority(int priority) throws Exception;

    void studyCollection() throws Exception;

}
