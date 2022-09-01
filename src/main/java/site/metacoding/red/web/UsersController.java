package site.metacoding.red.web;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.domain.users.UsersDao;
import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.UpdateDto;
import site.metacoding.red.web.dto.response.RespDto;

@RequiredArgsConstructor //final 붙어있는애들만 생성자를 받아준다
@RestController
public class UsersController {

	private final UsersDao usersDao;//final: 해당 객체가 new 될 때 값이 있어야한다 -> 그냥 같은 타입을 IoC 컨테이너에서 찾아서 주입해주는 역할
	
	@GetMapping("/users/{id}")
	public RespDto<?> getUsers(@PathVariable Integer id) {
		return new RespDto<>(1, "성공", usersDao.findById(id));
	}
	
	@GetMapping("/users")
	public RespDto<?> getUsersList(){
		return new RespDto<>(1, "성공", usersDao.findAll());
	}
	
	@PostMapping("/users")
	public RespDto<?> insert(JoinDto joinDto) {//body가 없어서 모르니까 ?(Object type)를 쓴다
		usersDao.insert(joinDto);
		return new RespDto<>(1, "회원가입완료", null);//201번 - insert됨
	}
	
	@PutMapping("/users/{id}")
	public RespDto<?> update(@PathVariable Integer id, UpdateDto updateDto){
		//1번 : id로 select하자. (영속화(Persistence) -> DB에 있는 row를 자바오브젝트로 옮기는것)
		Users usersPS = usersDao.findById(id);
		
		//2번 : 변경
		usersPS.전체수정(updateDto);
		
//		Users users = new Users();
//		users.setId(id);
//		users.setUsername(updateDto.getUsername());
//		users.setPassword(updateDto.getPassword());
//		users.setEmail(updateDto.getEmail());
//		usersDao.updateById(users);
		
		//3번 : 영속화된 오브젝트로 update하기
		usersDao.update(usersPS);
		return new RespDto<>(1, "수정완료", null);
	}
	
	@PutMapping("/users/{id}/password")
	public RespDto<?> updatePassword(@PathVariable Integer id, String password){
		//1번 영속화
		Users usersPS = usersDao.findById(id);
		//2번 부분 변경
		usersPS.패스워드수정(password);
		//3번 전체 업데이트
		usersDao.update(usersPS);
		return null;
	}
	
	@DeleteMapping("/users/{id}")
	public RespDto<?> delete(@PathVariable Integer id){
		usersDao.deleteById(id);
		return new RespDto<>(1, "삭제완료", null);
	}
	
}
