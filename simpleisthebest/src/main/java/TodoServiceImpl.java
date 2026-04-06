import java.util.List;

public class TodoServiceImpl implements TodoService {
    // serviceImpl 단은 dao 를 통해서만 DB에 접근 가능하기 때문에 dao 객체를 생성해주는 거임
    private TodoDAO todoDAO = new TodoDAO();

    // 인터페이스(service단) 에서 만든 메소드를 재정의해서 임플단에서 직접 구현하고 있어서 override 적음
    @Override
    public void createTodolist(TodoVO vo) throws Exception {
        todoDAO.insert(vo);
    }

    @Override
    public List<TodoVO> searchTodolist() throws Exception {
        return todoDAO.searchTodolist();
    }

    @Override
    public void updateTodolist(TodoVO updatedVO) throws Exception {
        todoDAO.updateTodolist(updatedVO);
    }

    @Override
    public TodoVO getTodolistById(int id) throws Exception {
        return todoDAO.getTodolistById(id);
    }

    @Override
    public void deleteTodolist(int id) throws Exception {
        todoDAO.deleteTodolist(id);
    }

    @Override
    public List<TodoVO> searchByKeyword(String keyword)  throws Exception {
        return todoDAO.searchByKeyword(keyword);
    }

    @Override
    public List<TodoVO> searchByStatus(String selectedStatus) throws Exception {
        return todoDAO.searchByStatus(selectedStatus);
    }

}