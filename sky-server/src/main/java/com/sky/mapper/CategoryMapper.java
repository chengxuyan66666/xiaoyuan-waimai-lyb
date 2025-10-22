package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     *修改分类
     * @param category
     */
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> list(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     *新增种类
     * @param category
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user) values " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Category category);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Select("select * from category where  type=#{type}")
    List<Category> selectByType(Integer type);

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    @Select("select * from category where id=#{id}")
    Category selectById(Long id);

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */
    @Delete("delete from category where id =#{id}")
    void  delect(Long id);


    /**
     * 根据类型查询分类，查询在售的
     * @param category
     * @return
     */
    List<Category> selectCategories(Category category);
}
