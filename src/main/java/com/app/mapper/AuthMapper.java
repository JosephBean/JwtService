package com.app.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.app.dto.RoleDTO;
import com.app.dto.UserDTO;

@Mapper
public interface AuthMapper {
	
	@Select("SELECT userNo, userNm, userPwd, userEnable FROM user WHERE userNm = #{userNm}")
	public Optional<UserDTO> findByUser(String userNm);

	@Select("SELECT roleNm FROM user_role AS ur INNER JOIN role AS r WHERE ur.roleNo = r.roleNo AND ur.userNo = #{userNo}")
	public List<RoleDTO> findByRoles(UserDTO userDTO);
	
}
