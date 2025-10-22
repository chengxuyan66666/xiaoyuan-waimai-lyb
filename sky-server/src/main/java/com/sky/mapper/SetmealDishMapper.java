package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 查询dishid是否包含再套餐中
     * @param dishIds
     * @return
     */
    List<Long> selectIdsByDishIds(List<Long> dishIds);

    /**
     * 套餐中添加菜品
     * @param dishes
     */
    void insert(List<SetmealDish> dishes);

    /**
     * 删除套餐中的菜品
     * @param setmealIds
     */
    void deleteBySetmealIds(List<Long> setmealIds);
    @Select("select * from setmeal_dish where setmeal_id=#{setmealId}")
    List<SetmealDish> selectBySetmealId(Long setmealId);

    /**
     * 根据一个setmealId删除修改套餐中菜品
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{setmeanlId}")
    void deleteBySetmealId(Long setmealId);
}
