package com.sbs.untact.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.untact.dto.Reply;

@Mapper
public interface ReplyDao {
	
	public List<Reply> getForPrintReplies(@Param("relTypeCode")String relTypeCode, @Param("relId")int relId);

	public Reply getReply(@Param("id")Integer id);

	public void deleteReply(@Param("id")Integer id);

	public void modifyReply(@Param("id")int id, @Param("body")String body);			

}
