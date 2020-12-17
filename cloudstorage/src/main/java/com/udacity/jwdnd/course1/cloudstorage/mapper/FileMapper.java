package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid=#{userId}")
    List<File> getFiles(Integer userId);

    @Select("SELECT * FROM files WHERE filename=#{filename} and userid=#{userId}")
    File getFile(String filename, Integer userId);

    @Insert("INSERT into Files(filename, contenttype, filesize, userid, filedata) VALUES(#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void insertFile(File file);

    @Delete("DELETE FROM files WHERE fileid=#{fileId}")
    void deleteFile(Integer fileId);
}
