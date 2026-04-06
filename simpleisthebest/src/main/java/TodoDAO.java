import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* DAO 파일중 중복 되는 코드 리팩토링 후 깃허브 올릴 예정 */
public class TodoDAO {

    public void insert(TodoVO vo) throws Exception {

        // H2 DB 연결 설정
        Connection conn = DBConnection.getConnection();
        // insert 메소드시 해당 쿼리문 탐
        // (?) 는 값을 나중에 받겠다는 뜻
        // id 가 null 이면 0으로 바꾼다음, id 컬럼 데이터중 최대값에서 +1 해서 id 으로 넣음. id 를 순차적으로 넣기 위한 sql
        String sql = "INSERT INTO TODOLIST (ID, STATUS, PRIORITY, CREATED_TIME, TASK) " + "VALUES ((SELECT IFNULL(MAX(ID), 0) + 1 FROM TODOLIST), ?, ?, ?, ?)";
        // insert를 날릴 준비를 하는 DB연결객체를 만듦
        PreparedStatement ps = conn.prepareStatement(sql);
        // 1은 sql 변수에 담긴 "?" 의 순서. 즉 "?"가 첫번째로 오는 자리에 task 파라미터를 넣겠다는 뜻
        ps.setString(1, vo.getStatus());
        ps.setInt(2, vo.getPriority());
        // 타임스탬프는 형식 맞춰줘야함
        ps.setTimestamp(3, vo.getCreatedTime());
        ps.setString(4, vo.getTask());
        // 지금까지 준비한 sql 에 담긴 쿼리를 실제로 DB에 실행한다
        ps.executeUpdate();
        // 객체 닫는다
        ps.close();
        // 커넥션도 닫는다
        conn.close();
    }


    public List<TodoVO> searchTodolist() throws Exception {
        // H2 DB 연결 설정
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT ID, STATUS, PRIORITY, CREATED_TIME, TASK FROM TODOLIST ORDER BY ID ASC";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<TodoVO> list = new ArrayList<>();

        while (rs.next()) {
            TodoVO vo = new TodoVO();

            vo.setId(rs.getInt("ID"));
            vo.setStatus(rs.getString("STATUS"));
            vo.setPriority(rs.getInt("PRIORITY"));
            vo.setCreatedTime(rs.getTimestamp("CREATED_TIME"));
            vo.setTask(rs.getString("TASK"));

            list.add(vo);
        }

        rs.close();
        ps.close();
        conn.close();

        return list;
    }

    public void updateTodolist(TodoVO updatedVO) throws Exception {

        // H2 DB 연결 설정
        Connection conn = DBConnection.getConnection();

        String sql = "UPDATE TODOLIST SET TASK = ?, STATUS = ?, PRIORITY = ?, CREATED_TIME = ? WHERE ID = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // 1번째 ? 자리에 = task (할일) 넣음.
        ps.setString(1, updatedVO.getTask());
        // 2번째 ? 자리에 = status (상태) 넣음. 나머지도 동일함.
        ps.setString(2, updatedVO.getStatus());
        ps.setInt(3, updatedVO.getPriority());
        ps.setTimestamp(4, currentTime);
        ps.setInt(5, updatedVO.getId());
        // 실제로 실행
        ps.executeUpdate();

        // 객체를 닫음으로서 메모리 정리
        ps.close();
        // DB 연결 종료함
        conn.close();
    }


    public TodoVO getTodolistById(int id) throws Exception {

        // H2 DB 연결 설정
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT ID, STATUS, PRIORITY, CREATED_TIME, TASK FROM TODOLIST WHERE ID = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        // 입력한 id 정보를 다시 main 으로 던질 vo 그릇임.
        TodoVO currentVO = null;

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            currentVO = new TodoVO();
            currentVO.setId(rs.getInt("ID"));
            currentVO.setStatus(rs.getString("STATUS"));
            currentVO.setPriority(rs.getInt("PRIORITY"));
            currentVO.setCreatedTime(rs.getTimestamp("CREATED_TIME"));
            currentVO.setTask(rs.getString("TASK"));
        }

        rs.close();
        ps.close();
        conn.close();

        return currentVO;
    }

    public void deleteTodolist(int id) throws Exception {

        // H2 DB 연결 설정
        Connection conn = DBConnection.getConnection();

        String sql = "DELETE FROM TODOLIST WHERE ID = ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, id);

        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public List<TodoVO> searchByKeyword(String keyword) throws Exception {
        // keyword 가 들어간 행 여러개일 경우 대비하여 리스트로 받음
        List<TodoVO> list = new ArrayList<>();

        Connection conn = DBConnection.getConnection();

        String sql = "SELECT ID, TASK, STATUS, PRIORITY, CREATED_TIME FROM TODOLIST WHERE TASK LIKE ?";

        // db conn 한다음 sql 실행
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");

        // 쿼리 실행하는 명령어
        ResultSet rs = ps.executeQuery();

        // while 문으로 하나하나 조회하면서 해당 키워드 들어간 행 꺼냄.
        while (rs.next()) {
            TodoVO vo = new TodoVO();
            vo.setId(rs.getInt("ID"));
            vo.setTask(rs.getString("TASK"));
            vo.setStatus(rs.getString("STATUS"));
            vo.setPriority(rs.getInt("PRIORITY"));
            vo.setCreatedTime(rs.getTimestamp("CREATED_TIME"));

            list.add(vo);
        }

        rs.close();
        ps.close();
        conn.close();

        // 해당 키워드가 담긴 행들 list 으로 담아서 반환함.
        return list;

    }

    public List<TodoVO> searchByStatus(String selectedStatus) throws Exception {

        // 완료 -> 3개의 행이 나오므로 리스트 사용
        List<TodoVO> list = new ArrayList<>();
        // DB 연결
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT ID, TASK, STATUS, PRIORITY, CREATED_TIME " +
                "FROM TODOLIST " +
                "WHERE STATUS = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        // ? 첫번쨰에 해당 '완료' 등 상태값 들어감. 기존 데이터와 맞는지 조회함.
        ps.setString(1, selectedStatus);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            TodoVO vo = new TodoVO();
            vo.setId(rs.getInt("ID"));
            vo.setTask(rs.getString("TASK"));
            vo.setStatus(rs.getString("STATUS"));
            vo.setPriority(rs.getInt("PRIORITY"));
            vo.setCreatedTime(rs.getTimestamp("CREATED_TIME"));

            list.add(vo);
        }

        rs.close();
        ps.close();
        conn.close();

        return list;
    }
}