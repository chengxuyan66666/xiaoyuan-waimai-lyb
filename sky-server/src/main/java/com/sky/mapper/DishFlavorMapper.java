package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.result.Result;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     *添加菜品口味
     * @param flavors
     */
    //@AutoFill(OperationType.INSERT)
    void insert(List<DishFlavor> flavors);

    /**
     * 删除菜品口味
     * @param dishIds
     */
    void delectByDishIds(List<Long> dishIds);

    /**
     * dishId查询菜品口味
     * @param dishId
     */
    @Select("select* from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor>selectByDishId(Long dishId);

    /**
     * 根据一个菜品id删除菜品口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void delectByDishId(Long dishId);

}
